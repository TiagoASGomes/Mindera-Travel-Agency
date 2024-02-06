package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class InvalidDateException extends TravelAgencyException {
    public InvalidDateException(String message) {
        super(message);
    }
}
