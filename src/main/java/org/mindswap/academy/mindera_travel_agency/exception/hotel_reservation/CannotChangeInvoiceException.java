package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class CannotChangeInvoiceException extends TravelAgencyException {
    public CannotChangeInvoiceException(String message) {
        super(message);
    }
}
