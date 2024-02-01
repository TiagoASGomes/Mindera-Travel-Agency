package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class RoomNotFoundException extends TravelAgencyException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
