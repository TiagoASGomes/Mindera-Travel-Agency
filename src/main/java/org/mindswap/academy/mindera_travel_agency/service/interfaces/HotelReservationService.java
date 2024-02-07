package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotUpdateToDifferentInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelReservationService {
    Page<HotelReservationGetDto> getAll(Pageable page);

    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, InvalidCheckInOutDateException;

    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, CannotUpdateToDifferentInvoiceException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException;

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws InvalidCheckInOutDateException, HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto addRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto removeRoom(Long id, Long roomId) throws HotelReservationNotFoundException, PaymentCompletedException, RoomNotFoundException, InvoiceNotFoundException;

    void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;


}
