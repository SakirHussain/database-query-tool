package com.assignment.datasets.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RecordQueryRepositoryImpl implements RecordQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, List<JsonNode>> groupAndSort(
            String dataset, 
            String groupKey,
            @Nullable String sortKey, 
            Sort.Direction dir) {
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT payload->>'").append(groupKey).append("' as group_key, payload ")
           .append("FROM records WHERE dataset_name = :dataset ");
        
        if (sortKey != null) {
            sql.append("ORDER BY payload->>'").append(sortKey).append("' ");
            sql.append(dir == Sort.Direction.ASC ? "ASC" : "DESC");
        }
        
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("dataset", dataset);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        Map<String, List<JsonNode>> groupedResults = new HashMap<>();
        
        for (Object[] row : results) {
            String groupValue = (String) row[0];
            if (groupValue == null) {
                groupValue = "null"; // Handle null group values
            }
            
            try {
                String payloadJson = (String) row[1];
                JsonNode payload = objectMapper.readTree(payloadJson);
                
                groupedResults.computeIfAbsent(groupValue, k -> new ArrayList<>()).add(payload);
            } catch (Exception e) {
                // Log error and continue processing other records
                System.err.println("Error parsing JSON payload: " + e.getMessage());
            }
        }
        
        // If sortKey is provided and we have scalar values, sort within each group
        if (sortKey != null) {
            for (List<JsonNode> group : groupedResults.values()) {
                sortJsonNodeList(group, sortKey, dir);
            }
        }
        
        return groupedResults;
    }

    @Override
    public List<JsonNode> sortOnly(String dataset, String sortKey, Sort.Direction dir) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT payload FROM records WHERE dataset_name = :dataset ");
        sql.append("ORDER BY payload->>'").append(sortKey).append("' ");
        sql.append(dir == Sort.Direction.ASC ? "ASC" : "DESC");
        
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("dataset", dataset);
        
        @SuppressWarnings("unchecked")
        List<String> results = query.getResultList();
        
        List<JsonNode> jsonResults = new ArrayList<>();
        
        for (String payloadJson : results) {
            try {
                JsonNode payload = objectMapper.readTree(payloadJson);
                jsonResults.add(payload);
            } catch (Exception e) {
                // Log error and continue processing other records
                System.err.println("Error parsing JSON payload: " + e.getMessage());
            }
        }
        
        // Fallback to Java Streams if any JSON value is non-scalar
        if (hasNonScalarValues(jsonResults, sortKey)) {
            return sortWithJavaStreams(jsonResults, sortKey, dir);
        }
        
        return jsonResults;
    }
    
    /**
     * Sorts a list of JsonNode objects by a specific key using Java Streams
     */
    private List<JsonNode> sortWithJavaStreams(List<JsonNode> nodes, String sortKey, Sort.Direction dir) {
        return nodes.stream()
            .sorted((a, b) -> {
                JsonNode valueA = a.get(sortKey);
                JsonNode valueB = b.get(sortKey);
                
                if (valueA == null && valueB == null) return 0;
                if (valueA == null) return dir == Sort.Direction.ASC ? -1 : 1;
                if (valueB == null) return dir == Sort.Direction.ASC ? 1 : -1;
                
                String strA = valueA.asText();
                String strB = valueB.asText();
                
                // Try to compare as numbers first
                try {
                    BigDecimal numA = new BigDecimal(strA);
                    BigDecimal numB = new BigDecimal(strB);
                    int result = numA.compareTo(numB);
                    return dir == Sort.Direction.ASC ? result : -result;
                } catch (NumberFormatException e) {
                    // Fall back to string comparison
                    int result = strA.compareTo(strB);
                    return dir == Sort.Direction.ASC ? result : -result;
                }
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts a list of JsonNode objects in place by a specific key
     */
    private void sortJsonNodeList(List<JsonNode> nodes, String sortKey, Sort.Direction dir) {
        nodes.sort((a, b) -> {
            JsonNode valueA = a.get(sortKey);
            JsonNode valueB = b.get(sortKey);
            
            if (valueA == null && valueB == null) return 0;
            if (valueA == null) return dir == Sort.Direction.ASC ? -1 : 1;
            if (valueB == null) return dir == Sort.Direction.ASC ? 1 : -1;
            
            String strA = valueA.asText();
            String strB = valueB.asText();
            
            // Try to compare as numbers first
            try {
                BigDecimal numA = new BigDecimal(strA);
                BigDecimal numB = new BigDecimal(strB);
                int result = numA.compareTo(numB);
                return dir == Sort.Direction.ASC ? result : -result;
            } catch (NumberFormatException e) {
                // Fall back to string comparison
                int result = strA.compareTo(strB);
                return dir == Sort.Direction.ASC ? result : -result;
            }
        });
    }
    
    /**
     * Checks if any of the JSON values for the specified key are non-scalar (objects or arrays)
     */
    private boolean hasNonScalarValues(List<JsonNode> nodes, String sortKey) {
        return nodes.stream()
            .map(node -> node.get(sortKey))
            .anyMatch(value -> value != null && (value.isObject() || value.isArray()));
    }
} 