package org.mindswap.academy.mindera_travel_agency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.*;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The HotelReservationController class handles the HTTP requests related to hotel reservations.
 */
@RestController
@RequestMapping("/api/v1/hotel_reservations")
public class HotelReservationController {

    private final HotelReservationService hotelReservationService;

    @Autowired
    public HotelReservationController(HotelReservationService hotelReservationService) {
        this.hotelReservationService = hotelReservationService;
    }

    /**
     * Retrieves all hotel reservations.
     *
     * @param page the pageable object for pagination
     * @return the ResponseEntity containing the page of HotelReservationGetDto objects
     */
    @Operation(summary = "Get all hotel reservations")
    @ApiResponse(responseCode = "200", description = "Return all hotel reservations")
    @GetMapping("/")
    public ResponseEntity<Page<HotelReservationGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(hotelReservationService.getAll(page));
    }

    /**
     * Retrieves a specific hotel reservation by its ID.
     *
     * @param id the ID of the hotel reservation
     * @return the ResponseEntity containing the HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     */
    @Operation(summary = "Get a hotel reservation by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the hotel reservation"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> getById(@PathVariable Long id) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.getById(id));
    }

    /**
     * Creates a new hotel reservation.
     *
     * @param hotelReservation the HotelReservationCreateDto object containing the reservation details
     * @return the ResponseEntity containing the created HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws InvalidCheckInOutDateException    if the check-in or check-out date is invalid
     * @throws InvoiceNotFoundException          if the invoice is not found
     * @throws ReservationAlreadyExistsException if a reservation already exists for the given details
     */
    @Operation(summary = "Create a new hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel reservation created"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Reservation already exists for the given invoice, invalid check-in or check-out date")
    })
    @PostMapping("/")
    public ResponseEntity<HotelReservationGetDto> create(@Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, ReservationAlreadyExistsException {
        return new ResponseEntity<>(hotelReservationService.create(hotelReservation), HttpStatus.CREATED);
    }

    /**
     * Updates an existing hotel reservation.
     *
     * @param id               the ID of the hotel reservation
     * @param hotelReservation the HotelReservationCreateDto object containing the updated reservation details
     * @return the ResponseEntity containing the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException       if the hotel reservation is not found
     * @throws CannotUpdateToDifferentInvoiceException if the reservation cannot be updated to a different invoice
     * @throws InvalidCheckInOutDateException          if the check-in or check-out date is invalid
     * @throws InvoiceNotFoundException                if the invoice is not found
     * @throws PaymentCompletedException               if the payment for the reservation is already completed
     */
    @Operation(summary = "Update an existing hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel reservation updated"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Cannot update to a different invoice, or invalid check-in or check-out date, or payment completed"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> updateReservation(@PathVariable Long id, @Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, CannotUpdateToDifferentInvoiceException, InvalidCheckInOutDateException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateReservation(id, hotelReservation));
    }

    /**
     * Updates the duration of a hotel reservation.
     *
     * @param id               the ID of the hotel reservation
     * @param hotelReservation the HotelReservationDurationDto object containing the updated duration details
     * @return the ResponseEntity containing the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws InvalidCheckInOutDateException    if the check-in or check-out date is invalid
     * @throws InvoiceNotFoundException          if the invoice is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     */
    @Operation(summary = "Update the duration of a hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel reservation duration updated"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Invalid check-in or check-out date, or payment completed"),
    })
    @PatchMapping("/{id}/duration")
    public ResponseEntity<HotelReservationGetDto> updateDuration(@PathVariable Long id, @Valid @RequestBody HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateDuration(id, hotelReservation));
    }

    /**
     * Adds a room to a hotel reservation.
     *
     * @param id   the ID of the hotel reservation
     * @param room the ExternalRoomInfoDto object containing the room details
     * @return the ResponseEntity containing the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws InvoiceNotFoundException          if the invoice is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     */
    @Operation(summary = "Add a room to a hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room added to the hotel reservation"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Payment completed"),
    })
    @PatchMapping("/{id}/rooms/add")
    public ResponseEntity<HotelReservationGetDto> addRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.addRoom(id, room));
    }

    /**
     * Removes a room from a hotel reservation.
     *
     * @param id     the ID of the hotel reservation
     * @param roomId the ID of the room to be removed
     * @return the ResponseEntity containing the updated HotelReservationGetDto object
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws RoomNotFoundException             if the room is not found
     * @throws InvoiceNotFoundException          if the invoice is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     */
    @Operation(summary = "Remove a room from a hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room removed from the hotel reservation"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found, or room not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Payment completed"),
    })
    @PatchMapping("/{id}/rooms/{roomId}/remove")
    public ResponseEntity<HotelReservationGetDto> removeRoom(@PathVariable Long id, @PathVariable Long roomId) throws HotelReservationNotFoundException, RoomNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.removeRoom(id, roomId));
    }

    /**
     * Deletes a hotel reservation.
     *
     * @param id the ID of the hotel reservation
     * @return the ResponseEntity with no content
     * @throws HotelReservationNotFoundException if the hotel reservation is not found
     * @throws PaymentCompletedException         if the payment for the reservation is already completed
     */
    @Operation(summary = "Delete a hotel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel reservation deleted"),
            @ApiResponse(responseCode = "404", description = "Hotel reservation not found"),
            @ApiResponse(responseCode = "400", description = "Payment completed"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        hotelReservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
