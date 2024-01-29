package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentStatusConverter {
    public List<PaymentStatusGetDto> fromEntityListToGetDtoList(List<PaymentStatus> paymentStatuses) {
        if (paymentStatuses == null) return new ArrayList<>();
        return paymentStatuses.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public PaymentStatusGetDto fromEntityToGetDto(PaymentStatus paymentStatus) {
        return new PaymentStatusGetDto(
                paymentStatus.getId(),
                paymentStatus.getStatusName());
    }

    public PaymentStatus fromCreateDtoToEntity(PaymentStatusCreateDto paymentStatus) {
        return new PaymentStatus(paymentStatus.statusName());
    }
}
