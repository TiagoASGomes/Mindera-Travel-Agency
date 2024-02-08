package org.mindswap.academy.mindera_travel_agency.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalBookingInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalReservationInfoDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ExternalServiceImpl implements ExternalService {
    private final String hotelApiUrl;
    private final String flightApiUrl;
    private final HotelReservationConverter hRConverter;
    private final FlightTicketConverter fTConverter;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs an instance of ExternalServiceImpl with the specified converters.
     *
     * @param hRConverter the HotelReservationConverter to be used for conversion
     * @param fTConverter the FlightTicketConverter to be used for conversion
     */
    public ExternalServiceImpl(HotelReservationConverter hRConverter, FlightTicketConverter fTConverter, @Value("${hotel.api.base-url}") String hotelApiUrl, @Value("${flight.api.base-url}") String flightApiUrl) {
        this.hRConverter = hRConverter;
        this.fTConverter = fTConverter;
        this.hotelApiUrl = hotelApiUrl;
        this.flightApiUrl = flightApiUrl;
    }

    /**
     * Retrieves a list of available hotels based on the specified location, arrival date, and page number.
     *
     * @param location    the location of the hotels
     * @param arrivalDate the arrival date
     * @param pageNumber  the page number for pagination
     * @return a list of ExternalHotelInfoDto objects representing the available hotels
     * @throws UnirestException        if an error occurs while making the HTTP request
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    public List<ExternalHotelInfoDto> getAvailableHotels(String location, String arrivalDate, int pageNumber) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        //TODO add location filters and such
        HttpResponse<String> response = Unirest.get(hotelApiUrl + "/api/v1/hotel?page=" + pageNumber)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .asString();
        if (response.getStatus() == 200) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalHotelInfoDto>>() {
            });
        }
        throw new UnirestException("Error getting hotels");
    }

    /**
     * Creates a reservation for the specified hotel reservation.
     *
     * @param hotelReservation the HotelReservation object representing the reservation to be created
     * @return an ExternalReservationInfoDto object representing the created reservation
     * @throws UnirestException        if an error occurs while making the HTTP request
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Override
    public ExternalReservationInfoDto createReservation(HotelReservation hotelReservation) throws UnirestException, JsonProcessingException {
        ExternalReservationCreateDto externalReservationCreateDto = hRConverter.fromEntityToExternalDto(hotelReservation);
        objectMapper.registerModule(new JavaTimeModule());
        String body = objectMapper.writeValueAsString(externalReservationCreateDto);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post(hotelApiUrl + "/api/v1/reservations/" + hotelReservation.getHotelName().replace(" ", "-"))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .asString();
        if (response.getStatus() == 201) {
            return objectMapper.readValue(response.getBody(), ExternalReservationInfoDto.class);
        }
        throw new UnirestException("Error creating reservation");
    }

    /**
     * Creates flight tickets for the specified set of flight tickets.
     *
     * @param flightTickets the set of FlightTicket objects representing the flight tickets to be created
     * @return a list of ExternalBookingInfoDto objects representing the created flight tickets
     * @throws UnirestException         if an error occurs while making the HTTP request
     * @throws JsonProcessingException  if an error occurs while processing the JSON response
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    @Override
    public List<ExternalBookingInfoDto> createFlightTickets(Set<FlightTicket> flightTickets) throws UnirestException, JsonProcessingException, InvoiceNotFoundException {
        List<ExternalFlightCreateDto> flights = fTConverter.fromEntityListToExternalDtoList(flightTickets);
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(flights);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post(flightApiUrl + "/api/v1/bookings")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .asString();
        if (response.getStatus() == 201) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalBookingInfoDto>>() {
            });
        }
        throw new InvoiceNotFoundException("Error creating flight tickets");
    }

    /**
     * Retrieves a list of flights based on the specified source, destination, arrival date, and page number.
     *
     * @param source      the source of the flights
     * @param destination the destination of the flights
     * @param date        the arrival date
     * @param price       the price of the flights
     * @param page        the page number for pagination
     * @return a list of ExternalFlightInfoDto objects representing the flights
     * @throws UnirestException        if an error occurs while making the HTTP request
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Override
    public List<ExternalFlightInfoDto> getFlights(String source, String destination, String date, int page, int price) throws UnirestException, JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        date = date.replace(":", "%3A");
        Unirest.setTimeouts(0, 0);
        String url = flightApiUrl + "/api/v1/flights/search/" + source + "/" + destination + "?date=" + date + "&page=" + page + "&price=" + price;
        HttpResponse<String> response = Unirest.get(url)
                .header("Content-Type", "application/json")
                .asString();
        if (response.getStatus() == 200) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalFlightInfoDto>>() {
            });
        }
        throw new UnirestException("Error getting flights");
    }
}
