package com.assignment.datasets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryResponse {

    private Map<String, List<JsonNode>> groupedRecords;
    private List<JsonNode> sortedRecords;

    // Default constructor
    public QueryResponse() {}

    // Constructor with parameters
    public QueryResponse(Map<String, List<JsonNode>> groupedRecords, List<JsonNode> sortedRecords) {
        this.groupedRecords = groupedRecords;
        this.sortedRecords = sortedRecords;
    }

    // Static factory method for grouped results
    public static QueryResponse grouped(Map<String, List<JsonNode>> groupedRecords) {
        return new QueryResponse(groupedRecords, null);
    }

    // Static factory method for sorted results
    public static QueryResponse sorted(List<JsonNode> sortedRecords) {
        return new QueryResponse(null, sortedRecords);
    }

    // Getters
    public Map<String, List<JsonNode>> getGroupedRecords() {
        return groupedRecords;
    }

    public List<JsonNode> getSortedRecords() {
        return sortedRecords;
    }

    // Setters
    public void setGroupedRecords(Map<String, List<JsonNode>> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }

    public void setSortedRecords(List<JsonNode> sortedRecords) {
        this.sortedRecords = sortedRecords;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResponse that = (QueryResponse) o;
        return Objects.equals(groupedRecords, that.groupedRecords)
                && Objects.equals(sortedRecords, that.sortedRecords);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(groupedRecords, sortedRecords);
    }

    // toString method
    @Override
    public String toString() {
        return "QueryResponse{"
                + "groupedRecords=" + groupedRecords
                + ", sortedRecords=" + sortedRecords
                + "}";
    }
} 