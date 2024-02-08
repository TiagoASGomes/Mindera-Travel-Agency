package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The PaymentStatusConverter class is responsible for converting PaymentStatus objects to DTOs (Data Transfer Objects)
 * and vice versa.
 */
@Component
public class PaymentStatusConverter {
    /**
     * Converts a list of PaymentStatus entities to a list of PaymentStatusGetDto objects.
     *
     * @param paymentStatuses the list of PaymentStatus entities to be converted
     * @return the list of PaymentStatusGetDto objects
     */
    public List<PaymentStatusGetDto> fromEntityListToGetDtoList(List<PaymentStatus> paymentStatuses) {
        if (paymentStatuses == null) return new ArrayList<>();
        return paymentStatuses.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    /**
     * Converts a PaymentStatus entity to a PaymentStatusGetDto object.
     *
     * @param paymentStatus the PaymentStatus entity to be converted
     * @return the PaymentStatusGetDto object
     */
    public PaymentStatusGetDto fromEntityToGetDto(PaymentStatus paymentStatus) {
        return new PaymentStatusGetDto(
                paymentStatus.getId(),
                paymentStatus.getStatusName());
    }

    /**
     * Converts a PaymentStatusCreateDto object to a PaymentStatus entity.
     *
     * @param paymentStatus the PaymentStatusCreateDto object to be converted
     * @return the PaymentStatus entity
     */
    public PaymentStatus fromCreateDtoToEntity(PaymentStatusCreateDto paymentStatus) {
        return new PaymentStatus(paymentStatus.statusName());
    }
}
