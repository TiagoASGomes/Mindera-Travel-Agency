package org.mindswap.academy.mindera_travel_agency.exception.invoice;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class InvoiceNotFoundException extends TravelAgencyException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
