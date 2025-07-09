package com.assignment.datasets.service;

import com.assignment.datasets.dto.InsertRecordResponse;
import com.assignment.datasets.dto.QueryResponse;
import com.assignment.datasets.exception.BadRequestException;
import com.assignment.datasets.repository.InMemoryRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatasetServiceTest {

    private DatasetServiceImpl datasetService;
    private InMemoryRecordRepository repository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRecordRepository();
        datasetService = new DatasetServiceImpl();
        objectMapper = new ObjectMapper();
        
        // Manually inject dependencies (no Spring context)
        datasetService.recordRepository = repository;
        datasetService.recordQueryRepository = repository;
    }

    @Test
    void insertStoresRecord() throws Exception {
        // Arrange
        String dataset = "test-dataset";
        JsonNode payload = objectMapper.readTree("{\"name\":\"John\",\"age\":30,\"city\":\"NYC\"}");

        // Act
        InsertRecordResponse response = datasetService.insert(dataset, payload);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getRecordId());
        assertEquals(dataset, response.getDataset());
        assertEquals("Record inserted successfully", response.getMessage());
        
        // Verify record was stored
        assertEquals(1, repository.count());
        assertTrue(response.getRecordId() > 0);
    }

    @Test
    void queryFlatList() throws Exception {
        // Arrange
        String dataset = "test-dataset";
        insertTestData(dataset);

        // Act - Query all records without grouping or sorting
        QueryResponse response = datasetService.query(dataset, 
            Optional.empty(), Optional.empty(), Optional.empty());

        // Assert
        assertNotNull(response);
        assertNull(response.getGroupedRecords());
        assertNotNull(response.getSortedRecords());
        assertEquals(3, response.getSortedRecords().size());
        
        // Verify all payloads are present
        List<JsonNode> records = response.getSortedRecords();
        assertTrue(records.stream().anyMatch(r -> "John".equals(r.get("name").asText())));
        assertTrue(records.stream().anyMatch(r -> "Jane".equals(r.get("name").asText())));
        assertTrue(records.stream().anyMatch(r -> "Bob".equals(r.get("name").asText())));
    }

    @Test
    void queryGroupOnly() throws Exception {
        // Arrange
        String dataset = "test-dataset";
        insertTestData(dataset);

        // Act - Group by city without sorting
        QueryResponse response = datasetService.query(dataset, 
            Optional.of("city"), Optional.empty(), Optional.empty());

        // Assert
        assertNotNull(response);
        assertNotNull(response.getGroupedRecords());
        assertNull(response.getSortedRecords());
        
        Map<String, List<JsonNode>> grouped = response.getGroupedRecords();
        assertEquals(2, grouped.size()); // NYC and LA
        assertTrue(grouped.containsKey("NYC"));
        assertTrue(grouped.containsKey("LA"));
        
        // NYC should have 2 records (John and Jane)
        assertEquals(2, grouped.get("NYC").size());
        // LA should have 1 record (Bob)
        assertEquals(1, grouped.get("LA").size());
    }

    @Test
    void querySortOnly() throws Exception {
        // Arrange
        String dataset = "test-dataset";
        insertTestData(dataset);

        // Act - Sort by age ascending
        QueryResponse response = datasetService.query(dataset, 
            Optional.empty(), Optional.of("age"), Optional.of(Sort.Direction.ASC));

        // Assert
        assertNotNull(response);
        assertNull(response.getGroupedRecords());
        assertNotNull(response.getSortedRecords());
        
        List<JsonNode> records = response.getSortedRecords();
        assertEquals(3, records.size());
        
        // Verify ascending order by age: Bob(25), John(30), Jane(35)
        assertEquals(25, records.get(0).get("age").asInt());
        assertEquals(30, records.get(1).get("age").asInt());
        assertEquals(35, records.get(2).get("age").asInt());
        
        assertEquals("Bob", records.get(0).get("name").asText());
        assertEquals("John", records.get(1).get("name").asText());
        assertEquals("Jane", records.get(2).get("name").asText());
    }

    @Test
    void queryGroupAndSort() throws Exception {
        // Arrange
        String dataset = "test-dataset";
        insertTestData(dataset);

        // Act - Group by city and sort by age descending
        QueryResponse response = datasetService.query(dataset, 
            Optional.of("city"), Optional.of("age"), Optional.of(Sort.Direction.DESC));

        // Assert
        assertNotNull(response);
        assertNotNull(response.getGroupedRecords());
        assertNull(response.getSortedRecords());
        
        Map<String, List<JsonNode>> grouped = response.getGroupedRecords();
        assertEquals(2, grouped.size());
        
        // Check NYC group (should have Jane(35) before John(30) - descending by age)
        List<JsonNode> nycRecords = grouped.get("NYC");
        assertEquals(2, nycRecords.size());
        assertEquals("Jane", nycRecords.get(0).get("name").asText());
        assertEquals(35, nycRecords.get(0).get("age").asInt());
        assertEquals("John", nycRecords.get(1).get("name").asText());
        assertEquals(30, nycRecords.get(1).get("age").asInt());
        
        // Check LA group (should have only Bob)
        List<JsonNode> laRecords = grouped.get("LA");
        assertEquals(1, laRecords.size());
        assertEquals("Bob", laRecords.get(0).get("name").asText());
        assertEquals(25, laRecords.get(0).get("age").asInt());
    }

    @Test
    void queryThrowsExceptionWhenOrderWithoutSortBy() {
        // Arrange
        String dataset = "test-dataset";

        // Act & Assert - Should throw BadRequestException when order is specified without sortBy
        BadRequestException exception = assertThrows(BadRequestException.class, () ->
            datasetService.query(dataset, Optional.empty(), Optional.empty(), Optional.of(Sort.Direction.ASC))
        );
        
        assertTrue(exception.getMessage().contains("Sort direction specified but no sort field provided"));
    }

    private void insertTestData(String dataset) throws Exception {
        // Insert test records with different ages and cities
        JsonNode john = objectMapper.readTree("{\"name\":\"John\",\"age\":30,\"city\":\"NYC\"}");
        JsonNode jane = objectMapper.readTree("{\"name\":\"Jane\",\"age\":35,\"city\":\"NYC\"}");
        JsonNode bob = objectMapper.readTree("{\"name\":\"Bob\",\"age\":25,\"city\":\"LA\"}");
        
        datasetService.insert(dataset, john);
        datasetService.insert(dataset, jane);
        datasetService.insert(dataset, bob);
    }
} 