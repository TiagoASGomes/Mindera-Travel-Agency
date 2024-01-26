package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelReservationRepository extends JpaRepository<HotelReservation, Long> {
    @Query(value = "SELECT hr FROM hotel_reservations hr\n" +
            "inner join invoices i ON i.id = hr.invoice_id \n" +
            "inner join users u ON u.id = i.user_id \n" +
            "where u.id = ?1", nativeQuery = true)
    List<HotelReservation> findAllByUser(Long id);
}
