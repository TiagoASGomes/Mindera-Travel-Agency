package org.mindswap.academy.mindera_travel_agency.aspect;

import lombok.*;

import java.util.Date;

/**
 * Represents an error that can occur in the agency application.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgencyError {
    private String message;
    private String path;
    private int status;
    private String method;
    private Date timestamp;
}
