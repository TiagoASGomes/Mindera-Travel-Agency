package org.mindswap.academy.mindera_travel_agency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdatePersonalInfo;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdateTicketDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.MaxFlightPerInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The FlightTicketController class handles the RESTful API endpoints for flight tickets.
 */
@RestController
@RequestMapping("/api/v1/flights")
public class FlightTicketController {

    private final FlightTicketService flightTicketService;

    @Autowired
    public FlightTicketController(FlightTicketService flightTicketService) {
        this.flightTicketService = flightTicketService;
    }

    /**
     * Retrieves all flight tickets with pagination.
     *
     * @param page the pageable object for pagination
     * @return the ResponseEntity containing the page of TicketGetDto objects
     */
    @Operation(summary = "Get all flight tickets")
    @ApiResponse(responseCode = "200", description = "Return a page of flight tickets")
    @GetMapping("/")
    public ResponseEntity<Page<TicketGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(flightTicketService.getAll(page));
    }

    /**
     * Retrieves all flight tickets associated with a specific invoice.
     *
     * @param invoiceId the ID of the invoice
     * @return the ResponseEntity containing the list of TicketGetDto objects
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    @Operation(summary = "Get all flight tickets by invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of flight tickets"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<TicketGetDto>> getAllByInvoice(@PathVariable Long invoiceId) throws InvoiceNotFoundException {
        return ResponseEntity.ok(flightTicketService.getAllByInvoice(invoiceId));
    }

    /**
     * Retrieves a flight ticket by its ID.
     *
     * @param id the ID of the flight ticket
     * @return the ResponseEntity containing the TicketGetDto object
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     */
    @Operation(summary = "Get flight ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a flight ticket"),
            @ApiResponse(responseCode = "404", description = "Flight ticket not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketGetDto> getById(@PathVariable Long id) throws FlightTicketNotFoundException {
        return ResponseEntity.ok(flightTicketService.getById(id));
    }

    /**
     * Creates a new flight ticket.
     *
     * @param flightTicket the TicketCreateDto object containing the flight ticket details
     * @return the ResponseEntity containing the created TicketGetDto object
     * @throws InvoiceNotFoundException     if the invoice is not found
     * @throws MaxFlightPerInvoiceException if the maximum number of flights per invoice is reached
     * @throws PaymentCompletedException    if the payment for the invoice is already completed
     */
    @Operation(summary = "Create a new flight ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the created flight ticket"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "400", description = "Max number of flights per invoice reached, or payment for the invoice is already completed"),
    })
    @PostMapping("/")
    public ResponseEntity<TicketGetDto> create(@Valid @RequestBody TicketCreateDto flightTicket) throws InvoiceNotFoundException, MaxFlightPerInvoiceException, PaymentCompletedException {
        return new ResponseEntity<>(flightTicketService.create(flightTicket), HttpStatus.CREATED);
    }

    /**
     * Updates the total price of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the TicketCreateDto object containing the updated flight ticket details
     * @return the ResponseEntity containing the updated TicketGetDto object
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws InvoiceNotFoundException      if the invoice is not found
     * @throws PaymentCompletedException     if the payment for the invoice is already completed
     */
    @Operation(summary = "Update a flight ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated flight ticket"),
            @ApiResponse(responseCode = "404", description = "Flight ticket not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Payment for the invoice is already completed")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketGetDto> updateTotal(@PathVariable Long id, @Valid @RequestBody TicketCreateDto flightTicket) throws FlightTicketNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(flightTicketService.updateTotal(id, flightTicket));
    }

    /**
     * Updates the personal information of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the TicketUpdatePersonalInfo object containing the updated personal information
     * @return the ResponseEntity containing the updated TicketGetDto object
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the payment for the invoice is already completed
     */
    @Operation(summary = "Update personal information of a flight ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated flight ticket"),
            @ApiResponse(responseCode = "404", description = "Flight ticket not found"),
            @ApiResponse(responseCode = "400", description = "Payment for the invoice is already completed")
    })
    @PatchMapping("/{id}/personal_info")
    public ResponseEntity<TicketGetDto> updatePersonalInfo(@PathVariable Long id, @Valid @RequestBody TicketUpdatePersonalInfo flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(flightTicketService.updatePersonalInfo(id, flightTicket));
    }

    /**
     * Updates the ticket information of a flight ticket.
     *
     * @param id           the ID of the flight ticket
     * @param flightTicket the TicketUpdateTicketDto object containing the updated ticket information
     * @return the ResponseEntity containing the updated TicketGetDto object
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the payment for the invoice is already completed
     * @throws InvoiceNotFoundException      if the invoice is not found
     */
    @Operation(summary = "Update ticket information of a flight ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated flight ticket"),
            @ApiResponse(responseCode = "404", description = "Flight ticket not found, or invoice not found"),
            @ApiResponse(responseCode = "400", description = "Payment for the invoice is already completed")
    })
    @PatchMapping("/{id}/ticket_info")
    public ResponseEntity<TicketGetDto> updateTicketNumber(@PathVariable Long id, @Valid @RequestBody TicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        return ResponseEntity.ok(flightTicketService.updateTicketInfo(id, flightTicket));
    }

    /**
     * Deletes a flight ticket by its ID.
     *
     * @param id the ID of the flight ticket
     * @return the ResponseEntity with no content
     * @throws FlightTicketNotFoundException if the flight ticket is not found
     * @throws PaymentCompletedException     if the payment for the invoice is already completed
     */
    @Operation(summary = "Delete a flight ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight ticket deleted"),
            @ApiResponse(responseCode = "404", description = "Flight ticket not found"),
            @ApiResponse(responseCode = "400", description = "Payment for the invoice is already completed")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws FlightTicketNotFoundException, PaymentCompletedException {
        flightTicketService.delete(id);
        return ResponseEntity.ok().build();
    }
}
