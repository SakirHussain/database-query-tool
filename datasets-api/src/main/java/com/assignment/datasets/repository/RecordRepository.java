package com.assignment.datasets.repository;

import com.assignment.datasets.model.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    
    List<RecordEntity> findByDatasetName(String datasetName);
} 