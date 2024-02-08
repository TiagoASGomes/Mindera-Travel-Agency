package org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class CannotUpdateToDifferentInvoiceException extends TravelAgencyException {
    public CannotUpdateToDifferentInvoiceException(String message) {
        super(message);
    }
}
