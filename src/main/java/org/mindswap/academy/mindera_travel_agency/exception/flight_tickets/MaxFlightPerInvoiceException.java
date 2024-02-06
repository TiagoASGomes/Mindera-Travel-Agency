package org.mindswap.academy.mindera_travel_agency.exception.flight_tickets;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class MaxFlightPerInvoiceException extends TravelAgencyException {
    public MaxFlightPerInvoiceException(String message) {
        super(message);
    }
}
