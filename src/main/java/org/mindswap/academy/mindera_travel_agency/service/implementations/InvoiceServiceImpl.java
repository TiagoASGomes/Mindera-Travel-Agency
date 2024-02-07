package org.mindswap.academy.mindera_travel_agency.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalBookingInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalReservationInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.user.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceRepository;
import org.mindswap.academy.mindera_travel_agency.repository.RoomInfoRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository inRep;
    private final InvoiceConverter inCon;
    private final PaymentStatusService paymentSer;
    private final UserService userSer;
    private final ExternalService extSer;
    private final RoomInfoRepository roomRep;
    private final FlightTicketRepository flightRep;
    private final RoomInfoConverter roomCon;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository inRep, InvoiceConverter inCon, PaymentStatusService paymentSer, UserService userSer, ExternalService extSer, FlightTicketRepository flightRep, RoomInfoRepository roomRep, RoomInfoConverter roomCon) {
        this.inRep = inRep;
        this.inCon = inCon;
        this.paymentSer = paymentSer;
        this.userSer = userSer;
        this.extSer = extSer;
        this.flightRep = flightRep;
        this.roomRep = roomRep;
        this.roomCon = roomCon;
    }

    @Override
    public Page<InvoiceGetDto> getAll(Pageable page) {
        Page<Invoice> result = inRep.findAll(page);
        return result.map(inCon::fromEntityToGetDto);
    }

    @Override
    @Cacheable(value = "invoices", key = "#id")
    public InvoiceGetDto getById(Long id) throws InvoiceNotFoundException {
        return inCon.fromEntityToGetDto(findById(id));
    }

    @Override
    public InvoiceGetDto create(InvoiceCreateDto invoiceDto) throws UserNotFoundException, PaymentStatusNotFoundException {
        Invoice invoice = inCon.fromCreateDtoToEntity(userSer.findById(invoiceDto.userId()));
        invoice.setPaymentStatus(paymentSer.findByName(NOT_REQUESTED_PAYMENT));
        return inCon.fromEntityToGetDto(inRep.save(invoice));
    }

    @Override
    @CachePut(value = "invoices", key = "#id")
    public InvoiceGetDto update(Long id, InvoiceUpdateDto invoiceDto) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException {
        Invoice invoice = findById(id);
        checkIfCanUpdate(invoice);
        if (invoiceDto.paymentDate() != null) {
            invoice.setPaymentDate(invoiceDto.paymentDate());
        }
        if (invoiceDto.paymentStatus() != null) {
            invoice.setPaymentStatus(paymentSer.findByName(invoiceDto.paymentStatus()));
        }
        return inCon.fromEntityToGetDto(inRep.save(invoice));
    }

    @Override
    @CachePut(value = "invoices", key = "#id")
    public InvoiceGetDto finalizeInvoice(Long id) throws InvoiceNotFoundException, PaymentCompletedException, InvoiceNotCompleteException, UnirestException, JsonProcessingException, PaymentStatusNotFoundException {
        Invoice invoice = findById(id);
        checkIfCanUpdate(invoice);
        HotelReservation hotelReservation = invoice.getHotelReservation();
        Set<FlightTicket> flightTickets = invoice.getFlightTickets();
        if (hotelReservation == null || flightTickets == null || flightTickets.isEmpty()) {
            throw new InvoiceNotCompleteException(INVOICE_NOT_COMPLETE);
        }
        List<ExternalBookingInfoDto> flightInfo = extSer.createFlightTickets(flightTickets);
        ExternalReservationInfoDto reservationInfo = extSer.createReservation(hotelReservation);
        updateFlights(flightInfo, invoice);
        updateHotel(reservationInfo, invoice);
        invoice.setPaymentStatus(paymentSer.findByName(PENDING_PAYMENT));
        return inCon.fromEntityToGetDto(inRep.save(invoice));
    }


    @Override
    @CacheEvict(value = "invoices", key = "#id")
    public void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        Invoice invoice = findById(id);
        if (invoice.getPaymentStatus().getStatusName().equals(PAID_PAYMENT) || invoice.getPaymentStatus().getStatusName().equals(PENDING_PAYMENT)) {
            throw new PaymentCompletedException(CANNOT_DELETE_INVOICE);
        }
        inRep.deleteById(id);
    }

    @Override
    public Invoice findById(Long id) throws InvoiceNotFoundException {
        return inRep.findById(id).orElseThrow(() -> new InvoiceNotFoundException(INVOICE_ID_NOT_FOUND + id));
    }

    @Override
    @CachePut(value = "invoices", key = "#id")
    public void updateHotelPrice(Long id, int hotelPrice) throws InvoiceNotFoundException {
        Invoice invoice = findById(id);
        if (invoice.getFlightTickets() == null || invoice.getFlightTickets().isEmpty()) {
            invoice.setTotalPrice(hotelPrice);
        } else {
            int flightPrice = invoice.getFlightTickets().stream()
                    .mapToInt(FlightTicket::getPrice)
                    .sum();
            invoice.setTotalPrice(hotelPrice + flightPrice);
        }
        inRep.save(invoice);
    }

    @Override
    @CachePut(value = "invoices", key = "#id")
    public void updateFlightPrices(List<FlightTicket> invoiceFlights, Long id) throws InvoiceNotFoundException {
        int price = invoiceFlights.stream()
                .mapToInt(FlightTicket::getPrice)
                .sum();
        Invoice invoice = findById(id);
        if (invoice.getHotelReservation() != null) {
            invoice.setTotalPrice(price + invoice.getHotelReservation().getTotalPrice());
        } else {
            invoice.setTotalPrice(price);
        }
        inRep.save(invoice);
    }

    private void checkIfCanUpdate(Invoice invoice) throws PaymentCompletedException {
        if (invoice.getPaymentStatus().getStatusName().equals(PAID_PAYMENT)) {
            throw new PaymentCompletedException(CANNOT_UPDATE_INVOICE);
        }
    }


    private void updateHotel(ExternalReservationInfoDto reservation, Invoice invoice) {
        HotelReservation hotelReservation = invoice.getHotelReservation();
        Set<RoomInfo> rooms = roomCon.fromExternalDtoListToEntityList(reservation.roomReservations());
        Set<RoomInfo> oldRooms = hotelReservation.getRooms();
        hotelReservation.setRooms(rooms);
        oldRooms.forEach(room -> roomRep.deleteById(room.getId()));
        rooms.forEach(room -> room.setHotelReservation(hotelReservation));
        rooms.forEach(roomRep::save);
    }

    private void updateFlights(List<ExternalBookingInfoDto> flightTickets, Invoice invoice) {
        Set<FlightTicket> tickets = invoice.getFlightTickets();
        tickets.forEach(ticket -> updateTicket(flightTickets, ticket));
    }

    private void updateTicket(List<ExternalBookingInfoDto> flightTickets, FlightTicket ticket) {
        Optional<ExternalBookingInfoDto> externalTicket = flightTickets.stream()
                .filter(flightTicket -> flightTicket.flight().id().equals(ticket.getFlightId()))
                .findFirst();
        if (externalTicket.isPresent()) {
            ticket.setTicketNumber(externalTicket.get().id());
            ticket.setSeatNumber(externalTicket.get().seatNumber());
            flightRep.save(ticket);
        }
    }
}