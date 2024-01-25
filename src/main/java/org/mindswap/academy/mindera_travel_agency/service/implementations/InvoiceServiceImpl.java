package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.CANNOT_DELETE_PAID_INVOICE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceConverter invoiceConverter;
    @Autowired
    private PaymentStatusService paymentStatusService;

    @Override
    public List<InvoiceGetDto> getAll() {
        return invoiceConverter.fromEntityListToGetDtoList(invoiceRepository.findAll());
    }

    @Override
    public InvoiceGetDto getById(Long id) throws InvoiceNotFoundException {
        return invoiceConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public List<InvoiceGetDto> getByUserId(Long id) {
        //TODO implement
        return null;
    }

    @Override
    public InvoiceGetDto create(InvoiceCreateDto invoice) {
        //TODO adicionar user
        Invoice invoiceToSave = invoiceConverter.fromCreateDtoToEntity(invoice);
        return invoiceConverter.fromEntityToGetDto(invoiceRepository.save(invoiceToSave));
    }

    @Override
    public void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        Invoice invoice = findById(id);
        if (invoice.getPaymentStatus().getStatusName().equals("PAID") || invoice.getPaymentStatus().getStatusName().equals("PENDING")) {
            throw new PaymentCompletedException(CANNOT_DELETE_PAID_INVOICE);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public InvoiceGetDto update(Long id, InvoiceUpdateDto invoiceDto) throws InvoiceNotFoundException {
        Invoice invoice = findById(id);
        if (invoiceDto.paymentDate() != null) {
            invoice.setPaymentDate(invoiceDto.paymentDate());
        }
        if (invoiceDto.paymentStatus() != null) {
            invoice.setPaymentStatus(paymentStatusService.findByName(invoiceDto.paymentStatus()));
        }
        return invoiceConverter.fromEntityToGetDto(invoiceRepository.save(invoice));
    }

    @Override
    public Invoice findById(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException(ID_NOT_FOUND + id));
    }

    @Override
    public void updatePrice(Long id, int price) throws InvoiceNotFoundException {
        Invoice invoice = findById(id);
        invoice.setTotalPrice(price);
        invoiceRepository.save(invoice);
    }
}
