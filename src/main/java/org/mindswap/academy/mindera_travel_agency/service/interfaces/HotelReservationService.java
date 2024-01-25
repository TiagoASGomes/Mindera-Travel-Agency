package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;

import java.util.List;

public interface HotelReservationService {
    List<HotelReservationGetDto> getAll();

    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    List<HotelReservationGetDto> getAllByUser(String sortBy, Long userId);

    List<HotelReservationGetDto> getAllByUserAndByName(String hotelName, String sortBy, Long userId);

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException;

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservationGetDto addRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservationGetDto removeRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException;

    void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;
}
