package org.mindswap.academy.mindera_travel_agency.exception.payment_status;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class PaymentStatusNotFoundException extends TravelAgencyException {
    public PaymentStatusNotFoundException(String message) {
        super(message);
    }
}
