package com.assignment.datasets.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Objects;

@Entity
@Table(name = "records")
public class RecordEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "dataset_name", nullable = false)
  private String datasetName;

  @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private JsonNode payload;

  public RecordEntity() {}

  public RecordEntity(String datasetName, JsonNode payload) {
    this.datasetName = datasetName;
    this.payload = payload;
  }

  // get
  public Long getId() {
    return id;
  }

  public String getDatasetName() {
    return datasetName;
  }

  public JsonNode getPayload() {
    return payload;
  }

  // set
  public void setId(Long id) {
    this.id = id;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public void setPayload(JsonNode payload) {
    this.payload = payload;
  }

  // equals method
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RecordEntity that = (RecordEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(datasetName, that.datasetName)
        && Objects.equals(payload, that.payload);
  }

  // hashCode method
  @Override
  public int hashCode() {
    return Objects.hash(id, datasetName, payload);
  }

  // toString method
  @Override
  public String toString() {
    return "RecordEntity{"
        + "id="
        + id
        + ", datasetName='"
        + datasetName
        + '\''
        + ", payload="
        + payload
        + "}";
  }
}
