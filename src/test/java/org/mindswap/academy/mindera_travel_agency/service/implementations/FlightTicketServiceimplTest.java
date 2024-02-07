package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.controller.FlightTicketController;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightTicketServiceimplTest {
    private FlightTicketController flightTicketController;
    private FlightTicketService flightTicketService;
    private FlightTicketRepository flightTicketRepository;
    private InvoiceService invoiceService;


    @BeforeEach
    public void setup() {
        flightTicketService = Mockito.mock(FlightTicketService.class);
        flightTicketController = new FlightTicketController(flightTicketService);
    }

    @Test
    void whenGetAllTickets_thenReturnsOkStatusAndCorrectResponseBody() {

        TicketGetDto ticket1 = new TicketGetDto(1L, "John", "john@example.com", "1234567890", 100L, "1A", 100, 20, true);
        TicketGetDto ticket2 = new TicketGetDto(2L, "Jana", "jane@example.com", "0987654321", 200L, "2B", 200, 30, false);
        List<TicketGetDto> tickets = Arrays.asList(ticket1, ticket2);

        when(flightTicketService.getAll()).thenReturn(tickets);

        ResponseEntity<List<TicketGetDto>> response = flightTicketController.getAll();
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(tickets, response.getBody());
    }

    @Test
    public void testGetAll_NoTickets() {

        when(flightTicketService.getAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TicketGetDto>> response = flightTicketController.getAll();
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    public void whenGetTicketById_thenReturnsOkStatusAndCorrectResponseBody() throws FlightTicketNotFoundException {

        TicketGetDto ticket = new TicketGetDto(1L, "John", "john@example.com", "1234567890", 100L, "1A", 100, 20, true);

        when(flightTicketService.getById(1L)).thenReturn(ticket);

        ResponseEntity<TicketGetDto> response = flightTicketController.getById(1L);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(ticket, response.getBody());
    }

    @Test
    public void whenGetById_thenFindByIdIsCalledOnRepository() throws FlightTicketNotFoundException {
        Long id = 1L;
        flightTicketService.getById(id);
        verify(flightTicketRepository).findById(id);
    }

    @Test
    public void whenGetAllTicketsByInvoice_thenReturnsOkStatusAndCorrectResponseBody() throws InvoiceNotFoundException {

        TicketGetDto ticket1 = new TicketGetDto(1L, "John", "john@example.com", "1234567890", 100L, "1A", 100, 20, true);
        TicketGetDto ticket2 = new TicketGetDto(2L, "Jane", "jane@example.com", "0987654321", 200L, "2B", 200, 30, false);
        List<TicketGetDto> tickets = Arrays.asList(ticket1, ticket2);

        when(flightTicketService.getAllByInvoice(1L)).thenReturn(tickets);

        ResponseEntity<List<TicketGetDto>> response = flightTicketController.getAllByInvoice(1L);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(tickets, response.getBody());
    }

    @Test
    public void whenGetAllByInvoice_thenFindByIdAndFindAllByInvoiceIdAreCalled() throws InvoiceNotFoundException {
        Long invoiceId = 1L;
        flightTicketService.getAllByInvoice(1L);
        verify(invoiceService).findById(invoiceId);
        verify(flightTicketRepository).findAllByInvoiceId(invoiceId);
    }

    @Test
    public void whenUpdateTotal_thenFindByIdAndSaveAreCalled() throws FlightTicketDuplicateException, InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        Long id = 1L;
        TicketCreateDto flightTicket = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);
        flightTicketService.updateTotal(id, flightTicket);
        verify(flightTicketRepository).findById(id);
        verify(flightTicketRepository).save(any(FlightTicket.class));
    }

    @Test
    public void whenCreateTicket_thenReturnsCreatedStatusAndCorrectResponseBody() throws FlightTicketDuplicateException, InvoiceNotFoundException {

        TicketCreateDto createDto = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);
        TicketGetDto getDto = new TicketGetDto(1L, "John", "john@example.com", "1234567890", 100L, "1A", 100, 20, true);

        when(flightTicketService.create(createDto)).thenReturn(getDto);

        ResponseEntity<TicketGetDto> response = flightTicketController.create(createDto);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals(getDto, response.getBody());
    }

    @Test
    public void whenCreateDuplicateTicket_thenThrowsBadRequestException() throws FlightTicketDuplicateException, InvoiceNotFoundException {

        TicketCreateDto createDto = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);

        when(flightTicketService.create(createDto)).thenThrow(new FlightTicketDuplicateException("Duplicate ticket"));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            flightTicketController.create(createDto);
        });
        assertEquals("400 BAD_REQUEST \"Duplicate ticket\"", exception.getMessage());
    }

    @Test
    public void whenCreateWithDuplicateTicket_thenThrowFlightTicketDuplicateException() throws FlightTicketDuplicateException, InvoiceNotFoundException {
        TicketCreateDto flightTicket = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);
        when(flightTicketRepository.save(any(FlightTicket.class))).thenThrow(new FlightTicketDuplicateException("Duplicate ticket"));
        assertThrows(FlightTicketDuplicateException.class, () -> flightTicketService.create(flightTicket));
    }

    @Test
    public void whenUpdateTotalWithValidInput_thenFindByIdAndSaveAreCalled() throws FlightTicketDuplicateException, InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        Long id = 1L;
        TicketCreateDto flightTicket = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);
        flightTicketService.updateTotal(id, flightTicket);
        verify(flightTicketRepository).findById(id);
        verify(flightTicketRepository).save(any(FlightTicket.class));
    }

    @Test
    public void whenUpdateTotalWithInvalidId_thenThrowFlightTicketNotFoundException() throws FlightTicketDuplicateException, InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        Long id = 1L;
        TicketCreateDto flightTicket = new TicketCreateDto("John", "john@exemple.com", "1234567890", "1A", 100, 20, true, 1L);
        when(flightTicketRepository.findById(id)).thenThrow(new FlightTicketNotFoundException("Ticket not found"));
        assertThrows(FlightTicketNotFoundException.class, () -> flightTicketService.updateTotal(id, flightTicket));
    }

    @Test
    public void whenDeleteWithValidId_thenNoExceptionThrown() throws FlightTicketNotFoundException {
        Long id = 1L;
        FlightTicket flightTicket = new FlightTicket();
        when(flightTicketRepository.findById(id)).thenReturn(Optional.of(flightTicket));

        flightTicketService.delete(id);

        verify(flightTicketRepository, times(1)).delete(flightTicket);
    }

    @Test
    public void whenDeleteWithInvalidId_thenThrowFlightTicketNotFoundException() throws FlightTicketNotFoundException {
        Long id = 1L;
        when(flightTicketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlightTicketNotFoundException.class, () -> flightTicketService.delete(id));
    }

    @Test
    public void whenDeleteWithNullId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> flightTicketService.delete(null));
    }

    @Test
    public void whenDeleteWithNonExistingId_thenThrowFlightTicketNotFoundException() {
        Long id = 1L;
        when(flightTicketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlightTicketNotFoundException.class, () -> flightTicketService.delete(id));
    }

    @Test
    public void whenDeleteWithExistingId_thenCallDeleteMethodOfRepository() throws FlightTicketNotFoundException {
        Long id = 1L;
        FlightTicket flightTicket = new FlightTicket();
        when(flightTicketRepository.findById(id)).thenReturn(Optional.of(flightTicket));

        flightTicketService.delete(id);

        verify(flightTicketRepository, times(1)).delete(flightTicket);
    }

    @Test
    public void whenDeleteWithExistingId_thenNoExceptionThrown() throws FlightTicketNotFoundException {
        Long id = 1L;
        FlightTicket flightTicket = new FlightTicket();
        when(flightTicketRepository.findById(id)).thenReturn(Optional.of(flightTicket));

        assertDoesNotThrow(() -> flightTicketService.delete(id));
    }

    @Test
    public void whenDeleteWithExistingId_thenRepositoryDeleteIsCalled() throws FlightTicketNotFoundException {
        Long id = 1L;
        FlightTicket flightTicket = new FlightTicket();
        when(flightTicketRepository.findById(id)).thenReturn(Optional.of(flightTicket));

        flightTicketService.delete(id);

        verify(flightTicketRepository, times(1)).delete(any(FlightTicket.class));
    }

    @Test
    public void whenDeleteWithExistingId_thenDoesNotThrowFlightTicketDuplicateException() throws FlightTicketNotFoundException {
        Long id = 1L;
        FlightTicket flightTicket = new FlightTicket();
        when(flightTicketRepository.findById(id)).thenReturn(Optional.of(flightTicket));

        assertDoesNotThrow(() -> flightTicketService.delete(id));
    }


}






