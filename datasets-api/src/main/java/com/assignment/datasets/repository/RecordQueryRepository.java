package com.assignment.datasets.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Map;

public interface RecordQueryRepository {
    
    // group and sort; upon null sortKey, becomes group only
    Map<String, List<JsonNode>> groupAndSort(
        String dataset, 
        String groupKey,
        @Nullable String sortKey, 
        Sort.Direction dir
    );
    
    // sort only
    List<JsonNode> sortOnly(String dataset, String sortKey, Sort.Direction dir);
} 