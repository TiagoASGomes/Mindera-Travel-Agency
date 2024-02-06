package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalBookingInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalReservationInfoDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;

import java.util.List;
import java.util.Set;

public interface ExternalService {
    List<ExternalHotelInfoDto> getAvailableHotels(int pageNumber) throws UnirestException, JsonProcessingException;

    ExternalReservationInfoDto createReservation(HotelReservation hotelReservation) throws UnirestException, JsonProcessingException;

    List<ExternalBookingInfoDto> createFlightTickets(Set<FlightTicket> flightTickets) throws UnirestException, JsonProcessingException, InvoiceNotFoundException;

    List<ExternalFlightInfoDto> getFlights(int page) throws UnirestException, JsonProcessingException;
}
