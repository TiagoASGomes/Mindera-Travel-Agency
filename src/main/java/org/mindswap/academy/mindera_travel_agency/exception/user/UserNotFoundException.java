package org.mindswap.academy.mindera_travel_agency.exception.user;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class UserNotFoundException extends TravelAgencyException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
