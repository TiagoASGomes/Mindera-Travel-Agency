package org.mindswap.academy.mindera_travel_agency.exception.user;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class EmailNotFoundException extends TravelAgencyException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
