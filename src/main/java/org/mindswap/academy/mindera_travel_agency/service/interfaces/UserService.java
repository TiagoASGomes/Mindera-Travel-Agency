package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;

import java.util.List;

public interface UserService {

    void add(UserCreateDto user) throws EmailNotFoundException;

    List<UserGetDto> getAll();

    void update(long id, @Valid UserCreateDto user) throws UserNotFoundException, EmailNotFoundException;

    void put(long id, @Valid UserCreateDto user) throws UserNotFoundException;

    User getById(long id) throws UserNotFoundException;

    void delete(long id, @Valid UserCreateDto user) throws UserNotFoundException;
}
