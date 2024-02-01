package org.mindswap.academy.mindera_travel_agency.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassDuplicateNameException;
import org.mindswap.academy.mindera_travel_agency.exception.fare_class.FareClassNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketDuplicateException;
import org.mindswap.academy.mindera_travel_agency.exception.flight_tickets.FlightTicketNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotChangeInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
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

    @ExceptionHandler({FareClassDuplicateNameException.class,
            FlightTicketDuplicateException.class,
            PaymentCompletedException.class,
            InvalidCheckInOutDateException.class,
            CannotChangeInvoiceException.class})
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

    @ExceptionHandler({FareClassNotFoundException.class,
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
