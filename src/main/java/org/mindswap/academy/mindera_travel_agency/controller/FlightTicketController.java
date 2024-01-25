package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightTicketController {
    @Autowired
    private FlightTicketService flightTicketService;

    @GetMapping("/")
    public ResponseEntity<List<FlightTicketGetDto>> getAll() {
        return ResponseEntity.ok(flightTicketService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightTicketGetDto> getById(@PathVariable Long id) throws FlightTicketNotFoundException {
        return ResponseEntity.ok(flightTicketService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlightTicketGetDto>> getAllByUser(@PathVariable Long userId, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(flightTicketService.getAllByUser(sortBy, userId));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<FlightTicketGetDto>> getAllByInvoice(@PathVariable Long invoiceId, @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(flightTicketService.getAllByInvoice(sortBy, invoiceId));
    }

    @PostMapping("/")
    public ResponseEntity<FlightTicketGetDto> create(@Valid @RequestBody FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException {
        return new ResponseEntity<>(flightTicketService.create(flightTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightTicketGetDto> update(@PathVariable Long id, @Valid @RequestBody FlightTicketCreateDto flightTicket) throws FlightTicketNotFoundException, FlightTicketDuplicateException {
        return ResponseEntity.ok(flightTicketService.update(id, flightTicket));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FlightTicketGetDto> updatePartial(@PathVariable Long id, @Valid @RequestBody FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException, FlightTicketDuplicateException {
        return ResponseEntity.ok(flightTicketService.updatePartial(id, flightTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws FlightTicketNotFoundException {
        flightTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
