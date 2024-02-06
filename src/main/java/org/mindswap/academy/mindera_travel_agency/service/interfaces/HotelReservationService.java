package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotChangeInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;

import java.util.List;

public interface HotelReservationService {
    List<HotelReservationGetDto> getAll();

    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException, UnirestException, JsonProcessingException;

    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, InvalidCheckInOutDateException;

    HotelReservationGetDto addRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    HotelReservationGetDto removeRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, RoomNotFoundException;

    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException, CannotChangeInvoiceException;

    void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException;

    HotelReservation findById(Long id) throws HotelReservationNotFoundException;
}
