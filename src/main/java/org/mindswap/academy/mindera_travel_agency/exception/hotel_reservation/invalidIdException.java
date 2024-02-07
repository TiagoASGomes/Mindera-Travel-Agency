package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class invalidIdException extends TravelAgencyException {
    public invalidIdException(String message) {
        super(message);
    }
}
