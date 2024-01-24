package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;

public interface RoomInfoService {
    void create(RoomInfo roomInfo);

    void delete(Long aLong);
}
