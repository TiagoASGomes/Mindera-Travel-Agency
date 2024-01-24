package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationRoomsDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.util.enums.SortParameter;

import java.util.List;

public interface HotelReservationService {
    List<HotelReservationGetDto> getAll();

    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    List<HotelReservationGetDto> getAllByUser(SortParameter sortBy, Long userId);

    List<HotelReservationGetDto> getAllByUserAndByName(String hotelName, SortParameter sortBy, Long userId);

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation);

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException;

    HotelReservationGetDto updateRooms(Long id, HotelReservationRoomsDto hotelReservation) throws HotelReservationNotFoundException;

    HotelReservationGetDto updateHotel(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException;

    void delete(Long id) throws HotelReservationNotFoundException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;
}
