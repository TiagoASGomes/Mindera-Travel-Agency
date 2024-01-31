package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketUpdateTicketDto;
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

    //TODO ir buscar invoice e fare class ao criar
    //TODO imperdir criar bilhete se invoice estiver paga ou pendente

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
    public List<FlightTicketGetDto> getAll() {
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAll());
    }

    @Override
    public List<FlightTicketGetDto> getAllByInvoice(String sortBy, Long invoiceId) throws InvoiceNotFoundException {
        invoiceService.findById(invoiceId);
        return flightTicketConverter.fromEntityListToGetDtoList(flightTicketRepository.findAllByInvoiceId(invoiceId));
    }

    @Override
    public FlightTicketGetDto getById(Long id) throws FlightTicketNotFoundException {
        return flightTicketConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public FlightTicketGetDto create(FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException, FareClassNotFoundException, InvoiceNotFoundException {
        FareClass fareClass = fareClassService.findByName(flightTicket.fareClass());
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        FlightTicket flightTicketToSave = flightTicketConverter.fromCreateDtoToEntity(flightTicket, fareClass, invoice);
        FlightTicketGetDto flightTicketGetDto = flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToSave));
        invoiceService.updatePrice(invoice.getId());
        return flightTicketGetDto;
    }

    @Override
    public FlightTicketGetDto updateTotal(Long id, FlightTicketCreateDto flightTicket) throws FlightTicketDuplicateException, FareClassNotFoundException, InvoiceNotFoundException, FlightTicketNotFoundException, PaymentCompletedException {
        findById(id);
        Invoice invoice = invoiceService.findById(flightTicket.invoiceId());
        verifyIfInvoicePaid(invoice);
        FareClass fareClass = fareClassService.findByName(flightTicket.fareClass());
        FlightTicket flightTicketToUpdate = flightTicketConverter.fromCreateDtoToEntity(flightTicket, fareClass, invoice);
        flightTicketToUpdate.setId(id);
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(flightTicketToUpdate));
    }

    @Override
    public FlightTicketGetDto updatePartial(Long id, FlightTicketUpdateDto flightTicket) throws FlightTicketNotFoundException, FareClassNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        FlightTicket fTUpdated = findById(id);
        verifyIfInvoicePaid(fTUpdated.getInvoice());
        fTUpdated.setFName(flightTicket.fName());
        fTUpdated.setEmail(flightTicket.email());
        fTUpdated.setPhone(flightTicket.phone());
        fTUpdated.setPrice(flightTicket.price());
        invoiceService.updatePrice(fTUpdated.getInvoice().getId());
        fTUpdated.setFareClass(fareClassService.findByName(flightTicket.fareClass()));
        fTUpdated.setMaxLuggageWeight(flightTicket.maxLuggageWeight());
        fTUpdated.setCarryOnLuggage(flightTicket.carryOnLuggage());
        return flightTicketConverter.fromEntityToGetDto(flightTicketRepository.save(fTUpdated));
    }

    @Override
    public FlightTicketGetDto updateTicketNumber(Long id, FlightTicketUpdateTicketDto flightTicket) throws FlightTicketNotFoundException, PaymentCompletedException, FlightTicketDuplicateException {
        FlightTicket dbTicket = findById(id);
        checkDuplicateTicketNumber(flightTicket.ticketNumber(), id);
        verifyIfInvoicePaid(dbTicket.getInvoice());
        dbTicket.setTicketNumber(flightTicket.ticketNumber());
        dbTicket.setSeatNumber(flightTicket.seatNumber());
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
