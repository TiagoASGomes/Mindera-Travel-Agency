package org.mindswap.academy.mindera_travel_agency.exception.payment_status;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class PaymentStatusInUseException extends TravelAgencyException {
    public PaymentStatusInUseException(String message) {
        super(message);
    }
}
