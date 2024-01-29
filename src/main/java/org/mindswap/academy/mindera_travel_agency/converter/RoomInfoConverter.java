package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoomInfoConverter {
    public RoomInfo fromExternalDtoToEntity(ExternalRoomInfoDto room) {
        return RoomInfo.builder()
                .externalId(room.externalId())
                .pricePerNight(room.pricePerNight())
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

    public Set<RoomInfo> fromExternalDtoListToEntityList(Set<ExternalRoomInfoDto> externalRoomInfoDtos) {
        return externalRoomInfoDtos.stream()
                .map(this::fromExternalDtoToEntity)
                .collect(Collectors.toSet());
    }
}
