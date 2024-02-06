package org.mindswap.academy.mindera_travel_agency.dto.external.hotel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalReservationInfoDto(

        LocalDate arrival,

        LocalDate departure,

        String hotelN,

        String firstName,

        String lastName,

        String phoneNumber,

        String vat,

        List<ExternalRoomInfoDto> roomReservations
) {
}
