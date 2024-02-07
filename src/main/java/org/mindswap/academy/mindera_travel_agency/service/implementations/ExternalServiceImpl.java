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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ExternalServiceImpl implements ExternalService {
    ObjectMapper objectMapper = new ObjectMapper();

    private HotelReservationConverter hRConverter;
    private FlightTicketConverter fTConverter;

    public ExternalServiceImpl(HotelReservationConverter hRConverter, FlightTicketConverter fTConverter) {
        this.hRConverter = hRConverter;
        this.fTConverter = fTConverter;
    }

    public List<ExternalHotelInfoDto> getAvailableHotels(int page) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://localhost:9000/api/v1/hotel?page=" + page)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .asString();
        if (response.getStatus() == 200) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalHotelInfoDto>>() {
            });
        }
        throw new UnirestException("Error getting hotels");
    }

    @Override
    public ExternalReservationInfoDto createReservation(HotelReservation hotelReservation) throws UnirestException, JsonProcessingException {
        ExternalReservationCreateDto externalReservationCreateDto = hRConverter.fromEntityToExternalDto(hotelReservation);
        objectMapper.registerModule(new JavaTimeModule());
        String body = objectMapper.writeValueAsString(externalReservationCreateDto);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:9000/api/v1/reservations/" + hotelReservation.getHotelName())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .asString();
        if (response.getStatus() == 201) {
            return objectMapper.readValue(response.getBody(), ExternalReservationInfoDto.class);
        }
        throw new UnirestException("Error creating reservation");
    }

    @Override
    public List<ExternalBookingInfoDto> createFlightTickets(Set<FlightTicket> flightTickets) throws UnirestException, JsonProcessingException, InvoiceNotFoundException {
        List<ExternalFlightCreateDto> flights = fTConverter.fromEntityListToExternalDtoList(flightTickets);
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(flights);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8081/api/v1/bookings")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .asString();
        if (response.getStatus() == 201) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalBookingInfoDto>>() {
            });
        }
        throw new InvoiceNotFoundException("Error creating flight tickets");
    }

    @Override
    public List<ExternalFlightInfoDto> getFlights(int page) throws UnirestException, JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://localhost:8081/api/v1/flights?page=" + page)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .asString();
        if (response.getStatus() == 200) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<ExternalFlightInfoDto>>() {
            });
        }
        throw new UnirestException("Error getting flights");
    }
}
