package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelReservationRepository extends JpaRepository<HotelReservation, Long> {

}
