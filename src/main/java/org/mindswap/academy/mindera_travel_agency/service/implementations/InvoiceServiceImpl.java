package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.CANNOT_DELETE_INVOICE;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceConverter invoiceConverter;
    private final PaymentStatusService paymentStatusService;
    private final UserService userService;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceConverter invoiceConverter, PaymentStatusService paymentStatusService, UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceConverter = invoiceConverter;
        this.paymentStatusService = paymentStatusService;
        this.userService = userService;
    }

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
        return invoiceConverter.fromEntityListToGetDtoList(invoiceRepository.findByUserId(id));
    }

    @Override
    public InvoiceGetDto create(InvoiceCreateDto invoiceDto) throws UserNotFoundException {
        Invoice invoice = invoiceConverter.fromCreateDtoToEntity(userService.findById(invoiceDto.userId()));
        return invoiceConverter.fromEntityToGetDto(invoiceRepository.save(invoice));
    }

    @Override
    public void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        Invoice invoice = findById(id);
        if (invoice.getPaymentStatus().getStatusName().equals("PAID") || invoice.getPaymentStatus().getStatusName().equals("PENDING")) {
            throw new PaymentCompletedException(CANNOT_DELETE_INVOICE);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public InvoiceGetDto update(Long id, InvoiceUpdateDto invoiceDto) throws InvoiceNotFoundException, PaymentStatusNotFoundException {
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
    public void updatePrice(Long id, int price) throws InvoiceNotFoundException {
        Invoice invoice = findById(id);
        invoice.setTotalPrice(price);
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException(ID_NOT_FOUND + id));
    }
}
