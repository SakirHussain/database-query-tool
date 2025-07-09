package com.assignment.datasets.service;

import com.assignment.datasets.dto.InsertRecordResponse;
import com.assignment.datasets.dto.QueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Sort;
import java.util.Optional;

public interface DatasetService {

    /**
     * Inserts a new record into the specified dataset
     * @param dataset the dataset name
     * @param payload the JSON payload to insert
     * @return response containing the inserted record details
     */
    InsertRecordResponse insert(String dataset, JsonNode payload);

    /**
     * Queries records from the specified dataset with optional grouping and sorting
     * @param dataset the dataset name to query
     * @param groupBy optional field to group records by
     * @param sortBy optional field to sort records by
     * @param order optional sort direction (requires sortBy to be present)
     * @return response containing the queried records
     * @throws com.assignment.datasets.exception.BadRequestException if order is present but sortBy is empty
     */
    QueryResponse query(String dataset,
                       Optional<String> groupBy,
                       Optional<String> sortBy,
                       Optional<Sort.Direction> order);
} 