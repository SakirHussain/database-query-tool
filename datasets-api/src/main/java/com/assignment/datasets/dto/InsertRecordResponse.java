package com.assignment.datasets.dto;

import java.util.Objects;

public class InsertRecordResponse {

    private Long recordId;
    private String dataset;
    private String message;

    public InsertRecordResponse() {}

    public InsertRecordResponse(Long recordId, String dataset, String message) {
        this.recordId = recordId;
        this.dataset = dataset;
        this.message = message;
    }

    public Long getRecordId() {
        return recordId;
    }

    public String getDataset() {
        return dataset;
    }

    public String getMessage() {
        return message;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertRecordResponse that = (InsertRecordResponse) o;
        return Objects.equals(recordId, that.recordId)
                && Objects.equals(dataset, that.dataset)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, dataset, message);
    }

    @Override
    public String toString() {
        return "InsertRecordResponse{"
                + "recordId=" + recordId
                + ", dataset='" + dataset + '\''
                + ", message='" + message + '\''
                + "}";
    }
} 