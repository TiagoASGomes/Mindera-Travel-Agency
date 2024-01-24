package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.model.User;

import java.util.List;

public interface UserService {
    void add(UserCreateDto user);

    List<UserGetDto> getAll();

    void update(long id, @Valid UserCreateDto user);

    void put(long id, @Valid UserCreateDto user);

    User findById(long id);

    void delete(long id, @Valid UserCreateDto user);
}
