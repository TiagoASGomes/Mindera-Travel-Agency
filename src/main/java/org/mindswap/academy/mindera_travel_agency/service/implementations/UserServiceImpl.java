package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserGetDto add(UserCreateDto user) throws EmailNotFoundException {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new EmailNotFoundException(EMAIL_NOT_FOUND);
        }
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }

    @Override
    public List<UserGetDto> getAll() {
        return userConverter.fromUserModelListToGetDto(userRepository.findAll());
    }

    @Override
    public UserGetDto update(long id, UserCreateDto user) throws UserNotFoundException, EmailNotFoundException {
        User newUser = findById(id);
        if (userRepository.findByEmail(user.email()).isPresent() && !newUser.getEmail().equals(user.email())) {
            throw new EmailNotFoundException(EMAIL_ALREADY_EXISTS);
        }
        newUser.setEmail(user.email());
        newUser.setPassword(user.password());
        newUser.setUserName(user.userName());
        newUser.setDateOfBirth(user.dateOfBirth());
        newUser.setPhoneNumber(user.phoneNumber());
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }


    @Override
    public UserGetDto put(long id, UserCreateDto user) throws UserNotFoundException {
        findById(id);
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        newUser.setId(id);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
    }


    @Override
    public void delete(long id, UserCreateDto user) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserGetDto getById(long id) throws UserNotFoundException {
        return userConverter.fromUserModelToGetDto(findById(id));
    }


}
