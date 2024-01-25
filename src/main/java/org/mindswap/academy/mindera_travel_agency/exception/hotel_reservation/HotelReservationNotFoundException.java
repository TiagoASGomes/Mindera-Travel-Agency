package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class HotelReservationNotFoundException extends TravelAgencyException {
    public HotelReservationNotFoundException(String message) {
        super(message);
    }
}
