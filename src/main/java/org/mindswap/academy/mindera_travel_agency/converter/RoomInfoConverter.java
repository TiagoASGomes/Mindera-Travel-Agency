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

@Component
public class RoomInfoConverter {
    public RoomInfo fromExternalDtoToEntity(ExternalRoomInfoDto room) {
        return RoomInfo.builder()
                .pricePerNight(room.roomPrice())
                .roomType(room.roomType())
                .numberOfBeds(room.numberOfBeds())
                .roomNumber(room.roomNumber())
                .build();
    }

    public List<RoomInfoGetDto> fromEntityListToGetDtoList(Set<RoomInfo> roomInfos) {
        if (roomInfos == null) return new ArrayList<>();
        return roomInfos.stream()
                .map(this::fromEntityToGetDto)
                .toList();
    }

    public RoomInfoGetDto fromEntityToGetDto(RoomInfo roomInfo) {
        return new RoomInfoGetDto(
                roomInfo.getId(),
                roomInfo.getRoomType(),
                roomInfo.getRoomNumber(),
                roomInfo.getNumberOfBeds(),
                roomInfo.getPricePerNight()
        );
    }

    public Set<RoomInfo> fromExternalDtoListToEntityList(Collection<ExternalRoomInfoDto> externalRoomInfoDtos) {
        return externalRoomInfoDtos.stream()
                .map(this::fromExternalDtoToEntity)
                .collect(Collectors.toSet());
    }

    public List<ExternalCreateRoomReservation> fromEntityListToExternalCreateRoomReservationList(Set<RoomInfo> rooms) {
        return rooms.stream()
                .map(this::fromEntityToExternalCreateRoomReservation)
                .toList();
    }

    public ExternalCreateRoomReservation fromEntityToExternalCreateRoomReservation(RoomInfo room) {
        return new ExternalCreateRoomReservation(
                room.getRoomType()
        );
    }
}
