package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceConverter {

    private final HotelReservationConverter hotelReservationConverter;
    private final FlightTicketConverter flightTicketConverter;

    @Autowired
    public InvoiceConverter(HotelReservationConverter hotelReservationConverter, FlightTicketConverter flightTicketConverter) {
        this.hotelReservationConverter = hotelReservationConverter;
        this.flightTicketConverter = flightTicketConverter;
    }


    public List<InvoiceGetDto> fromEntityListToGetDtoList(List<Invoice> invoices) {
        if (invoices == null) return new ArrayList<>();
        return invoices.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

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

    public Invoice fromCreateDtoToEntity(User user) {
        return new Invoice(user);
    }
}
