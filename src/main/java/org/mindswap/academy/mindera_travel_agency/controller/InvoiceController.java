package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {


    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public ResponseEntity<List<InvoiceGetDto>> getAll() {
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceGetDto> getById(@PathVariable Long id) throws InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<InvoiceGetDto>> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getByUserId(id));
    }

    @PostMapping("/")
    public ResponseEntity<InvoiceGetDto> create(@Valid @RequestBody InvoiceCreateDto invoice) throws UserNotFoundException {
        return new ResponseEntity<>(invoiceService.create(invoice), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InvoiceGetDto> updatePayment(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException {
        return ResponseEntity.ok(invoiceService.update(id, invoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        invoiceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
