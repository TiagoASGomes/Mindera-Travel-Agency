package org.mindswap.academy.mindera_travel_agency.exception.flight_tickets;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class FlightTicketDuplicateException extends TravelAgencyException {
    public FlightTicketDuplicateException(String message) {
        super(message);
    }

}
