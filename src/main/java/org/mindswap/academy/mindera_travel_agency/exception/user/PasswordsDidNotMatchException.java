package org.mindswap.academy.mindera_travel_agency.exception.user;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class PasswordsDidNotMatchException extends TravelAgencyException {
    public PasswordsDidNotMatchException(String message) {
        super(message);
    }
}
