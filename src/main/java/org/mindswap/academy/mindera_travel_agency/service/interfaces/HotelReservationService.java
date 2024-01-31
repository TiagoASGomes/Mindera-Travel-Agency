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

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException;

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto addRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto removeRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException;

    void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;
}
