package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdatePersonalInfo;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdateTicketDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightTicketController {

    private final FlightTicketService flightTicketService;

    @Autowired
    public FlightTicketController(FlightTicketService flightTicketService) {
        this.flightTicketService = flightTicketService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TicketGetDto>> getAll() {
        return ResponseEntity.ok(flightTicketService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketGetDto> getById(@PathVariable Long id) throws FlightTicketNotFoundException {
        return ResponseEntity.ok(flightTicketService.getById(id));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<TicketGetDto>> getAllByInvoice(@PathVariable Long invoiceId, @RequestParam(required = false) String sortBy) throws InvoiceNotFoundException {
        return ResponseEntity.ok(flightTicketService.getAllByInvoice(sortBy, invoiceId));
    }

    @PostMapping("/")
    public ResponseEntity<TicketGetDto> create(@Valid @RequestBody TicketCreateDto flightTicket) throws FlightTicketDuplicateException, FareClassNotFoundException, InvoiceNotFoundException {
        return new ResponseEntity<>(flightTicketService.create(flightTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketGetDto> updateTotal(@PathVariable Long id, @Valid @RequestBody TicketCreateDto flightTicket) throws FlightTicketNotFoundException, FareClassNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(flightTicketService.updateTotal(id, flightTicket));
    }

    @PatchMapping("/{id}/personal_info")
    public ResponseEntity<TicketGetDto> updatePersonalInfo(@PathVariable Long id, @Valid @RequestBody TicketUpdatePersonalInfo flightTicket) throws FlightTicketNotFoundException, FlightTicketDuplicateException, FareClassNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        return ResponseEntity.ok(flightTicketService.updatePersonalInfo(id, flightTicket));
    }

    @PatchMapping("/{id}/ticket_info")
    public ResponseEntity<TicketGetDto> updateTicketNumber(@PathVariable Long id, @Valid @RequestBody TicketUpdateTicketDto flightTicket) throws FlightTicketDuplicateException, FlightTicketNotFoundException, PaymentCompletedException, FareClassNotFoundException, InvoiceNotFoundException {
        return ResponseEntity.ok(flightTicketService.updateTicketInfo(id, flightTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws FlightTicketNotFoundException {
        flightTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
