package org.mindswap.academy.mindera_travel_agency.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.springframework.stereotype.Service;

@Service
public class ExternalServiceImpl implements ExternalService {
    ObjectMapper objectMapper = new ObjectMapper();


    public String getAvailableHotels() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://localhost:9000/api/v1/hotel")
                .header("Content-Type", "application/json")
                .asString();
        if (response.getStatus() == 200) {
            return response.getBody();
        }
        return "no hotels available";
    }

    @Override
    public String createReservation(ExternalReservationCreateDto externalReservationCreateDto) throws UnirestException, JsonProcessingException {
        String body = objectMapper.writeValueAsString(externalReservationCreateDto);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:9000/api/v1/reservations/Sample Hotel/SINGLEROOM")
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        if (response.getStatus() == 200) {
            return response.getBody();
        }
        throw new UnirestException("Error creating reservation");
    }
}
