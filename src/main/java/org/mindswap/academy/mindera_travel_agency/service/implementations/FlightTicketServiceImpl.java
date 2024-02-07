package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdatePersonalInfo;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdateTicketDto;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.MaxFlightPerInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class FlightTicketServiceImpl implements FlightTicketService {
    private static final int MAX_FLIGHTS_PER_INVOICE = 50;
    private final FlightTicketConverter flightTicketConverter;
    private final FlightTicketRepository flightTicketRepository;
    private final InvoiceService invoiceService;

    @Autowired
    public FlightTicketServiceImpl(FlightTicketConverter flightTicketConverter, FlightTicketRepository flightTicketRepository, InvoiceService invoiceService) {
        this.flightTicketConverter = flightTicketConverter;
        this.flightTicketRepository = flightTicketRepository;
        this.invoiceService = invoiceService;
    }


    @Override
    public Page<TicketGetDto> getAll(Pageable page) {
        Page<FlightTicket> flightTickets = flightTicketRepository.findAll(page);
        return flightTickets.map(flightTicketConverter::fromEntityToGetDto);
    }

    @Override
    public List<TicketGetDto> getAllByInvoice(Long invoiceId) throws InvoiceNotFoundException {
        invoiceService.findById(invoiceId);
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAllByInvoiceId(invoiceId));
    }

    @Override
    public TicketGetDto getById(Long id) throws FlightTicketNotFoundException {
        return flightTicketConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public TicketGetDto create(TicketCreateDto flightTicket) throws InvoiceNotFoundException, MaxFlightPerInvoiceException, PaymentCompletedException {
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        verifyIfInvoicePaid(invoice);
        checkIfInvoiceAtFlightLimit(invoice);
        FlightTicket flightTicketToSave = flightTicketConverter.fromCreateDtoToEntity(flightTicket, invoice);
        TicketGetDto ticketGetDto = flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToSave));
        invoiceService.updatePrice(invoice.getId());
        return ticketGetDto;
    }

    @Override
    public TicketGetDto updateTotal(Long id, TicketCreateDto flightTicket) throws InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        findById(id);
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        verifyIfInvoicePaid(invoice);
        FlightTicket flightTicketToUpdate = flightTicketConverter.fromCreateDtoToEntity(flightTicket, invoice);
        flightTicketToUpdate.setId(id);
        TicketGetDto ticketGetDto = flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
        invoiceService.updatePrice(invoice.getId());
        return ticketGetDto;
    }

    @Override
    public TicketGetDto updatePersonalInfo(Long id, TicketUpdatePersonalInfo flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException {
        FlightTicket fTUpdated = findById(id);
        verifyIfInvoicePaid(fTUpdated.getInvoice());
        fTUpdated.setFName(flightTicket.fName());
        fTUpdated.setEmail(flightTicket.email());
        fTUpdated.setPhone(flightTicket.phone());
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(fTUpdated));
    }

    @Override
    public TicketGetDto updateTicketInfo(Long id, TicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        FlightTicket dbTicket = findById(id);
        verifyIfInvoicePaid(dbTicket.getInvoice());
        dbTicket.setPrice(flightTicket.price());
        dbTicket.setFareClass(flightTicket.fareClass());
        dbTicket.setMaxLuggageWeight(flightTicket.maxLuggageWeight());
        dbTicket.setCarryOnLuggage(flightTicket.carryOnLuggage());
        TicketGetDto ticketGetDto = flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(dbTicket));
        invoiceService.updatePrice(dbTicket.getInvoice().getId());
        return ticketGetDto;
    }

    @Override
    public void delete(Long id) throws FlightTicketNotFoundException, PaymentCompletedException {
        FlightTicket flightTicket = findById(id);
        verifyIfInvoicePaid(flightTicket.getInvoice());
        flightTicketRepository.deleteById(id);
    }

    @Override
    public FlightTicket findById(Long id) throws FlightTicketNotFoundException {
        return flightTicketRepository.findById(id).orElseThrow(() -> new FlightTicketNotFoundException(FLIGHT_TICKET_ID_NOT_FOUND + id));
    }

    private void verifyIfInvoicePaid(Invoice invoice) throws PaymentCompletedException {
        String status = invoice.getPaymentStatus().getStatusName();
        if (status.equals(PENDING_PAYMENT) || status.equals(PAID_PAYMENT)) {
            throw new PaymentCompletedException(CANNOT_ALTER_PLANE_TICKET);
        }
    }

    private void checkIfInvoiceAtFlightLimit(Invoice invoice) throws MaxFlightPerInvoiceException {
        if (invoice.getFlightTickets().size() >= MAX_FLIGHTS_PER_INVOICE) {
            throw new MaxFlightPerInvoiceException(INVOICE_FLIGHT_LIMIT_REACHED);
        }
    }
}
