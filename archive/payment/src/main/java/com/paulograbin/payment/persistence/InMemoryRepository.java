package com.paulograbin.payment.persistence;

import com.paulograbin.payment.domain.AbstracEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class InMemoryRepository<T extends AbstracEntity> implements EntityRepository<T, Long> {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryRepository.class);

    private long lastId = 1;
    protected final Map<Long, T> map = new HashMap<>();


    @Override
    public <S extends T> S save(S entity) {
        if (entity.getId() != null) {
            map.put(entity.getId(), entity);
            return entity;
        }

        entity.setId(lastId++);
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> findById(Long aLong) {
        if (map.containsKey(aLong)) {
            return Optional.of(map.get(aLong));
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        logger.info("Checking if exist entity with id {}", aLong);
        return map.containsKey(aLong);
    }

    @Override
    public Iterable<T> findAll() {
        return map.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(T entity) {
        throw new UnsupportedOperationException();
    }

    public void deleteAllById(Iterable<? extends Long> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
