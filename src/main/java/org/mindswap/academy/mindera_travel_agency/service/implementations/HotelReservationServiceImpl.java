package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotChangeInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.HotelReservationRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class HotelReservationServiceImpl implements HotelReservationService {
    //TODO Nao deixar msm quartos na msm reserva

    private final HotelReservationRepository hRRepository;
    private final HotelReservationConverter hRConverter;
    private final RoomInfoService roomInfoService;
    private final RoomInfoConverter roomInfoConverter;
    private final InvoiceService invoiceService;
    private final ExternalService externalService;

    @Autowired
    public HotelReservationServiceImpl(HotelReservationRepository hRRepository, HotelReservationConverter hRConverter, RoomInfoService roomInfoService, RoomInfoConverter roomInfoConverter, InvoiceService invoiceService, ExternalService externalService) {
        this.hRRepository = hRRepository;
        this.hRConverter = hRConverter;
        this.roomInfoService = roomInfoService;
        this.roomInfoConverter = roomInfoConverter;
        this.invoiceService = invoiceService;
        this.externalService = externalService;
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
    public HotelReservationGetDto create(HotelReservationCreateDto dtoReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException {
        Invoice invoice = invoiceService.findById(dtoReservation.invoiceId());
        verifyIfInvoicePaid(invoice);
        checkIfCheckInDateIsBeforeCheckOutDate(dtoReservation.checkInDate(), dtoReservation.checkOutDate());
        HotelReservation convertedReservation = hRConverter.fromCreateDtoToEntity(dtoReservation, invoice);
        Set<RoomInfo> roomInfo = roomInfoConverter.fromExternalDtoListToEntityList(dtoReservation.hotelInfo().rooms());
        convertedReservation.setRooms(roomInfo);
        convertedReservation.setPricePerNight(calculatePrice(convertedReservation));
        convertedReservation.setTotalPrice(convertedReservation.getPricePerNight() * convertedReservation.getDurationOfStay());
        HotelReservation savedHotel = hRRepository.save(convertedReservation);
        roomInfo.forEach(room -> room.setHotelReservation(savedHotel));
        roomInfo.forEach(roomInfoService::create);
        invoiceService.updatePrice(invoice.getId());
        return hRConverter.fromEntityToGetDto(findById(savedHotel.getId()));
    }

    @Override
    public HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto dtoReservation) throws HotelReservationNotFoundException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException, CannotChangeInvoiceException {
        HotelReservation dbReservation = findById(id);
        checkSameInvoice(dbReservation.getInvoice().getId(), dtoReservation.invoiceId());
        dbReservation.getRooms().forEach(roomInfo -> roomInfoService.delete(roomInfo.getId(), dbReservation.getId()));
        Invoice invoice = invoiceService.findById(dtoReservation.invoiceId());
        verifyIfInvoicePaid(invoice);
        checkIfCheckInDateIsBeforeCheckOutDate(dtoReservation.checkInDate(), dtoReservation.checkOutDate());
        HotelReservation convertedReservation = hRConverter.fromCreateDtoToEntity(dtoReservation, invoice);
        Set<RoomInfo> roomInfo = roomInfoConverter.fromExternalDtoListToEntityList(dtoReservation.hotelInfo().rooms());
        convertedReservation.setRooms(roomInfo);
        convertedReservation.setPricePerNight(calculatePrice(convertedReservation));
        convertedReservation.setTotalPrice(convertedReservation.getPricePerNight() * convertedReservation.getDurationOfStay());
        convertedReservation.setId(id);
        HotelReservation savedHotel = hRRepository.save(convertedReservation);
        roomInfo.forEach(room -> room.setHotelReservation(savedHotel));
        roomInfo.forEach(roomInfoService::create);
        invoiceService.updatePrice(invoice.getId());
        return hRConverter.fromEntityToGetDto(findById(id));
    }

    private void checkSameInvoice(Long dbInvoice, Long dtoInvoice) throws CannotChangeInvoiceException {
        if (!dbInvoice.equals(dtoInvoice)) {
            throw new CannotChangeInvoiceException(INVALID_INVOICE);
        }
    }

    @Override
    public HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto dtoReservation) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, InvalidCheckInOutDateException {
        checkIfCheckInDateIsBeforeCheckOutDate(dtoReservation.checkInDate(), dtoReservation.checkOutDate());
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
    public HotelReservationGetDto addRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
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
    public HotelReservationGetDto removeRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException, RoomNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        verifyIfInvoicePaid(hotelReservationToUpdate.getInvoice());
        checkIfRoomExists(hotelReservationToUpdate, room);
        roomInfoService.delete(room.externalId(), hotelReservationToUpdate.getId());
        hotelReservationToUpdate.removeRoom(room.externalId());
        hotelReservationToUpdate.setPricePerNight(calculatePrice(hotelReservationToUpdate));
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(hotelReservationToUpdate));
        invoiceService.updatePrice(hotelReservationToUpdate.getInvoice().getId());
        return reservation;
    }

    @Override
    public void delete(Long id) throws HotelReservationNotFoundException, PaymentCompletedException {
        HotelReservation hotelReservation = findById(id);
        verifyIfInvoicePaid(hotelReservation.getInvoice());
        hRRepository.deleteById(id);
    }

    @Override
    public HotelReservation findById(Long id) throws HotelReservationNotFoundException {
        return hRRepository.findById(id).orElseThrow(() -> new HotelReservationNotFoundException(ID_NOT_FOUND + id));
    }

    private void verifyIfInvoicePaid(Invoice invoice) throws PaymentCompletedException {
        String status = invoice.getPaymentStatus().getStatusName();
        if (status.equals("PENDING") || status.equals("PAID")) {
            throw new PaymentCompletedException(CANNOT_ALTER_HOTEL_RESERVATION);
        }
    }

    private int calculatePrice(HotelReservation hotelReservationToUpdate) {
        return hotelReservationToUpdate.getRooms().stream()
                .map(RoomInfo::getPricePerNight)
                .reduce(0, Integer::sum);
    }

    private void checkIfCheckInDateIsBeforeCheckOutDate(LocalDateTime checkIn, LocalDateTime checkOut) throws InvalidCheckInOutDateException {
        if (checkIn.isAfter(checkOut)) {
            throw new InvalidCheckInOutDateException(INVALID_CHECK_IN_OUT_DATE);
        }
    }

    private void checkIfRoomExists(HotelReservation hotelReservationToUpdate, ExternalRoomInfoDto room) throws RoomNotFoundException {
        hotelReservationToUpdate.getRooms().stream()
                .filter(roomInfo -> roomInfo.getExternalId().equals(room.externalId()))
                .findFirst()
                .orElseThrow(() -> new RoomNotFoundException(ROOM_NOT_FOUND));
    }
}
