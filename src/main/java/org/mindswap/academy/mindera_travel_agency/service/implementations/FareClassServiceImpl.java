package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FareClassConverter;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassDuplicateNameException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FareClass;
import org.mindswap.academy.mindera_travel_agency.repository.FareClassRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FareClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class FareClassServiceImpl implements FareClassService {

    private final FareClassRepository fareClassRepository;
    private final FareClassConverter fareClassConverter;

    @Autowired
    public FareClassServiceImpl(FareClassRepository fareClassRepository, FareClassConverter fareClassConverter) {
        this.fareClassRepository = fareClassRepository;
        this.fareClassConverter = fareClassConverter;
    }

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
    public FareClassGetDto create(FareClassCreateDto fareClass) throws FareClassDuplicateNameException {
        checkDuplicateName(fareClass.className());
        FareClass fareClassToSave = fareClassConverter.fromCreateDtoToEntity(fareClass);
        return fareClassConverter.fromEntityToGetDto(fareClassRepository.save(fareClassToSave));
    }

    @Override
    public FareClassGetDto update(Long id, FareClassCreateDto fareClass) throws FareClassDuplicateNameException, FareClassNotFoundException {
        checkDuplicateName(fareClass.className());
        FareClass fareClassToUpdate = findById(id);
        fareClassToUpdate.setClassName(fareClass.className());
        return fareClassConverter.fromEntityToGetDto(fareClassRepository.save(fareClassToUpdate));
    }

    @Override
    public void delete(Long id) throws FareClassNotFoundException {
        findById(id);
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

    private void checkDuplicateName(String name) throws FareClassDuplicateNameException {
        Optional<FareClass> fareClass = fareClassRepository.findByClassName(name);
        if (fareClass.isPresent()) {
            throw new FareClassDuplicateNameException(NAME_TAKEN);
        }
    }
}
