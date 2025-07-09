package com.assignment.datasets.repository;

import com.assignment.datasets.model.RecordEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.lang.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryRecordRepository implements RecordRepository, RecordQueryRepository {

    private final Map<Long, RecordEntity> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public RecordEntity save(RecordEntity entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public List<RecordEntity> findByDatasetName(String datasetName) {
        return storage.values().stream()
            .filter(entity -> datasetName.equals(entity.getDatasetName()))
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<JsonNode>> groupAndSort(String dataset, String groupKey, 
                                                   @Nullable String sortKey, Sort.Direction dir) {
        List<RecordEntity> records = findByDatasetName(dataset);
        Map<String, List<JsonNode>> groupedResults = new HashMap<>();

        for (RecordEntity record : records) {
            JsonNode payload = record.getPayload();
            JsonNode groupValue = payload.get(groupKey);
            String groupStr = groupValue != null ? groupValue.asText() : "null";
            
            groupedResults.computeIfAbsent(groupStr, k -> new ArrayList<>()).add(payload);
        }

        // Sort within each group if sortKey is provided
        if (sortKey != null) {
            for (List<JsonNode> group : groupedResults.values()) {
                sortJsonNodeList(group, sortKey, dir);
            }
        }

        return groupedResults;
    }

    @Override
    public List<JsonNode> sortOnly(String dataset, String sortKey, Sort.Direction dir) {
        List<RecordEntity> records = findByDatasetName(dataset);
        List<JsonNode> payloads = records.stream()
            .map(RecordEntity::getPayload)
            .collect(Collectors.toList());
        
        sortJsonNodeList(payloads, sortKey, dir);
        return payloads;
    }

    private void sortJsonNodeList(List<JsonNode> nodes, String sortKey, Sort.Direction dir) {
        nodes.sort((a, b) -> {
            JsonNode valueA = a.get(sortKey);
            JsonNode valueB = b.get(sortKey);
            
            if (valueA == null && valueB == null) return 0;
            if (valueA == null) return dir == Sort.Direction.ASC ? -1 : 1;
            if (valueB == null) return dir == Sort.Direction.ASC ? 1 : -1;
            
            String strA = valueA.asText();
            String strB = valueB.asText();
            
            // Try to compare as numbers first
            try {
                BigDecimal numA = new BigDecimal(strA);
                BigDecimal numB = new BigDecimal(strB);
                int result = numA.compareTo(numB);
                return dir == Sort.Direction.ASC ? result : -result;
            } catch (NumberFormatException e) {
                // Fall back to string comparison
                int result = strA.compareTo(strB);
                return dir == Sort.Direction.ASC ? result : -result;
            }
        });
    }

    // Minimal implementations of other JpaRepository methods
    @Override
    public void flush() {}

    @Override
    public <S extends RecordEntity> S saveAndFlush(S entity) {
        return (S) save(entity);
    }

    @Override
    public <S extends RecordEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add((S) save(entity)));
        return result;
    }

    @Override
    public void deleteAllInBatch(Iterable<RecordEntity> entities) {
        entities.forEach(entity -> storage.remove(entity.getId()));
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        longs.forEach(storage::remove);
    }

    @Override
    public void deleteAllInBatch() {
        storage.clear();
    }

    @Override
    public RecordEntity getOne(Long aLong) {
        return storage.get(aLong);
    }

    @Override
    public RecordEntity getById(Long aLong) {
        return storage.get(aLong);
    }

    @Override
    public RecordEntity getReferenceById(Long aLong) {
        return storage.get(aLong);
    }

    @Override
    public <S extends RecordEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends RecordEntity> List<S> findAll(Example<S> example) {
        return new ArrayList<>();
    }

    @Override
    public <S extends RecordEntity> List<S> findAll(Example<S> example, Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public <S extends RecordEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends RecordEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends RecordEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends RecordEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends RecordEntity> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add((S) save(entity)));
        return result;
    }

    @Override
    public Optional<RecordEntity> findById(Long aLong) {
        return Optional.ofNullable(storage.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        return storage.containsKey(aLong);
    }

    @Override
    public List<RecordEntity> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<RecordEntity> findAllById(Iterable<Long> longs) {
        List<RecordEntity> result = new ArrayList<>();
        longs.forEach(id -> {
            RecordEntity entity = storage.get(id);
            if (entity != null) result.add(entity);
        });
        return result;
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public void deleteById(Long aLong) {
        storage.remove(aLong);
    }

    @Override
    public void delete(RecordEntity entity) {
        storage.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        longs.forEach(storage::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends RecordEntity> entities) {
        entities.forEach(entity -> storage.remove(entity.getId()));
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public List<RecordEntity> findAll(Sort sort) {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Page<RecordEntity> findAll(Pageable pageable) {
        return Page.empty();
    }
} 