package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.exception.hotel_reservation.RoomNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;

/**
 * This interface represents the service for managing room information.
 */
public interface RoomInfoService {
    /**
     * Creates a new room information entry.
     *
     * @param roomInfo The room information to be created.
     */
    void create(RoomInfo roomInfo);

    /**
     * Deletes a room information entry by its ID.
     *
     * @param id The ID of the room information to be deleted.
     */
    void delete(Long id);

    /**
     * Checks if a room information entry exists by its ID.
     *
     * @param id The ID of the room information to check.
     * @throws RoomNotFoundException If the room information is not found.
     */
    void existsById(Long id) throws RoomNotFoundException;
}
