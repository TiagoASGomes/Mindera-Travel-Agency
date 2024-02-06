package org.mindswap.academy.mindera_travel_agency.exception.invoice;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class InvoiceNotCompleteException extends TravelAgencyException {
    public InvoiceNotCompleteException(String message) {
        super(message);
    }
}
