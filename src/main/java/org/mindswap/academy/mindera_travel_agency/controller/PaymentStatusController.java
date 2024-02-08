package org.mindswap.academy.mindera_travel_agency.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.StatusNameAlreadyExistsException;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The PaymentStatusController class handles the HTTP requests related to payment status operations.
 */
@RestController
@RequestMapping("/api/v1/payment_status")
public class PaymentStatusController {

    private final PaymentStatusService paymentStatusService;

    @Autowired
    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

    /**
     * Retrieves all payment statuses.
     *
     * @return ResponseEntity containing a list of PaymentStatusGetDto objects
     */
    @Operation(summary = "Get all payment statuses")
    @ApiResponse(responseCode = "200", description = "Return all payment statuses")
    @GetMapping("/")
    public ResponseEntity<List<PaymentStatusGetDto>> getAll() {
        return ResponseEntity.ok(paymentStatusService.getAll());
    }

    /**
     * Retrieves a payment status by its ID.
     *
     * @param id the ID of the payment status
     * @return ResponseEntity containing the PaymentStatusGetDto object
     * @throws PaymentStatusNotFoundException if the payment status is not found
     */
    @Operation(summary = "Get payment status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return payment status by ID"),
            @ApiResponse(responseCode = "404", description = "Payment status not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatusGetDto> getById(@PathVariable Long id) throws PaymentStatusNotFoundException {
        return ResponseEntity.ok(paymentStatusService.getById(id));
    }

    /**
     * Retrieves a payment status by its name.
     *
     * @param name the name of the payment status
     * @return ResponseEntity containing the PaymentStatusGetDto object
     * @throws PaymentStatusNotFoundException if the payment status is not found
     */
    @Operation(summary = "Get payment status by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return payment status by name"),
            @ApiResponse(responseCode = "404", description = "Payment status not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<PaymentStatusGetDto> getByName(@PathVariable String name) throws PaymentStatusNotFoundException {
        return ResponseEntity.ok(paymentStatusService.getByName(name));
    }

    /**
     * Creates a new payment status.
     *
     * @param paymentStatus the PaymentStatusCreateDto object containing the details of the payment status to be created
     * @return ResponseEntity containing the created PaymentStatusGetDto object
     * @throws StatusNameAlreadyExistsException if the payment status name already exists
     */
    @Operation(summary = "Create a new payment status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment status created"),
            @ApiResponse(responseCode = "400", description = "Payment status name already exists")
    })
    @PostMapping("/")
    public ResponseEntity<PaymentStatusGetDto> create(@Valid @RequestBody PaymentStatusCreateDto paymentStatus) throws StatusNameAlreadyExistsException {
        return new ResponseEntity<>(paymentStatusService.create(paymentStatus), HttpStatus.CREATED);
    }

    /**
     * Creates multiple payment statuses.
     *
     * @param paymentStatusList the list of PaymentStatusCreateDto objects containing the details of the payment statuses to be created
     * @return ResponseEntity containing a list of the created PaymentStatusGetDto objects
     * @throws StatusNameAlreadyExistsException if any of the payment status names already exist
     */
    @Operation(summary = "Create multiple payment statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment statuses created"),
            @ApiResponse(responseCode = "400", description = "Payment status name already exists")
    })
    @PostMapping("/List")
    public ResponseEntity<List<PaymentStatusGetDto>> createList(@Valid @RequestBody List<PaymentStatusCreateDto> paymentStatusList) throws StatusNameAlreadyExistsException {
        return new ResponseEntity<>(paymentStatusService.createList(paymentStatusList), HttpStatus.CREATED);
    }

    /**
     * Updates a payment status.
     *
     * @param id            the ID of the payment status to be updated
     * @param paymentStatus the PaymentStatusCreateDto object containing the updated details of the payment status
     * @return ResponseEntity containing the updated PaymentStatusGetDto object
     * @throws PaymentStatusNotFoundException   if the payment status is not found
     * @throws StatusNameAlreadyExistsException if the updated payment status name already exists
     */
    @Operation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status updated"),
            @ApiResponse(responseCode = "404", description = "Payment status not found"),
            @ApiResponse(responseCode = "400", description = "Payment status name already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentStatusGetDto> update(@PathVariable Long id, @Valid @RequestBody PaymentStatusCreateDto paymentStatus) throws PaymentStatusNotFoundException, StatusNameAlreadyExistsException {
        return ResponseEntity.ok(paymentStatusService.update(id, paymentStatus));
    }

    /**
     * Deletes a payment status.
     *
     * @param id the ID of the payment status to be deleted
     * @return ResponseEntity with no content
     * @throws PaymentStatusNotFoundException if the payment status is not found
     * @throws PaymentStatusInUseException    if the payment status is in use and cannot be deleted
     */
    @Operation(summary = "Delete payment status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment status deleted"),
            @ApiResponse(responseCode = "404", description = "Payment status not found"),
            @ApiResponse(responseCode = "400", description = "Payment status in use")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws PaymentStatusNotFoundException, PaymentStatusInUseException {
        paymentStatusService.delete(id);
        return ResponseEntity.ok().build();
    }
}
