package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
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

    @Autowired
    private FareClassService fareClassService;

    @GetMapping("/")
    public ResponseEntity<List<FareClassGetDto>> getAll() {
        return ResponseEntity.ok(fareClassService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FareClassGetDto> getById(@PathVariable Long id) throws FareClassNotFoundException {
        return ResponseEntity.ok(fareClassService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<FareClassGetDto> getByName(@PathVariable String name) throws FareClassNotFoundException {
        return ResponseEntity.ok(fareClassService.getByName(name));
    }

    @PostMapping("/")
    public ResponseEntity<FareClassGetDto> create(@Valid @RequestBody FareClassCreateDto fareClass) {
        return new ResponseEntity<>(fareClassService.create(fareClass), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FareClassGetDto> update(@PathVariable Long id, @Valid @RequestBody FareClassCreateDto fareClass) {
        return ResponseEntity.ok(fareClassService.update(id, fareClass));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fareClassService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
