package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.HotelReservationRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.CANNOT_ALTER_HOTEL_RESERVATION;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class HotelReservationServiceImpl implements HotelReservationService {
//TODO saver first update price later

    private final HotelReservationRepository hRRepository;
    private final HotelReservationConverter hRConverter;
    private final RoomInfoService roomInfoService;
    private final RoomInfoConverter roomInfoConverter;
    private final InvoiceService invoiceService;

    @Autowired
    public HotelReservationServiceImpl(HotelReservationRepository hRRepository, HotelReservationConverter hRConverter, RoomInfoService roomInfoService, RoomInfoConverter roomInfoConverter, InvoiceService invoiceService) {
        this.hRRepository = hRRepository;
        this.hRConverter = hRConverter;
        this.roomInfoService = roomInfoService;
        this.roomInfoConverter = roomInfoConverter;
        this.invoiceService = invoiceService;
    }

    @Override
    public List<HotelReservationGetDto> getAll() {
        return hRConverter.fromEntityListToGetDtoList(hRRepository.findAll());
    }

    @Override
    public HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException {
        return hRConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public HotelReservationGetDto create(HotelReservationCreateDto dtoReservation) throws InvoiceNotFoundException {
        Invoice invoice = invoiceService.findById(dtoReservation.invoiceId());
        HotelReservation convertedReservation = hRConverter.fromCreateDtoToEntity(dtoReservation, invoice);
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(convertedReservation));
        invoiceService.updatePrice(invoice.getId());
        return reservation;
    }

    @Override
    public HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto dtoReservation) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        HotelReservation dbReservation = findById(id);
        verifyIfInvoicePaid(dbReservation.getInvoice());
        dbReservation.setCheckInDate(dtoReservation.checkInDate());
        dbReservation.setCheckOutDate(dtoReservation.checkOutDate());
        dbReservation.setDurationOfStay(dtoReservation.checkOutDate().getDayOfMonth() - dtoReservation.checkInDate().getDayOfMonth());
        dbReservation.setTotalPrice(dbReservation.getPricePerNight() * dbReservation.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(dbReservation));
        invoiceService.updatePrice(dbReservation.getInvoice().getId());
        return reservation;
    }

    @Override
    public HotelReservationGetDto addRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        verifyIfInvoicePaid(hotelReservationToUpdate.getInvoice());
        RoomInfo roomInfo = roomInfoConverter.fromExternalDtoToEntity(room);
        hotelReservationToUpdate.addRoom(roomInfo);
        roomInfoService.create(roomInfo);
        hotelReservationToUpdate.setPricePerNight(calculatePrice(hotelReservationToUpdate));
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(hotelReservationToUpdate));
        invoiceService.updatePrice(hotelReservationToUpdate.getInvoice().getId());
        return reservation;
    }

    @Override
    public HotelReservationGetDto removeRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        verifyIfInvoicePaid(hotelReservationToUpdate.getInvoice());
        roomInfoService.delete(room.externalId());
        hotelReservationToUpdate.removeRoom(room.externalId());
        hotelReservationToUpdate.setPricePerNight(calculatePrice(hotelReservationToUpdate));
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(hotelReservationToUpdate));
        invoiceService.updatePrice(hotelReservationToUpdate.getInvoice().getId());
        return reservation;
    }

    @Override
    public HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException {
        findById(id);
        Invoice invoice = invoiceService.findById(hotelReservation.invoiceId());
        verifyIfInvoicePaid(invoice);
        HotelReservation dbReservation = hRConverter.fromCreateDtoToEntity(hotelReservation, invoice);
        dbReservation.setId(id);
        dbReservation.setTotalPrice(dbReservation.getPricePerNight() * dbReservation.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(dbReservation));
        invoiceService.updatePrice(dbReservation.getInvoice().getId());
        return reservation;
    }

    @Override
    public void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        HotelReservation hotelReservation = findById(id);
        verifyIfInvoicePaid(hotelReservation.getInvoice());
        hotelReservation.getRooms().forEach(roomInfo -> roomInfoService.delete(roomInfo.getId()));
        hRRepository.deleteById(id);
    }

    @Override
    public HotelReservation findById(Long id) throws HotelReservationNotFoundException {
        return hRRepository.findById(id).orElseThrow(() -> new HotelReservationNotFoundException(ID_NOT_FOUND + id));
    }

    private List<HotelReservation> sort(List<HotelReservation> hotelReservations, String sortBy) {
        //TODO adicionar sort
        return hotelReservations;
    }

    private void verifyIfInvoicePaid(Invoice invoice) throws PaymentCompletedException {
        String status = invoice.getPaymentStatus().getStatusName();
        if (status.equals("PENDING") || status.equals("COMPLETED")) {
            throw new PaymentCompletedException(CANNOT_ALTER_HOTEL_RESERVATION);
        }
    }

    private int calculatePrice(HotelReservation hotelReservationToUpdate) {
       return hotelReservationToUpdate.getRooms().stream()
                .map(RoomInfo::getPricePerNight)
                .reduce(0, Integer::sum);
    }
}
