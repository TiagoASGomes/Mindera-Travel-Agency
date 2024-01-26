package org.mindswap.academy.mindera_travel_agency.aspect;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class AgencyError {
    private String message;
    private String path;
    private int status;
    private String method;
    private Date timestamp;
}
