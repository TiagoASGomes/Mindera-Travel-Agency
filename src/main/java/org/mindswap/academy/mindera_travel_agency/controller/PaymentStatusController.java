package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment_status")
public class PaymentStatusController {

    @Autowired
    private PaymentStatusService paymentStatusService;

    @GetMapping("/")
    public ResponseEntity<List<PaymentStatusGetDto>> getAll() {
        return ResponseEntity.ok(paymentStatusService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatusGetDto> getById(@PathVariable Long id) throws PaymentStatusNotFoundException {
        return ResponseEntity.ok(paymentStatusService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PaymentStatusGetDto> getByName(@PathVariable String name) throws PaymentStatusNotFoundException {
        return ResponseEntity.ok(paymentStatusService.getByName(name));
    }

    @PostMapping("/")
    public ResponseEntity<PaymentStatusGetDto> create(@Valid @RequestBody PaymentStatusCreateDto paymentStatus) {
        return new ResponseEntity<>(paymentStatusService.create(paymentStatus), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentStatusGetDto> update(@PathVariable Long id, @Valid @RequestBody PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException {
        return ResponseEntity.ok(paymentStatusService.update(id, paymentStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PaymentStatusNotFoundException {
        paymentStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}