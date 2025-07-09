package com.assignment.datasets.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Map;

public interface RecordQueryRepository {
    
    /**
     * Groups records by the specified groupKey and sorts them by sortKey
     * @param dataset the dataset name to filter by
     * @param groupKey the JSON field to group by
     * @param sortKey the JSON field to sort by (nullable)
     * @param dir the sort direction
     * @return Map where keys are group values and values are lists of JsonNode payloads
     */
    Map<String, List<JsonNode>> groupAndSort(
        String dataset, 
        String groupKey,
        @Nullable String sortKey, 
        Sort.Direction dir
    );
    
    /**
     * Sorts records by the specified sortKey
     * @param dataset the dataset name to filter by
     * @param sortKey the JSON field to sort by
     * @param dir the sort direction
     * @return List of JsonNode payloads sorted by the specified key
     */
    List<JsonNode> sortOnly(String dataset, String sortKey, Sort.Direction dir);
} 