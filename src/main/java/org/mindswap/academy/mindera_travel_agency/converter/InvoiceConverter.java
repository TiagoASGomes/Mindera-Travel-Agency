package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceConverter {


    public List<InvoiceGetDto> fromEntityListToGetDtoList(List<Invoice> invoices) {
        return null;
    }

    public InvoiceGetDto fromEntityToGetDto(Invoice invoice) {
        return null;
    }

    public Invoice fromCreateDtoToEntity(InvoiceCreateDto invoice) {
        return null;
    }
}
