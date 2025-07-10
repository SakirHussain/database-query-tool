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

    
    @PostMapping(value = "/{name}/record", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public InsertRecordResponse insertRecord(
            @PathVariable String name,
            @Valid @RequestBody InsertRecordRequest request) {
        
        return datasetService.insert(name, request.getPayload());
    }

    
    @GetMapping(value = "/{name}/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResponse queryRecords(
            @PathVariable String name,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        
        Optional<String> groupByOpt = Optional.ofNullable(groupBy);
        Optional<String> sortByOpt = Optional.ofNullable(sortBy);
        
        Optional<Sort.Direction> orderOpt = Optional.empty();
        if (sortByOpt.isPresent()) {
            if (order != null && "desc".equalsIgnoreCase(order.trim())) {
                orderOpt = Optional.of(Sort.Direction.DESC);
            } else {
                // default to asc, even when no sortBy or groupBy is present
                orderOpt = Optional.of(Sort.Direction.ASC);
            }
        } else if (order != null && !order.trim().isEmpty()) {
            orderOpt = Optional.of("desc".equalsIgnoreCase(order.trim()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC);
        }
        
        return datasetService.query(name, groupByOpt, sortByOpt, orderOpt);
    }
} 