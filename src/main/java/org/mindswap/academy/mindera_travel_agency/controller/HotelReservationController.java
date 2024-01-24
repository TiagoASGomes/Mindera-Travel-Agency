package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationRoomsDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel/reservations")
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

    @PostMapping("/")
    public ResponseEntity<HotelReservationGetDto> create(@Valid @RequestBody HotelReservationCreateDto hotelReservation) {
        return new ResponseEntity<>(hotelReservationService.create(hotelReservation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/duration")
    public ResponseEntity<HotelReservationGetDto> updateDuration(@PathVariable Long id, @Valid @RequestBody HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.updateDuration(id, hotelReservation));
    }

    @PatchMapping("/{id}/rooms")
    public ResponseEntity<HotelReservationGetDto> updateRooms(@PathVariable Long id, @Valid @RequestBody HotelReservationRoomsDto hotelReservation) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.updateRooms(id, hotelReservation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReservationGetDto> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException {
        return ResponseEntity.ok(hotelReservationService.updateHotel(id, hotelReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws HotelReservationNotFoundException {
        hotelReservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
