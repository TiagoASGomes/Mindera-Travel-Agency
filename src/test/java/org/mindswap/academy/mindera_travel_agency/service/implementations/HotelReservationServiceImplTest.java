package org.mindswap.academy.mindera_travel_agency.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.*;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.InvoiceNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.invoice.PaymentCompletedException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.HotelReservationRepository;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HotelReservationServiceImplTest {

    @Mock
    private HotelReservationRepository hotelReservationRepository;

    @InjectMocks
    private HotelReservationServiceImpl hotelReservationServiceImpl;
    @Mock
    private HotelReservationService hotelReservationService;
    @Mock
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() throws HotelReservationNotFoundException {
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        HotelReservationGetDto result = hotelReservationServiceImpl.getById(1L);

        assertNotNull(result);
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdNotFound() {
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelReservationNotFoundException.class, () -> hotelReservationServiceImpl.getById(1L));
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdWhenReservationIsNull() {
        when(hotelReservationRepository.findById(1L)).thenReturn(null);

        assertThrows(HotelReservationNotFoundException.class, () -> hotelReservationServiceImpl.getById(1L));
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdWhenReservationIsEmpty() {
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelReservationNotFoundException.class, () -> hotelReservationServiceImpl.getById(1L));
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdWhenReservationHasNoRooms() throws HotelReservationNotFoundException {
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        HotelReservationGetDto result = hotelReservationService.getById(1L);

        assertNotNull(result);
        assertTrue(result.rooms().isEmpty());
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdWhenReservationHasOneRoom() throws HotelReservationNotFoundException {
        HotelReservation hotelReservation = new HotelReservation();
        RoomInfo roomInfo = new RoomInfo();
        hotelReservation.addRoom(roomInfo);
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        HotelReservationGetDto result = hotelReservationService.getById(1L);

        assertNotNull(result);
        assertEquals(1, result.rooms().size());
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetByIdWhenReservationHasMultipleRooms() throws HotelReservationNotFoundException {
        HotelReservation hotelReservation = new HotelReservation();
        RoomInfo roomInfo1 = new RoomInfo();
        RoomInfo roomInfo2 = new RoomInfo();
        hotelReservation.addRoom(roomInfo1);
        hotelReservation.addRoom(roomInfo2);
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        HotelReservationGetDto result = hotelReservationService.getById(1L);

        assertNotNull(result);
        assertEquals(2, result.rooms().size());
        verify(hotelReservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateWhenInvoiceNotFound() {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                hotelInfo,
                1L
        );
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> hotelReservationService.create(createDto));
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateWhenReservationIsSuccessfullyCreated() throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, UnirestException, JsonProcessingException, PaymentCompletedException {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                hotelInfo,
                1L
        );
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.save(any(HotelReservation.class))).thenReturn(hotelReservation);

        HotelReservationGetDto result = hotelReservationService.create(createDto);

        assertNotNull(result);
        verify(invoiceRepository, times(1)).findById(1L);
        verify(hotelReservationRepository, times(1)).save(any(HotelReservation.class));
    }

    @Test
    public void testCreateWhenArrivalDateIsInThePast() {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                hotelInfo,
                1L
        );

        assertThrows(InvalidDateException.class, () -> hotelReservationService.create(createDto));
    }

    @Test
    public void testCreateWhenLeaveDateIsBeforeArrivalDate() {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                hotelInfo,
                1L
        );

        assertThrows(InvalidDateException.class, () -> hotelReservationService.create(createDto));
    }

    @Test
    public void testCreateWhenInvoiceIdIsLessThan1() {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                hotelInfo,
                0L
        );

        assertThrows(invalidIdException.class, () -> hotelReservationService.create(createDto));
    }

    @Test
    public void testCreateWhenReservationIsSuccessfullyCreatedWithMultipleRooms() {
        ExternalHotelInfoDto hotelInfo = mock(ExternalHotelInfoDto.class);
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                hotelInfo,
                1L
        );
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        HotelReservation hotelReservation = new HotelReservation();
        hotelReservation.addRoom(new RoomInfo());
        hotelReservation.addRoom(new RoomInfo());
    }

    @Test
    public void testUpdateReservationWhenReservationNotFound() {
        HotelReservationCreateDto createDto = mock(HotelReservationCreateDto.class);
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelReservationNotFoundException.class, () -> hotelReservationService.updateReservation(1L, createDto));
    }

    @Test
    public void testUpdateReservationWhenInvoiceNotFound() {
        HotelReservationCreateDto createDto = mock(HotelReservationCreateDto.class);
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> hotelReservationService.updateReservation(1L, createDto));
    }

    @Test
    public void testUpdateReservationWhenArrivalDateIsInThePast() {
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                mock(ExternalHotelInfoDto.class),
                1L
        );

        assertThrows(ConstraintViolationException.class, () -> hotelReservationService.updateReservation(1L, createDto));
    }

    @Test
    public void testUpdateReservationWhenLeaveDateIsBeforeArrivalDate() {
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                mock(ExternalHotelInfoDto.class),
                1L
        );

        assertThrows(ConstraintViolationException.class, () -> hotelReservationService.updateReservation(1L, createDto));
    }

    @Test
    public void testUpdateReservationWhenReservationIsSuccessfullyUpdated() throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, CannotChangeInvoiceException, PaymentCompletedException {
        HotelReservationCreateDto createDto = mock(HotelReservationCreateDto.class);
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoice));

        HotelReservationGetDto result = hotelReservationService.updateReservation(1L, createDto);

        assertNotNull(result);
        verify(hotelReservationRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateReservationWhenUpdateIsSuccessful() throws HotelReservationNotFoundException, InvalidCheckInOutDateException, InvoiceNotFoundException, CannotChangeInvoiceException, PaymentCompletedException {
        HotelReservationCreateDto createDto = new HotelReservationCreateDto(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                mock(ExternalHotelInfoDto.class),
                1L
        );
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoice));

        HotelReservationGetDto result = hotelReservationService.updateReservation(1L, createDto);

        assertNotNull(result);
        verify(hotelReservationRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testDeleteWhenReservationNotFound() {
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelReservationNotFoundException.class, () -> hotelReservationService.delete(1L));
    }

    @Test
    public void testDeleteWhenDeletionIsSuccessful() throws HotelReservationNotFoundException, PaymentCompletedException {
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        hotelReservationService.delete(1L);

        verify(hotelReservationRepository, times(1)).delete(hotelReservation);
    }

    @Test
    public void testDeleteWhenReservationIsSuccessfullyDeleted() throws HotelReservationNotFoundException, PaymentCompletedException {
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));

        hotelReservationService.delete(1L);

        verify(hotelReservationRepository, times(1)).delete(hotelReservation);
    }

    @Test
    public void testDeleteWhenUnexpectedErrorOccurs() {
        HotelReservation hotelReservation = new HotelReservation();
        when(hotelReservationRepository.findById(1L)).thenReturn(Optional.of(hotelReservation));
        doThrow(RuntimeException.class).when(hotelReservationRepository).delete(hotelReservation);

        assertThrows(RuntimeException.class, () -> hotelReservationService.delete(1L));
    }
}



