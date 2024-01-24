package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.HotelReservationRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class HotelReservationServiceImpl implements HotelReservationService {

    @Autowired
    private HotelReservationRepository hotelReservationRepository;

    @Autowired
    private HotelReservationConverter hotelReservationConverter;
    @Autowired
    private RoomInfoService roomInfoService;
    @Autowired
    private RoomInfoConverter roomInfoConverter;
    @Autowired
    private InvoiceService invoiceService;

    @Override
    public List<HotelReservationGetDto> getAll() {
        return hotelReservationConverter.fromEntityListToGetDtoList(hotelReservationRepository.findAll());
    }

    @Override
    public HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException {
        return hotelReservationConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public List<HotelReservationGetDto> getAllByUser(String sortBy, Long userId) {
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAllByUserId(userId);
        return hotelReservationConverter.fromEntityListToGetDtoList(sort(hotelReservations, sortBy));
    }

    @Override
    public List<HotelReservationGetDto> getAllByUserAndByName(String hotelName, String sortBy, Long userId) {
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAllByUserId(userId).stream()
                .filter(hotelReservation -> hotelReservation.getHotelName().contains(hotelName))
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(sort(hotelReservations, sortBy));
    }

    @Override
    public HotelReservationGetDto create(HotelReservationCreateDto dtoReservation) {
        HotelReservation dbReservation = hotelReservationConverter.fromCreateDtoToEntity(dtoReservation);
        setReservationProperties(dbReservation, dtoReservation);
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(dbReservation));
    }

    @Override
    public HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto dtoReservation) throws HotelReservationNotFoundException {
        HotelReservation dbReservation = findById(id);
        dbReservation.setCheckInDate(dtoReservation.checkInDate());
        dbReservation.setCheckOutDate(dtoReservation.checkOutDate());
        dbReservation.setDurationOfStay(dtoReservation.checkOutDate().getDayOfMonth() - dtoReservation.checkInDate().getDayOfMonth());
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(dbReservation));
    }

    @Override
    public HotelReservationGetDto addRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        RoomInfo roomInfo = roomInfoConverter.fromExternalDtoToEntity(room);
        hotelReservationToUpdate.addRoom(roomInfo);
        roomInfoService.create(roomInfo);
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToUpdate));
    }

    @Override
    public HotelReservationGetDto removeRooms(Long id, ExternalRoomInfoDto room) throws HotelReservationNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        roomInfoService.delete(room.externalId());
        hotelReservationToUpdate.removeRoom(room.externalId());
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToUpdate));
    }

    @Override
    public HotelReservationGetDto updateReservation(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException {
        findById(id);
        HotelReservation dbReservation = hotelReservationConverter.fromCreateDtoToEntity(hotelReservation);
        dbReservation.setId(id);
        setReservationProperties(dbReservation, hotelReservation);
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(dbReservation));
    }

    @Override
    public void delete(Long id) throws HotelReservationNotFoundException {
        HotelReservation hotelReservation = findById(id);
        hotelReservation.getRooms().forEach(roomInfo -> roomInfoService.delete(roomInfo.getId()));
        hotelReservationRepository.deleteById(id);
    }

    @Override
    public HotelReservation findById(Long id) throws HotelReservationNotFoundException {
        return hotelReservationRepository.findById(id).orElseThrow(() -> new HotelReservationNotFoundException(ID_NOT_FOUND + id));
    }

    private List<HotelReservation> sort(List<HotelReservation> hotelReservations, String sortBy) {
        //TODO adicionar sort
        return hotelReservations;
    }

    private void setReservationProperties(HotelReservation dbReservation, HotelReservationCreateDto dtoReservation) {
        dbReservation.setPricePerNight(calculatePricePerNight(dtoReservation.hotelInfo().roomInfo()));
        dbReservation.setDurationOfStay(dtoReservation.checkOutDate().getDayOfMonth() - dtoReservation.checkInDate().getDayOfMonth());
        dbReservation.setTotalPrice(dbReservation.getPricePerNight() * dbReservation.getDurationOfStay());
        dbReservation.setInvoice(invoiceService.findById(dtoReservation.invoiceId()));
        dbReservation.setRooms(saveRooms(dtoReservation.hotelInfo().roomInfo()));
    }

    private Set<RoomInfo> saveRooms(Set<ExternalRoomInfoDto> externalRoomInfoDtos) {
        Set<RoomInfo> rooms = roomInfoConverter.fromExternalDtoListToEntityList(externalRoomInfoDtos);
        rooms.forEach(roomInfoService::create);
        return rooms;
    }

    private int calculatePricePerNight(Set<ExternalRoomInfoDto> externalRoomInfoDtos) {
        return externalRoomInfoDtos.stream()
                .mapToInt(ExternalRoomInfoDto::pricePerNight)
                .sum();
    }

}