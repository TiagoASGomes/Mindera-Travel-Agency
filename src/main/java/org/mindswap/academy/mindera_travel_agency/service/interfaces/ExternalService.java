package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalReservationCreateDto;

public interface ExternalService {
    String getAvailableHotels() throws UnirestException;

    String createReservation(ExternalReservationCreateDto externalReservationCreateDto) throws UnirestException, JsonProcessingException;
}
