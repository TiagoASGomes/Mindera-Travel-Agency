package org.mindswap.academy.mindera_travel_agency.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.MaxFlightPerInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.*;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotCompleteException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusInUseException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.StatusNameAlreadyExistsException;
import org.mindswap.academy.mindera_travel_agency.exception.user.DuplicateEmailException;
import org.mindswap.academy.mindera_travel_agency.exception.user.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.user.PasswordsDidNotMatchException;
import org.mindswap.academy.mindera_travel_agency.exception.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@Component
@ControllerAdvice
public class AgencyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgencyExceptionHandler.class);

    /**
     * Exception handler for bad requests. It handles various types of exceptions
     * related to flight tickets, payment, check-in/out dates, invoice, payment status,
     * user, and hotel reservation. It logs the error message and returns a ResponseEntity
     * with the appropriate error details.
     *
     * @param e       The exception that occurred
     * @param request The HTTP servlet request
     * @return A ResponseEntity containing the error details and HTTP status code
     */
    @ExceptionHandler({
            FlightTicketDuplicateException.class,
            PaymentCompletedException.class,
            InvalidCheckInOutDateException.class,
            CannotUpdateToDifferentInvoiceException.class,
            StatusNameAlreadyExistsException.class,
            PaymentStatusInUseException.class,
            DuplicateEmailException.class,
            PasswordsDidNotMatchException.class,
            InvoiceNotCompleteException.class,
            MaxFlightPerInvoiceException.class,
            ReservationAlreadyExistsException.class})
    public ResponseEntity<AgencyError> handleBadRequest(Exception e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(
                AgencyError.builder()
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .method(request.getMethod())
                        .timestamp(new Date())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for not found errors. It handles various types of exceptions
     * related to flight tickets, hotel reservation, invoice, payment status, user, and room.
     * It logs the error message and returns a ResponseEntity with the appropriate error details.
     *
     * @param e       The exception that occurred
     * @param request The HTTP servlet request
     * @return A ResponseEntity containing the error details and HTTP status code
     */
    @ExceptionHandler({
            FlightTicketNotFoundException.class,
            HotelReservationNotFoundException.class,
            InvoiceNotFoundException.class,
            PaymentStatusNotFoundException.class,
            UserNotFoundException.class,
            EmailNotFoundException.class,
            RoomNotFoundException.class})
    public ResponseEntity<AgencyError> handleNotFound(Exception e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(
                AgencyError.builder()
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .status(HttpStatus.NOT_FOUND.value())
                        .method(request.getMethod())
                        .timestamp(new Date())
                        .build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for validation errors. It handles MethodArgumentNotValidException
     * and extracts the field errors. It logs the error message and returns a ResponseEntity
     * with the appropriate error details.
     *
     * @param ex      The MethodArgumentNotValidException that occurred
     * @param request The HTTP servlet request
     * @return A ResponseEntity containing the error details and HTTP status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AgencyError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        StringBuilder errorMessageBuilder = new StringBuilder();
        errors.forEach(error -> errorMessageBuilder.append(error).append(", "));
        errorMessageBuilder.delete(errorMessageBuilder.length() - 2, errorMessageBuilder.length()).append(".");
        String errorMessage = errorMessageBuilder.toString();
        logger.error(errorMessage);
        return new ResponseEntity<>(
                AgencyError.builder()
                        .message(errorMessage)
                        .path(request.getRequestURI())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .method(request.getMethod())
                        .timestamp(new Date())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
