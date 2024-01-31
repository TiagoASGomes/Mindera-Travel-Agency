package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdatePersonalInfo;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketUpdateTicketDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FareClassService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FlightTicketService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class FlightTicketServiceImpl implements FlightTicketService {

    //TODO Separar patch em personal info e ticket info

    private final FlightTicketConverter flightTicketConverter;
    private final FlightTicketRepository flightTicketRepository;
    private final FareClassService fareClassService;
    private final InvoiceService invoiceService;

    @Autowired
    public FlightTicketServiceImpl(FlightTicketConverter flightTicketConverter, FlightTicketRepository flightTicketRepository, FareClassService fareClassService, InvoiceService invoiceService) {
        this.flightTicketConverter = flightTicketConverter;
        this.flightTicketRepository = flightTicketRepository;
        this.fareClassService = fareClassService;
        this.invoiceService = invoiceService;
    }


    @Override
    public List<TicketGetDto> getAll() {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAll());
    }

    @Override
    public List<TicketGetDto> getAllByInvoice(String sortBy, Long invoiceId) throws InvoiceNotFoundException {
        invoiceService.findById(invoiceId);
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAllByInvoiceId(invoiceId));
    }

    @Override
    public TicketGetDto getById(Long id) throws FlightTicketNotFoundException {
        return flightTicketConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public TicketGetDto create(TicketCreateDto flightTicket) throws FlightTicketDuplicateException, FareClassNotFoundException, InvoiceNotFoundException {
        FareClass fareClass = fareClassService.findByName(flightTicket.fareClass());
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        FlightTicket flightTicketToSave = flightTicketConverter.fromCreateDtoToEntity(flightTicket, fareClass, invoice);
        TicketGetDto ticketGetDto = flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToSave));
        invoiceService.updatePrice(invoice.getId());
        return ticketGetDto;
    }

    @Override
    public TicketGetDto updateTotal(Long id, TicketCreateDto flightTicket) throws FareClassNotFoundException, InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        findById(id);
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        verifyIfInvoicePaid(invoice);
        FareClass fareClass = fareClassService.findByName(flightTicket.fareClass());
        FlightTicket flightTicketToUpdate = flightTicketConverter.fromCreateDtoToEntity(flightTicket, fareClass, invoice);
        flightTicketToUpdate.setId(id);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
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
    public TicketGetDto updateTicketInfo(Long id, TicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, FlightTicketDuplicateException, FareClassNotFoundException, InvoiceNotFoundException {
        FlightTicket dbTicket = findById(id);
        checkDuplicateTicketNumber(flightTicket.ticketNumber(), id);
        verifyIfInvoicePaid(dbTicket.getInvoice());
        dbTicket.setTicketNumber(flightTicket.ticketNumber());
        dbTicket.setSeatNumber(flightTicket.seatNumber());
        dbTicket.setPrice(flightTicket.price());
        invoiceService.updatePrice(dbTicket.getInvoice().getId());
        dbTicket.setFareClass(fareClassService.findByName(flightTicket.fareClass()));
        dbTicket.setMaxLuggageWeight(flightTicket.maxLuggageWeight());
        dbTicket.setCarryOnLuggage(flightTicket.carryOnLuggage());
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(dbTicket));
    }

    @Override
    public void delete(Long id) throws FlightTicketNotFoundException {
        findById(id);
        flightTicketRepository.deleteById(id);
    }

    public FlightTicket findById(Long id) throws FlightTicketNotFoundException {
        return flightTicketRepository.findById(id).orElseThrow(() -> new FlightTicketNotFoundException(ID_NOT_FOUND + id));
    }


    private void checkDuplicateTicketNumber(Long ticketNumber, Long id) throws FlightTicketDuplicateException {
        Optional<FlightTicket> flightTicket = flightTicketRepository.findByTicketNumber(ticketNumber);
        if (flightTicket.isPresent() && !flightTicket.get().getId().equals(id)) {
            throw new FlightTicketDuplicateException(DUPLICATE_FLIGHT_TICKET_NUMBER);
        }
    }

    private void verifyIfInvoicePaid(Invoice invoice) throws PaymentCompletedException {
        String status = invoice.getPaymentStatus().getStatusName();
        if (status.equals(PENDING_PAYMENT) || status.equals(PAID_PAYMENT)) {
            throw new PaymentCompletedException(CANNOT_ALTER_PLANE_TICKET);
        }
    }
}
