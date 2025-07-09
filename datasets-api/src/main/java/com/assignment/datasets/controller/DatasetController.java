package com.assignment.datasets.controller;

import com.assignment.datasets.dto.InsertRecordRequest;
import com.assignment.datasets.dto.InsertRecordResponse;
import com.assignment.datasets.dto.QueryResponse;
import com.assignment.datasets.service.DatasetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    /**
     * Insert a new record into the specified dataset
     * @param name the dataset name
     * @param request the validated insert request containing the payload
     * @return response with inserted record details
     */
    @PostMapping(value = "/{name}/record", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public InsertRecordResponse insertRecord(
            @PathVariable String name,
            @Valid @RequestBody InsertRecordRequest request) {
        
        return datasetService.insert(name, request.getPayload());
    }

    /**
     * Query records from the specified dataset with optional grouping and sorting
     * @param name the dataset name
     * @param groupBy optional field to group records by
     * @param sortBy optional field to sort records by
     * @param order optional sort direction (asc|desc, default asc)
     * @return response containing the queried records
     */
    @GetMapping(value = "/{name}/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResponse queryRecords(
            @PathVariable String name,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        
        // Convert string parameters to Optional
        Optional<String> groupByOpt = Optional.ofNullable(groupBy);
        Optional<String> sortByOpt = Optional.ofNullable(sortBy);
        
        // Parse order parameter to Sort.Direction
        // Only set order if sortBy is present OR if order is explicitly provided
        Optional<Sort.Direction> orderOpt = Optional.empty();
        if (sortByOpt.isPresent()) {
            // If sortBy is present, determine sort direction
            if (order != null && "desc".equalsIgnoreCase(order.trim())) {
                orderOpt = Optional.of(Sort.Direction.DESC);
            } else {
                // Default to ASC when sortBy is present but no order specified, or order is "asc"
                orderOpt = Optional.of(Sort.Direction.ASC);
            }
        } else if (order != null && !order.trim().isEmpty()) {
            // If order is specified but no sortBy, this will trigger validation error in service
            orderOpt = Optional.of("desc".equalsIgnoreCase(order.trim()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC);
        }
        
        return datasetService.query(name, groupByOpt, sortByOpt, orderOpt);
    }
} 