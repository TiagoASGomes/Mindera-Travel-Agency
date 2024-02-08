package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class InvalidCheckInOutDateException extends TravelAgencyException {
    public InvalidCheckInOutDateException(String message) {
        super(message);
    }
}
