package org.mindswap.academy.mindera_travel_agency.exception.User;

import org.mindswap.academy.mindera_travel_agency.exception.UserException;

public class EmailNotFoundException extends UserException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
