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

/**
 * This interface represents an external service that provides functionality related to hotels and flights.
 */
public interface ExternalService {
    /**
     * Retrieves a list of available hotels based on the specified location, arrival date, and page number.
     *
     * @param location    the location of the hotels
     * @param arrivalDate the arrival date
     * @param pageNumber  the page number for pagination
     * @return a list of available hotels
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    List<ExternalHotelInfoDto> getAvailableHotels(String location, String arrivalDate, int pageNumber) throws UnirestException, JsonProcessingException;

    /**
     * Creates a reservation for the specified hotel reservation.
     *
     * @param hotelReservation the hotel reservation to create
     * @return the created reservation information
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    ExternalReservationInfoDto createReservation(HotelReservation hotelReservation) throws UnirestException, JsonProcessingException;

    /**
     * Creates flight tickets for the specified set of flight tickets.
     *
     * @param flightTickets the set of flight tickets to create
     * @return a list of created booking information for the flight tickets
     * @throws UnirestException         if an error occurs during the HTTP request
     * @throws JsonProcessingException  if an error occurs while processing JSON
     * @throws InvoiceNotFoundException if an invoice is not found for the flight tickets
     */
    List<ExternalBookingInfoDto> createFlightTickets(Set<FlightTicket> flightTickets) throws UnirestException, JsonProcessingException, InvoiceNotFoundException;

    /**
     * Retrieves a list of flights based on the specified source, destination, arrival date, and page number.
     *
     * @param source      the source of the flights
     * @param destination the destination of the flights
     * @param date        the arrival date
     * @param page        the page number for pagination
     * @return a list of flights
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    List<ExternalFlightInfoDto> getFlights(String source, String destination, String date, int page, int price) throws UnirestException, JsonProcessingException;
}
