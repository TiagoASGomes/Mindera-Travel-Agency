package org.mindswap.academy.mindera_travel_agency.repository;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public interface HotelReservationTestRepository extends HotelReservationRepository {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE hotel_reservations ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrement();

//    @Modifying
//    @Transactional
//    @Query(value = "DELETE FROM hotel_reservations", nativeQuery = true)
//    void deleteAll();
}
