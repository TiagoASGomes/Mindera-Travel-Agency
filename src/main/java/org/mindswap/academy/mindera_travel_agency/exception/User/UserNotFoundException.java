package org.mindswap.academy.mindera_travel_agency.exception.User;

import org.mindswap.academy.mindera_travel_agency.exception.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
