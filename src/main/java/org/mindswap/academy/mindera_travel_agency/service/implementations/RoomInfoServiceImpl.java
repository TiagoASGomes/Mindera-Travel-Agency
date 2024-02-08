package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.RoomInfoRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.ROOM_NOT_FOUND;

/**
 * Implementation of the RoomInfoService interface.
 * Provides methods for creating, deleting, and checking the existence of room information.
 */
@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomInfoRepository roomInfoRepository;

    /**
     * Creates a new room information entry.
     *
     * @param roomInfo The room information to be created.
     */
    @Override
    public void create(RoomInfo roomInfo) {
        roomInfoRepository.save(roomInfo);
    }

    /**
     * Deletes a room information entry by its ID.
     *
     * @param id The ID of the room information to be deleted.
     */
    @Override
    public void delete(Long id) {
        Optional<RoomInfo> room = roomInfoRepository.findById(id);
        room.ifPresent(roomInfo -> roomInfoRepository.delete(roomInfo));
    }

    /**
     * Checks if a room information entry exists by its ID.
     * Throws a RoomNotFoundException if the room information does not exist.
     *
     * @param id The ID of the room information to check.
     * @throws RoomNotFoundException If the room information does not exist.
     */
    @Override
    public void existsById(Long id) throws RoomNotFoundException {
        if (!roomInfoRepository.existsById(id)) {
            throw new RoomNotFoundException(ROOM_NOT_FOUND + id);
        }
    }
}
