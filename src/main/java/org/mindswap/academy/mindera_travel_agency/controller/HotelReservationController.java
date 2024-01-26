package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class HotelReservationController {
    @Autowired
    private HotelReservationService hotelReservationService;

    @GetMapping("/")
    public ResponseEntity<List<HotelReservationGetDto>> getAll() {
        return ResponseEntity.ok(hotelReservationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> getById(@PathVariable Long id) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HotelReservationGetDto>> getAllByUser(@PathVariable Long userId, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(hotelReservationService.getAllByUser(sortBy, userId));
    }

    @GetMapping("/user/{userId}/name/{hotelName}")
    public ResponseEntity<List<HotelReservationGetDto>> getAllByUserAndByName(@PathVariable Long userId, @PathVariable String hotelName, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(hotelReservationService.getAllByUserAndByName(hotelName, sortBy, userId));
    }

    @PostMapping("/")
    public ResponseEntity<HotelReservationGetDto> create(@Valid @RequestBody HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException {
        return new ResponseEntity<>(hotelReservationService.create(hotelReservation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<HotelReservationGetDto> updateDuration(@PathVariable Long id, @Valid @RequestBody HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateDuration(id, hotelReservation));
    }

    @PatchMapping("/{id}/rooms/add")
    public ResponseEntity<HotelReservationGetDto> addRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.addRooms(id, room));
    }

    @PatchMapping("/{id}/rooms/remove")
    public ResponseEntity<HotelReservationGetDto> removeRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.removeRooms(id, room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> updateReservation(@PathVariable Long id, @Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(hotelReservationService.updateReservation(id, hotelReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        hotelReservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
