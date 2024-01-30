package org.mindswap.academy.mindera_travel_agency.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FlightTicketTestRepository extends FlightTicketRepository {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE courses ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrement();
}
