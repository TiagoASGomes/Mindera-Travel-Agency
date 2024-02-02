package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotChangeInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
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

    //TODO add external service
    private final HotelReservationService hotelReservationService;

    @Autowired
    public HotelReservationController(HotelReservationService hotelReservationService) {
        this.hotelReservationService = hotelReservationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<HotelReservationGetDto>> getAll() {
        return ResponseEntity.ok(hotelReservationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> getById(@PathVariable Long id) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<HotelReservationGetDto> create(@Valid @RequestBody HotelReservationCreateDto hotelReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException {
        return new ResponseEntity<>(hotelReservationService.create(hotelReservation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<HotelReservationGetDto> updateDuration(@PathVariable Long id, @Valid @RequestBody HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, InvalidCheckInOutDateException {
        return ResponseEntity.ok(hotelReservationService.updateDuration(id, hotelReservation));
    }

    @PatchMapping("/{id}/rooms/add")
    public ResponseEntity<HotelReservationGetDto> addRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        return ResponseEntity.ok(hotelReservationService.addRoom(id, room));
    }

    @PatchMapping("/{id}/rooms/remove")
    public ResponseEntity<HotelReservationGetDto> removeRoom(@PathVariable Long id, @Valid @RequestBody ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, RoomNotFoundException {
        return ResponseEntity.ok(hotelReservationService.removeRoom(id, room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> updateReservation(@PathVariable Long id, @Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException, CannotChangeInvoiceException {
        return ResponseEntity.ok(hotelReservationService.updateReservation(id, hotelReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        hotelReservationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
