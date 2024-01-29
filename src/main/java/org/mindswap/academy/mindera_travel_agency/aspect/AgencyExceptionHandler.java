package org.mindswap.academy.mindera_travel_agency.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Component
@ControllerAdvice
public class AgencyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgencyExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AgencyError> handleException(Exception e, HttpServletRequest request) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(
                AgencyError.builder()
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .method(request.getMethod())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
