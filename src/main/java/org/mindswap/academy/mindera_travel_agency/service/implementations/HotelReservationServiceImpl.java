package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.CannotUpdateToDifferentInvoiceException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.InvalidCheckInOutDateException;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class HotelReservationServiceImpl implements HotelReservationService {

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
    public Page<HotelReservationGetDto> getAll(Pageable page) {
        Page<HotelReservation> hotelReservations = hRRepository.findAll(page);
        return hotelReservations.map(hRConverter::fromEntityToGetDto);
    }

    @Override
    public HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException {
        return hRConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public HotelReservationGetDto create(HotelReservationCreateDto dtoReservation) throws InvoiceNotFoundException, HotelReservationNotFoundException, InvalidCheckInOutDateException {
        Invoice invoice = invoiceService.findById(dtoReservation.invoiceId());
        checkIfArrivalBeforeDeparture(dtoReservation.arrivalDate(), dtoReservation.leaveDate());
        HotelReservation convertedReservation = hRConverter.fromCreateDtoToEntity(dtoReservation, invoice);
        Set<RoomInfo> roomInfo = roomInfoConverter.fromExternalDtoListToEntityList(dtoReservation.hotelInfo().rooms());
        convertedReservation.setRooms(roomInfo);
        convertedReservation.setPricePerNight(calculatePrice(convertedReservation));
        convertedReservation.setTotalPrice(convertedReservation.getPricePerNight() * convertedReservation.getDurationOfStay());
        HotelReservation savedHotel = hRRepository.save(convertedReservation);
        roomInfo.forEach(room -> room.setHotelReservation(savedHotel));
        roomInfo.forEach(roomInfoService::create);
        invoiceService.updateHotelPrice(invoice.getId());
        return hRConverter.fromEntityToGetDto(findById(savedHotel.getId()));
    }

    @Override
    public HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto dtoReservation) throws HotelReservationNotFoundException, CannotUpdateToDifferentInvoiceException, InvoiceNotFoundException, PaymentCompletedException, InvalidCheckInOutDateException {
        Invoice invoice = invoiceService.findById(dtoReservation.invoiceId());
        verifyIfInvoicePaid(invoice);
        HotelReservation dbReservation = findById(id);
        checkSameInvoice(dbReservation.getInvoice().getId(), dtoReservation.invoiceId());
        checkIfArrivalBeforeDeparture(dtoReservation.arrivalDate(), dtoReservation.leaveDate());
        HotelReservation convertedReservation = hRConverter.fromCreateDtoToEntity(dtoReservation, invoice);
        Set<RoomInfo> roomInfo = roomInfoConverter.fromExternalDtoListToEntityList(dtoReservation.hotelInfo().rooms());
        convertedReservation.setRooms(roomInfo);
        convertedReservation.setPricePerNight(calculatePrice(convertedReservation));
        convertedReservation.setTotalPrice(convertedReservation.getPricePerNight() * convertedReservation.getDurationOfStay());
        convertedReservation.setId(id);
        HotelReservation savedHotel = hRRepository.save(convertedReservation);
        roomInfo.forEach(room -> room.setHotelReservation(savedHotel));
        roomInfo.forEach(roomInfoService::create);
        dbReservation.getRooms().forEach(oldRoom -> roomInfoService.delete(oldRoom.getId()));
        invoiceService.updateHotelPrice(invoice.getId());
        return hRConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto dtoReservation) throws InvalidCheckInOutDateException, HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        checkIfArrivalBeforeDeparture(dtoReservation.arrivalDate(), dtoReservation.leaveDate());
        HotelReservation dbReservation = findById(id);
        verifyIfInvoicePaid(dbReservation.getInvoice());
        dbReservation.setArrivalDate(dtoReservation.arrivalDate());
        dbReservation.setLeaveDate(dtoReservation.leaveDate());
        dbReservation.setDurationOfStay(dtoReservation.leaveDate().getDayOfMonth() - dtoReservation.arrivalDate().getDayOfMonth());
        dbReservation.setTotalPrice(dbReservation.getPricePerNight() * dbReservation.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(dbReservation));
        invoiceService.updateHotelPrice(dbReservation.getInvoice().getId());
        return reservation;
    }

    @Override
    public HotelReservationGetDto addRoom(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException, PaymentCompletedException, InvoiceNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        verifyIfInvoicePaid(hotelReservationToUpdate.getInvoice());
        RoomInfo roomInfo = roomInfoConverter.fromExternalDtoToEntity(room);
        hotelReservationToUpdate.addRoom(roomInfo);
        hotelReservationToUpdate.setPricePerNight(calculatePrice(hotelReservationToUpdate));
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        HotelReservation savedHotel = hRRepository.save(hotelReservationToUpdate);
        roomInfo.setHotelReservation(savedHotel);
        roomInfoService.create(roomInfo);
        invoiceService.updateHotelPrice(hotelReservationToUpdate.getInvoice().getId());
        return hRConverter.fromEntityToGetDto(savedHotel);
    }

    @Override
    public HotelReservationGetDto removeRoom(Long id, Long roomId) throws HotelReservationNotFoundException, PaymentCompletedException, RoomNotFoundException, InvoiceNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        verifyIfInvoicePaid(hotelReservationToUpdate.getInvoice());
        roomInfoService.existsById(roomId);
        roomInfoService.delete(roomId);
        hotelReservationToUpdate.removeRoom(roomId);
        hotelReservationToUpdate.setPricePerNight(calculatePrice(hotelReservationToUpdate));
        hotelReservationToUpdate.setTotalPrice(hotelReservationToUpdate.getPricePerNight() * hotelReservationToUpdate.getDurationOfStay());
        HotelReservationGetDto reservation = hRConverter.fromEntityToGetDto(hRRepository.save(hotelReservationToUpdate));
        invoiceService.updateHotelPrice(hotelReservationToUpdate.getInvoice().getId());
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
        return hRRepository.findById(id).orElseThrow(() -> new HotelReservationNotFoundException(HOTEL_RESERVATION_ID_NOT_FOUND + id));
    }

    private void verifyIfInvoicePaid(Invoice invoice) throws PaymentCompletedException {
        String status = invoice.getPaymentStatus().getStatusName();
        if (status.equals(PENDING_PAYMENT) || status.equals(PAID_PAYMENT)) {
            throw new PaymentCompletedException(CANNOT_ALTER_HOTEL_RESERVATION);
        }
    }

    private int calculatePrice(HotelReservation hotelReservationToUpdate) {
        return hotelReservationToUpdate.getRooms().stream()
                .map(RoomInfo::getPricePerNight)
                .reduce(0, Integer::sum);
    }

    private void checkIfArrivalBeforeDeparture(LocalDate arrival, LocalDate departure) throws InvalidCheckInOutDateException {
        if (arrival.isAfter(departure)) {
            throw new InvalidCheckInOutDateException(INVALID_CHECK_IN_OUT_DATE);
        }
    }

    private void checkSameInvoice(Long dbInvoice, Long dtoInvoice) throws CannotUpdateToDifferentInvoiceException {
        if (!dbInvoice.equals(dtoInvoice)) {
            throw new CannotUpdateToDifferentInvoiceException(CANNOT_CHANGE_INVOICE);
        }
    }

}
