package org.mindswap.academy.mindera_travel_agency.controller;

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

@RestController
@RequestMapping("/api/v1/hotel_reservations")
public class HotelReservationController {

    private final HotelReservationService hotelReservationService;

    @Autowired
    public HotelReservationController(HotelReservationService hotelReservationService) {
        this.hotelReservationService = hotelReservationService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<HotelReservationGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(hotelReservationService.getAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> getById(@PathVariable Long id) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<HotelReservationGetDto> create(@Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, ReservationAlreadyExistsException {
        return new ResponseEntity<>(hotelReservationService.create(hotelReservation), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> updateReservation(@PathVariable Long id, @Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, CannotUpdateToDifferentInvoiceException, InvalidCheckInOutDateException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateReservation(id, hotelReservation));
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<HotelReservationGetDto> updateDuration(@PathVariable Long id, @Valid @RequestBody HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateDuration(id, hotelReservation));
    }

    @PatchMapping("/{id}/rooms/add")
    public ResponseEntity<HotelReservationGetDto> addRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.addRoom(id, room));
    }

    @PatchMapping("/{id}/rooms/{roomId}/remove")
    public ResponseEntity<HotelReservationGetDto> removeRoom(@PathVariable Long id, @PathVariable Long roomId) throws HotelReservationNotFoundException, RoomNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.removeRoom(id, roomId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        hotelReservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
