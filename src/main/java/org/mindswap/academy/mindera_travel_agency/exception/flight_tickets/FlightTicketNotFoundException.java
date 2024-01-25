package org.mindswap.academy.mindera_travel_agency.exception.flight_tickets;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class FlightTicketNotFoundException extends TravelAgencyException {
    public FlightTicketNotFoundException(String message) {
        super(message);
    }
}
