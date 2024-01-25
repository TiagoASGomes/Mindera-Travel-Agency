package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;

public interface PaymentStatusService {

    PaymentStatus findByName(String name);
}
