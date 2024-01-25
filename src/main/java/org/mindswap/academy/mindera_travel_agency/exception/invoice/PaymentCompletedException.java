package org.mindswap.academy.mindera_travel_agency.exception.invoice;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class PaymentCompletedException extends TravelAgencyException {
    public PaymentCompletedException(String message) {
        super(message);
    }
}
