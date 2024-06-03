package org.example.dao;

import java.util.Optional;

public interface BaseDao<K, E> {
    E save(E entity);

    E update(E entity);

    Optional<E> findById(K id);

    void delete(K id);
}
