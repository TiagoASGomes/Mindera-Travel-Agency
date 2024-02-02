package org.mindswap.academy.mindera_travel_agency.exception.payment_status;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class StatusNameAlreadyExistsException extends TravelAgencyException {
    public StatusNameAlreadyExistsException(String message) {
        super(message);
    }
}
