package com.assignment.datasets.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class InsertRecordRequest {

    @NotNull(message = "Payload cannot be null")
    private JsonNode payload;

    public InsertRecordRequest() {}

    public InsertRecordRequest(JsonNode payload) {
        this.payload = payload;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertRecordRequest that = (InsertRecordRequest) o;
        return Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }

    @Override
    public String toString() {
        return "InsertRecordRequest{"
                + "payload=" + payload
                + "}";
    }
} 