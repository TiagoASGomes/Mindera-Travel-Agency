package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;

import java.util.List;

public interface HotelReservationService {
    List<HotelReservationGetDto> getAll();

    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    List<HotelReservationGetDto> getAllByUser(String sortBy, Long userId);

    List<HotelReservationGetDto> getAllByUserAndByName(String hotelName, String sortBy, Long userId);

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException;

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException;

    HotelReservationGetDto addRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException;

    HotelReservationGetDto removeRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException;

    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException;

    void delete(Long id) throws HotelReservationNotFoundException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;
}
