package org.mindswap.academy.mindera_travel_agency.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserTestRepository extends UserRepository {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrement();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users", nativeQuery = true)
    void deleteAll();
}
