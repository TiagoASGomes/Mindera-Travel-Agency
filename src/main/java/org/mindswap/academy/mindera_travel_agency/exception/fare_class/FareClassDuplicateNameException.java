package org.mindswap.academy.mindera_travel_agency.exception.fare_class;

import org.mindswap.academy.mindera_travel_agency.exception.TravelAgencyException;

public class FareClassDuplicateNameException extends TravelAgencyException {
    public FareClassDuplicateNameException(String message) {
        super(message);
    }
}
