package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.RoomInfoRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ROOM_NOT_FOUND;

@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomInfoRepository roomInfoRepository;

    @Override
    public void create(RoomInfo roomInfo) {
        roomInfoRepository.save(roomInfo);
    }

    @Override
    public void delete(Long id) {
        roomInfoRepository.deleteById(id);
    }

    @Override
    public void existsById(Long id) throws RoomNotFoundException {
        if (!roomInfoRepository.existsById(id)) {
            throw new RoomNotFoundException(ROOM_NOT_FOUND + id);
        }
    }
}
