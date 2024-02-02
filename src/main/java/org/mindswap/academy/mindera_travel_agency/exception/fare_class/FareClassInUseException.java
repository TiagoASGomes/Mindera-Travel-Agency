package org.mindswap.academy.mindera_travel_agency.exception.fare_class;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class FareClassInUseException extends TravelAgencyException {
    public FareClassInUseException(String message) {
        super(message);
    }
}
