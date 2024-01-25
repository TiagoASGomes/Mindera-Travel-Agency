package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareClassConverter {
    public List<FareClassGetDto> fromEntityListToGetDtoList(List<FareClass> classes) {
        return null;
    }

    public FareClassGetDto fromEntityToGetDto(FareClass fareClass) {
        return null;
    }

    public FareClass fromCreateDtoToEntity(FareClassCreateDto fareClass) {
        return null;
    }
}
