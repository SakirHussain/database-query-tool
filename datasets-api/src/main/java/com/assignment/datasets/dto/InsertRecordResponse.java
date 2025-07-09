package com.assignment.datasets.dto;

import java.util.Objects;

public class InsertRecordResponse {

    private Long recordId;
    private String dataset;
    private String message;

    // Default constructor
    public InsertRecordResponse() {}

    // Constructor with parameters
    public InsertRecordResponse(Long recordId, String dataset, String message) {
        this.recordId = recordId;
        this.dataset = dataset;
        this.message = message;
    }

    // Getters
    public Long getRecordId() {
        return recordId;
    }

    public String getDataset() {
        return dataset;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertRecordResponse that = (InsertRecordResponse) o;
        return Objects.equals(recordId, that.recordId)
                && Objects.equals(dataset, that.dataset)
                && Objects.equals(message, that.message);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(recordId, dataset, message);
    }

    // toString method
    @Override
    public String toString() {
        return "InsertRecordResponse{"
                + "recordId=" + recordId
                + ", dataset='" + dataset + '\''
                + ", message='" + message + '\''
                + "}";
    }
} 