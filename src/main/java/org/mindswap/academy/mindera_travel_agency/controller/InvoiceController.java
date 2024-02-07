package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<InvoiceGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(invoiceService.getAll(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceGetDto> getById(@PathVariable Long id) throws InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    @PostMapping("/")
    public ResponseEntity<InvoiceGetDto> create(@Valid @RequestBody InvoiceCreateDto invoice) throws UserNotFoundException, PaymentStatusNotFoundException {
        return new ResponseEntity<>(invoiceService.create(invoice), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<InvoiceGetDto> updatePayment(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(invoiceService.update(id, invoice));
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<InvoiceGetDto> finalize(@PathVariable Long id) throws InvoiceNotCompleteException, InvoiceNotFoundException, UnirestException, JsonProcessingException, PaymentCompletedException, PaymentStatusNotFoundException {
        return ResponseEntity.ok(invoiceService.finalizeInvoice(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        invoiceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
