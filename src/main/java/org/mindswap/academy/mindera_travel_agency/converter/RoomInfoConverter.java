package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalCreateRoomReservation;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The RoomInfoConverter class is responsible for converting between different representations of RoomInfo objects.
 * It provides methods to convert from external DTOs to entities, from entities to DTOs, and from lists of entities to lists of DTOs.
 */
@Component
public class RoomInfoConverter {
    /**
     * Converts an ExternalRoomInfoDto object to a RoomInfo entity.
     *
     * @param room the ExternalRoomInfoDto object to convert
     * @return the converted RoomInfo entity
     */
    public RoomInfo fromExternalDtoToEntity(ExternalRoomInfoDto room) {
        return RoomInfo.builder()
                .pricePerNight(room.roomPrice())
                .roomType(room.roomType())
                .numberOfBeds(room.numberOfBeds())
                .roomNumber(room.roomNumber())
                .build();
    }

    /**
     * Converts a Set of RoomInfo entities to a List of RoomInfoGetDto objects.
     *
     * @param roomInfos the Set of RoomInfo entities to convert
     * @return the converted List of RoomInfoGetDto objects
     */
    public List<RoomInfoGetDto> fromEntityListToGetDtoList(Set<RoomInfo> roomInfos) {
        if (roomInfos == null) return new ArrayList<>();
        return roomInfos.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    /**
     * Converts a RoomInfo entity to a RoomInfoGetDto object.
     *
     * @param roomInfo the RoomInfo entity to convert
     * @return the converted RoomInfoGetDto object
     */

    public RoomInfoGetDto fromEntityToGetDto(RoomInfo roomInfo) {
        return new RoomInfoGetDto(
                roomInfo.getId(),
                roomInfo.getRoomType(),
                roomInfo.getRoomNumber(),
                roomInfo.getNumberOfBeds(),
                roomInfo.getPricePerNight()
        );
    }

    /**
     * Converts a collection of ExternalRoomInfoDto objects to a Set of RoomInfo entities.
     *
     * @param externalRoomInfoDtos the collection of ExternalRoomInfoDto objects to convert
     * @return the converted Set of RoomInfo entities
     */
    public Set<RoomInfo> fromExternalDtoListToEntityList(Collection<ExternalRoomInfoDto> externalRoomInfoDtos) {
        return externalRoomInfoDtos.stream()
                .map(this::fromExternalDtoToEntity)
                .collect(Collectors.toSet());
    }

    /**
     * Converts a Set of RoomInfo entities to a List of ExternalCreateRoomReservation objects.
     *
     * @param rooms the Set of RoomInfo entities to convert
     * @return the converted List of ExternalCreateRoomReservation objects
     */
    public List<ExternalCreateRoomReservation> fromEntityListToExternalCreateRoomReservationList(Set<RoomInfo> rooms) {
        return rooms.stream()
                .map(this::fromEntityToExternalCreateRoomReservation)
                .toList();
    }

    /**
     * Converts a RoomInfo entity to an ExternalCreateRoomReservation object.
     *
     * @param room the RoomInfo entity to convert
     * @return the converted ExternalCreateRoomReservation object
     */

    public ExternalCreateRoomReservation fromEntityToExternalCreateRoomReservation(RoomInfo room) {
        return new ExternalCreateRoomReservation(
                room.getRoomType()
        );
    }
}
