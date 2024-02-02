package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassDuplicateNameException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FareClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fare_classes")
public class FareClassController {
    //TODO add post with list of fare classes

    private final FareClassService fareClassService;

    @Autowired
    public FareClassController(FareClassService fareClassService) {
        this.fareClassService = fareClassService;
    }

    @GetMapping("/")
    public ResponseEntity<List<FareClassGetDto>> getAll() {
        return ResponseEntity.ok(fareClassService.getAll());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<FareClassGetDto> getByName(@PathVariable String name) throws FareClassNotFoundException {
        return ResponseEntity.ok(fareClassService.getByName(name));
    }

    @PostMapping("/")
    public ResponseEntity<FareClassGetDto> create(@Valid @RequestBody FareClassCreateDto fareClass) throws FareClassDuplicateNameException {
        return new ResponseEntity<>(fareClassService.create(fareClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FareClassGetDto> update(@PathVariable Long id, @Valid @RequestBody FareClassCreateDto fareClass) throws FareClassDuplicateNameException, FareClassNotFoundException {
        return ResponseEntity.ok(fareClassService.update(id, fareClass));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws FareClassNotFoundException, FareClassInUseException {
        fareClassService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
