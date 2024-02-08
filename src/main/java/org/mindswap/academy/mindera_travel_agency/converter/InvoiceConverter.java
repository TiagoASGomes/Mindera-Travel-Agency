package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The InvoiceConverter class is responsible for converting Invoice objects to DTOs (Data Transfer Objects) and vice versa.
 * It provides methods to convert from Entity objects to GetDto objects, and from CreateDto objects to Entity objects.
 */
@Component
public class InvoiceConverter {

    private final HotelReservationConverter hotelReservationConverter;
    private final FlightTicketConverter flightTicketConverter;

    @Autowired
    public InvoiceConverter(HotelReservationConverter hotelReservationConverter, FlightTicketConverter flightTicketConverter) {
        this.hotelReservationConverter = hotelReservationConverter;
        this.flightTicketConverter = flightTicketConverter;
    }

    /**
     * Converts a collection of Invoice entities to a list of InvoiceGetDto objects.
     * If the input collection is null, an empty list is returned.
     *
     * @param invoices the collection of Invoice entities to convert
     * @return a list of InvoiceGetDto objects
     */
    public List<InvoiceGetDto> fromEntityListToGetDtoList(Collection<Invoice> invoices) {
        if (invoices == null) return new ArrayList<>();
        return invoices.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    /**
     * Converts an Invoice entity to an InvoiceGetDto object.
     *
     * @param invoice the Invoice entity to convert
     * @return an InvoiceGetDto object
     */
    public InvoiceGetDto fromEntityToGetDto(Invoice invoice) {
        return new InvoiceGetDto(
                invoice.getId(),
                invoice.getTotalPrice(),
                invoice.getPaymentDate(),
                invoice.getPaymentStatus().getStatusName(),
                hotelReservationConverter.fromEntityToGetDto(invoice.getHotelReservation()),
                flightTicketConverter.fromEntityListToGetDtoList(invoice.getFlightTickets())
        );
    }

    /**
     * Converts a CreateDto object to an Invoice entity.
     *
     * @param user the User object associated with the Invoice
     * @return an Invoice entity
     */
    public Invoice fromCreateDtoToEntity(User user) {
        return new Invoice(user);
    }
}
