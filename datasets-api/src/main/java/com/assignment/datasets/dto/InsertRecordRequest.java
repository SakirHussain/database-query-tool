package com.assignment.datasets.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class InsertRecordRequest {

    @NotNull(message = "Payload cannot be null")
    private JsonNode payload;

    // Default constructor
    public InsertRecordRequest() {}

    // Constructor with parameters
    public InsertRecordRequest(JsonNode payload) {
        this.payload = payload;
    }

    // Getter
    public JsonNode getPayload() {
        return payload;
    }

    // Setter
    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertRecordRequest that = (InsertRecordRequest) o;
        return Objects.equals(payload, that.payload);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }

    // toString method
    @Override
    public String toString() {
        return "InsertRecordRequest{"
                + "payload=" + payload
                + "}";
    }
} 