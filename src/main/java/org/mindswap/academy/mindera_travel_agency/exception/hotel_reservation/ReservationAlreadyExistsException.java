package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class ReservationAlreadyExistsException extends TravelAgencyException {
    public ReservationAlreadyExistsException(String message) {
        super(message);
    }
}
