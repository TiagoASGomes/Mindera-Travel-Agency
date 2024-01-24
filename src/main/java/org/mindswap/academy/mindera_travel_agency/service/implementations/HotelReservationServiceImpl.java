package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationDurationDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationRoomsDto;
import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.HotelReservationNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.repository.HotelReservationRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.HotelReservationService;
import org.mindswap.academy.mindera_travel_agency.util.enums.SortParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ID_NOT_FOUND;

@Service
public class HotelReservationServiceImpl implements HotelReservationService {

    @Autowired
    private HotelReservationRepository hotelReservationRepository;

    @Autowired
    private HotelReservationConverter hotelReservationConverter;

    @Override
    public List<HotelReservationGetDto> getAll() {
        return hotelReservationConverter.fromEntityListToGetDtoList(hotelReservationRepository.findAll());
    }

    @Override
    public HotelReservationGetDto getById(Long id) throws HotelReservationNotFoundException {
        return hotelReservationConverter.fromEntityToGetDto(findById(id));
    }

    @Override
    public List<HotelReservationGetDto> getAllByUser(SortParameter sortBy, Long userId) {
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll().stream()
                .filter(hotelReservation -> hotelReservation.getInvoice().getUser().getId().equals(userId))
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(sort(hotelReservations, sortBy));
    }

    @Override
    public List<HotelReservationGetDto> getAllByUserAndByName(String hotelName, SortParameter sortBy, Long userId) {
        List<HotelReservation> hotelReservations = hotelReservationRepository.findAll().stream()
                .filter(hotelReservation -> hotelReservation.getHotelInfo().getName().contains(hotelName))
                .filter(hotelReservation -> hotelReservation.getInvoice().getUser().getId().equals(userId))
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(sort(hotelReservations, sortBy));
    }

    @Override
    public HotelReservationGetDto create(HotelReservationCreateDto hotelReservation) {
        HotelReservation hotelReservationToSave = hotelReservationConverter.fromCreateDtoToEntity(hotelReservation);
        //TODO add logic to calculate price from rooms and duration
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToSave));
    }

    @Override
    public HotelReservationGetDto updateDuration(Long id, HotelReservationDurationDto hotelReservation) throws HotelReservationNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        //TODO change duration and calculate price
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToUpdate));
    }

    @Override
    public HotelReservationGetDto updateRooms(Long id, HotelReservationRoomsDto hotelReservation) throws HotelReservationNotFoundException {
        HotelReservation hotelReservationToUpdate = findById(id);
        //TODO change rooms and calculate price
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToUpdate));
    }

    @Override
    public HotelReservationGetDto updateHotel(Long id, HotelReservationCreateDto hotelReservation) throws HotelReservationNotFoundException {
        findById(id);
        HotelReservation hotelReservationToUpdate = hotelReservationConverter.fromCreateDtoToEntity(hotelReservation);
        hotelReservationToUpdate.setId(id);
        return hotelReservationConverter.fromEntityToGetDto(hotelReservationRepository.save(hotelReservationToUpdate));
    }

    @Override
    public void delete(Long id) throws HotelReservationNotFoundException {
        findById(id);
        hotelReservationRepository.deleteById(id);
    }

    @Override
    public HotelReservation findById(Long id) throws HotelReservationNotFoundException {
        return hotelReservationRepository.findById(id).orElseThrow(() -> new HotelReservationNotFoundException(ID_NOT_FOUND + id));
    }

    private List<HotelReservation> sort(List<HotelReservation> hotelReservations, SortParameter sortBy) {
        return hotelReservations;
    }

}
