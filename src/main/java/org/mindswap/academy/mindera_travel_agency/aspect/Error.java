package org.mindswap.academy.mindera_travel_agency.aspect;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Builder
@Getter
@Setter
public class Error {
    private String message;
    private String path;
    private String method;
    private Date timestamp;
}

