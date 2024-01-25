package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.PaymentStatusConverter;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.mindswap.academy.mindera_travel_agency.repository.PaymentStatusRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private PaymentStatusConverter paymentStatusConverter;

    @Override
    public PaymentStatus findByName(String name) {
        return null;
    }
}
