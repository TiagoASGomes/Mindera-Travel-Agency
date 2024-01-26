package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FareClassConverter;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;
import org.mindswap.academy.mindera_travel_agency.repository.FareClassRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FareClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.NAME_NOT_FOUND;

@Service
public class FareClassServiceImpl implements FareClassService {

    @Autowired
    private FareClassRepository fareClassRepository;
    @Autowired
    private FareClassConverter fareClassConverter;

    @Override
    public List<FareClassGetDto> getAll() {
        return fareClassConverter.fromEntityListToGetDtoList(fareClassRepository.findAll());
    }

    @Override
    public FareClassGetDto getById(Long id) throws FareClassNotFoundException {
        return fareClassConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public FareClassGetDto getByName(String name) throws FareClassNotFoundException {
        return fareClassConverter.fromEntityToGetDto(findByName(name));
    }

    @Override
    public FareClassGetDto create(FareClassCreateDto fareClass) {
        FareClass fareClassToSave = fareClassConverter.fromCreateDtoToEntity(fareClass);
        return fareClassConverter.fromEntityToGetDto(fareClassRepository.save(fareClassToSave));
    }

    @Override
    public FareClassGetDto update(Long id, FareClassCreateDto fareClass) {
        FareClass fareClassToUpdate = fareClassConverter.fromCreateDtoToEntity(fareClass);
        fareClassToUpdate.setId(id);
        return fareClassConverter.fromEntityToGetDto(fareClassRepository.save(fareClassToUpdate));
    }

    @Override
    public void delete(Long id) {
        fareClassRepository.deleteById(id);
    }

    @Override
    public FareClass findById(Long id) throws FareClassNotFoundException {
        return fareClassRepository.findById(id).orElseThrow(() -> new FareClassNotFoundException(ID_NOT_FOUND + id));
    }

    @Override
    public FareClass findByName(String name) throws FareClassNotFoundException {
        return fareClassRepository.findByClassName(name).orElseThrow(() -> new FareClassNotFoundException(NAME_NOT_FOUND + name));
    }
}
