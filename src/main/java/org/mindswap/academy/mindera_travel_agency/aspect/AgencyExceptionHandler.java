package org.mindswap.academy.mindera_travel_agency.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Component
@ControllerAdvice
public class AgencyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AgencyExceptionHandler.class);

}
