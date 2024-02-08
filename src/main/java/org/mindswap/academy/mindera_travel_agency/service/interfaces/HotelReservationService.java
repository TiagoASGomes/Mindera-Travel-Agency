package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.*;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The HotelReservationService interface provides methods for managing hotel reservations.
 */
public interface HotelReservationService {
    /**
     * Retrieves all hotel reservations.
     *
     * @param page the pageable object for pagination
     * @return a page of HotelReservationGetDto objects
     */
    Page<HotelReservationGetDto> getAll(Pageable page);

    /**
     * Retrieves a hotel reservation by its ID.
     *
     * @param id the ID of the hotel reservation
     * @return the HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     */
    HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException;

    /**
     * Creates a new hotel reservation.
     *
     * @param hotelReservation the HotelReservationCreateDto object containing the reservation details
     * @return the created HotelReservationGetDto object
     * @throws InvoiceNotFoundException          if the invoice is not found
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws InvalidCheckInOutDateException    if the check-in or check-out date is invalid
     * @throws ReservationAlreadyExistsException if a reservation already exists for the given dates and room
     */
    HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, InvalidCheckInOutDateException, ReservationAlreadyExistsException;

    /**
     * Updates an existing hotel reservation.
     *
     * @param id               the ID of the hotel reservation to update
     * @param hotelReservation the HotelReservationCreateDto object containing the updated reservation details
     * @return the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException       if the hotel reservation is not found
     * @throws CannotUpdateToDifferentInvoiceException if trying to update the reservation to a different invoice
     * @throws InvoiceNotFoundException                if the invoice is not found
     * @throws PaymentCompletedException               if the payment for the reservation is already completed
     * @throws InvalidCheckInOutDateException          if the check-in or check-out date is invalid
     */
    HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, CannotUpdateToDifferentInvoiceException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException;

    /**
     * Updates the duration of a hotel reservation.
     *
     * @param id               the ID of the hotel reservation to update
     * @param hotelReservation the HotelReservationDurationDto object containing the updated duration
     * @return the updated HotelReservationGetDto object
     * @throws InvalidCheckInOutDateException    if the check-in or check-out date is invalid
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     * @throws InvoiceNotFoundException          if the invoice is not found
     */
    HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws InvalidCheckInOutDateException, HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    /**
     * Adds a room to a hotel reservation.
     *
     * @param id   the ID of the hotel reservation
     * @param room the ExternalRoomInfoDto object containing the room information
     * @return the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     * @throws InvoiceNotFoundException          if the invoice is not found
     */
    HotelReservationGetDto addRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException;

    /**
     * Removes a room from a hotel reservation.
     *
     * @param id     the ID of the hotel reservation
     * @param roomId the ID of the room to remove
     * @return the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     * @throws RoomNotFoundException             if the room is not found
     * @throws InvoiceNotFoundException          if the invoice is not found
     */
    HotelReservationGetDto removeRoom(Long id, Long roomId) throws HotelReservationNotFoundException, PaymentCompletedException, RoomNotFoundException, InvoiceNotFoundException;

    /**
     * Deletes a hotel reservation.
     *
     * @param id the ID of the hotel reservation to delete
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     */
    void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException;

    /**
     * Finds a hotel reservation by its ID.
     *
     * @param id the ID of the hotel reservation
     * @return the HotelReservation object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     */
    HotelReservation findById(Long id) throws HotelReservationNotFoundException;


}
