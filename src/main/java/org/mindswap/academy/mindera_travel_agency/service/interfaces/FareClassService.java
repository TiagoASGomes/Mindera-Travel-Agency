package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassDuplicateNameException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;

import java.util.List;

public interface FareClassService {
    List<FareClassGetDto> getAll();

    FareClassGetDto getById(Long id) throws FareClassNotFoundException;

    FareClassGetDto getByName(String name) throws FareClassNotFoundException;

    FareClassGetDto create(FareClassCreateDto fareClass) throws FareClassDuplicateNameException;

    FareClassGetDto update(Long id, FareClassCreateDto fareClass) throws FareClassDuplicateNameException, FareClassNotFoundException;

    void delete(Long id) throws FareClassNotFoundException, FareClassInUseException;

    FareClass findById(Long id) throws FareClassNotFoundException;

    FareClass findByName(String name) throws FareClassNotFoundException;
}
