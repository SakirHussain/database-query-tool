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
        
        // order cannot be present without sortBy
        if (order.isPresent() && sortBy.isEmpty()) {
            throw new BadRequestException("Sort direction specified but no sort field provided. " +
                "Please provide a sortBy parameter when specifying order.");
        }

        Sort.Direction sortDirection = order.orElse(Sort.Direction.ASC);

        // four possible scenarios
        if (groupBy.isPresent() && sortBy.isPresent()) {
            Map<String, List<JsonNode>> groupedRecords = recordQueryRepository.groupAndSort(
                dataset, groupBy.get(), sortBy.get(), sortDirection
            );
            return QueryResponse.grouped(groupedRecords);

        } else if (groupBy.isPresent()) {
            Map<String, List<JsonNode>> groupedRecords = recordQueryRepository.groupAndSort(
                dataset, groupBy.get(), null, sortDirection
            );
            return QueryResponse.grouped(groupedRecords);

        } else if (sortBy.isPresent()) {
            List<JsonNode> sortedRecords = recordQueryRepository.sortOnly(
                dataset, sortBy.get(), sortDirection
            );
            return QueryResponse.sorted(sortedRecords);

        } else {
            List<RecordEntity> allRecords = recordRepository.findByDatasetName(dataset);
            List<JsonNode> payloads = allRecords.stream()
                .map(RecordEntity::getPayload)
                .toList();
            return QueryResponse.sorted(payloads);
        }
    }
} 