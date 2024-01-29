package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceUpdateDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.exception.payment_status.PaymentStatusNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.PaymentStatusService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

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
    public InvoiceGetDto create(InvoiceCreateDto invoiceDto) throws UserNotFoundException, PaymentStatusNotFoundException {
        Invoice invoice = invoiceConverter.fromCreateDtoToEntity(userService.findById(invoiceDto.userId()));
        invoice.setPaymentStatus(paymentStatusService.findByName(NOT_REQUESTED_PAYMENT));
        return invoiceConverter.fromEntityToGetDto(invoiceRepository.save(invoice));
    }

    @Override
    public void delete(Long id) throws InvoiceNotFoundException, PaymentCompletedException {
        Invoice invoice = findById(id);
        if (invoice.getPaymentStatus().getStatusName().equals(PAID_PAYMENT) || invoice.getPaymentStatus().getStatusName().equals(PENDING_PAYMENT)) {
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
    public void updatePrice(Long id) throws InvoiceNotFoundException {
        Invoice invoice = findById(id);
        invoice.setTotalPrice(calculatePrice(invoice));
        invoiceRepository.save(invoice);
    }

    private int calculatePrice(Invoice invoice) {
        if(invoice.getHotelReservation()== null && invoice.getFlightTickets() == null){
            return 0;
        }
        if(invoice.getHotelReservation() == null){
            return invoice.getFlightTickets().stream()
                    .mapToInt(FlightTicket::getPrice)
                    .sum();
        }
        if(invoice.getFlightTickets() == null || invoice.getFlightTickets().isEmpty())  {
            return invoice.getHotelReservation().getTotalPrice();
        }
        return invoice.getFlightTickets().stream()
                .mapToInt(FlightTicket::getPrice)
                .sum() + invoice.getHotelReservation().getTotalPrice();
    }

    @Override
    public Invoice findById(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException(ID_NOT_FOUND + id));
    }
}
