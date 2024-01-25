package org.mindswap.academy.mindera_travel_agency.exception.fare_class;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class FareClassNotFoundException extends TravelAgencyException {
    public FareClassNotFoundException(String message) {
        super(message);
    }
}
