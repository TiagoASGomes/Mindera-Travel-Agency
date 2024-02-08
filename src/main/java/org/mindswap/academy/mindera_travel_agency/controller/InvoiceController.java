package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.user.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The InvoiceController class handles HTTP requests related to invoices.
 */
@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Retrieves all invoices.
     *
     * @param page the pageable object for pagination
     * @return the ResponseEntity containing the page of InvoiceGetDto objects
     */
    @GetMapping("/")
    public ResponseEntity<Page<InvoiceGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(invoiceService.getAll(page));
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the ID of the invoice
     * @return the ResponseEntity containing the InvoiceGetDto object
     * @throws InvoiceNotFoundException if the invoice is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceGetDto> getById(@PathVariable Long id) throws InvoiceNotFoundException {
        return ResponseEntity.ok(invoiceService.getById(id));
    }

    /**
     * Creates a new invoice.
     *
     * @param invoice the InvoiceCreateDto object containing the invoice details
     * @return the ResponseEntity containing the created InvoiceGetDto object
     * @throws UserNotFoundException          if the user is not found
     * @throws PaymentStatusNotFoundException if the payment status is not found
     */
    @PostMapping("/")
    public ResponseEntity<InvoiceGetDto> create(@Valid @RequestBody InvoiceCreateDto invoice) throws UserNotFoundException, PaymentStatusNotFoundException {
        return new ResponseEntity<>(invoiceService.create(invoice), HttpStatus.CREATED);
    }

    /**
     * Updates the payment details of an invoice.
     *
     * @param id      the ID of the invoice
     * @param invoice the InvoiceUpdateDto object containing the updated invoice details
     * @return the ResponseEntity containing the updated InvoiceGetDto object
     * @throws InvoiceNotFoundException       if the invoice is not found
     * @throws PaymentStatusNotFoundException if the payment status is not found
     * @throws PaymentCompletedException      if the payment is already completed
     */
    @PatchMapping("/{id}/payment")
    public ResponseEntity<InvoiceGetDto> updatePayment(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateDto invoice) throws InvoiceNotFoundException, PaymentStatusNotFoundException, PaymentCompletedException {
        return ResponseEntity.ok(invoiceService.update(id, invoice));
    }

    /**
     * Finalizes an invoice.
     *
     * @param id the ID of the invoice
     * @return the ResponseEntity containing the finalized InvoiceGetDto object
     * @throws InvoiceNotCompleteException    if the invoice is not complete
     * @throws InvoiceNotFoundException       if the invoice is not found
     * @throws UnirestException               if an error occurs during the HTTP request
     * @throws JsonProcessingException        if an error occurs during JSON processing
     * @throws PaymentCompletedException      if the payment is already completed
     * @throws PaymentStatusNotFoundException if the payment status is not found
     */
    @PatchMapping("/{id}/finalize")
    public ResponseEntity<InvoiceGetDto> finalizeInvoice(@PathVariable Long id) throws InvoiceNotCompleteException, InvoiceNotFoundException, UnirestException, JsonProcessingException, PaymentCompletedException, PaymentStatusNotFoundException {
        return ResponseEntity.ok(invoiceService.finalizeInvoice(id));
    }

    /**
     * Deletes an invoice.
     *
     * @param id the ID of the invoice
     * @return the ResponseEntity with no content
     * @throws InvoiceNotFoundException  if the invoice is not found
     * @throws PaymentCompletedException if the payment is already completed
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        invoiceService.delete(id);
        return ResponseEntity.ok().build();
    }
}
