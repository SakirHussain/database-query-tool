package com.assignment.datasets.service;

import com.assignment.datasets.dto.InsertRecordResponse;
import com.assignment.datasets.dto.QueryResponse;
import com.assignment.datasets.exception.BadRequestException;
import com.assignment.datasets.model.RecordEntity;
import com.assignment.datasets.repository.RecordQueryRepository;
import com.assignment.datasets.repository.RecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatasetServiceImpl implements DatasetService {

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    RecordQueryRepository recordQueryRepository;

    @Override
    public InsertRecordResponse insert(String dataset, JsonNode payload) {
        // Create and save the record entity
        RecordEntity recordEntity = new RecordEntity(dataset, payload);
        RecordEntity savedEntity = recordRepository.save(recordEntity);

        // Return response with saved record details
        return new InsertRecordResponse(
            savedEntity.getId(),
            savedEntity.getDatasetName(),
            "Record inserted successfully"
        );
    }

    @Override
    public QueryResponse query(String dataset,
                              Optional<String> groupBy,
                              Optional<String> sortBy,
                              Optional<Sort.Direction> order) {
        
        // Validate input: if order is present, sortBy must also be present
        if (order.isPresent() && sortBy.isEmpty()) {
            throw new BadRequestException("Sort direction specified but no sort field provided. " +
                "Please provide a sortBy parameter when specifying order.");
        }

        // Determine sort direction (default to ASC if not specified)
        Sort.Direction sortDirection = order.orElse(Sort.Direction.ASC);

        // Switch logic for 4 scenarios
        if (groupBy.isPresent() && sortBy.isPresent()) {
            // Scenario 1: Both groupBy and sortBy - group and sort
            Map<String, List<JsonNode>> groupedRecords = recordQueryRepository.groupAndSort(
                dataset, groupBy.get(), sortBy.get(), sortDirection
            );
            return QueryResponse.grouped(groupedRecords);

        } else if (groupBy.isPresent()) {
            // Scenario 2: Only groupBy - group without sorting
            Map<String, List<JsonNode>> groupedRecords = recordQueryRepository.groupAndSort(
                dataset, groupBy.get(), null, sortDirection
            );
            return QueryResponse.grouped(groupedRecords);

        } else if (sortBy.isPresent()) {
            // Scenario 3: Only sortBy - sort without grouping
            List<JsonNode> sortedRecords = recordQueryRepository.sortOnly(
                dataset, sortBy.get(), sortDirection
            );
            return QueryResponse.sorted(sortedRecords);

        } else {
            // Scenario 4: Neither groupBy nor sortBy - return all records unsorted
            List<RecordEntity> allRecords = recordRepository.findByDatasetName(dataset);
            List<JsonNode> payloads = allRecords.stream()
                .map(RecordEntity::getPayload)
                .toList();
            return QueryResponse.sorted(payloads);
        }
    }
} 