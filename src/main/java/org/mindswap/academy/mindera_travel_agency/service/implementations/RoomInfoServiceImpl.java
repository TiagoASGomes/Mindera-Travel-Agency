package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.RoomInfoConverter;
import org.mindswap.academy.mindera_travel_agency.model.RoomInfo;
import org.mindswap.academy.mindera_travel_agency.repository.RoomInfoRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private RoomInfoConverter roomInfoConverter;

    @Override
    public void create(RoomInfo roomInfo) {

    }

    @Override
    public void delete(Long aLong) {

    }
}
