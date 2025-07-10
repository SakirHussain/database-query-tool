package com.assignment.datasets.service;

import com.assignment.datasets.dto.InsertRecordResponse;
import com.assignment.datasets.dto.QueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Sort;
import java.util.Optional;

public interface DatasetService {

    InsertRecordResponse insert(String dataset, JsonNode payload);


    QueryResponse query(String dataset,
                       Optional<String> groupBy,
                       Optional<String> sortBy,
                       Optional<Sort.Direction> order);
} 