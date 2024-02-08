package org.mindswap.academy.mindera_travel_agency.exception.user;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class DuplicateEmailException extends TravelAgencyException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
