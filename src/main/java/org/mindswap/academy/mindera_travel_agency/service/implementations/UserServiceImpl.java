package org.mindswap.academy.mindera_travel_agency.service.implementations;

import jakarta.transaction.Transactional;
import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.mindswap.academy.mindera_travel_agency.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.mindswap.academy.mindera_travel_agency.util.Message.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserCreateDto user) throws EmailNotFoundException {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new EmailNotFoundException(EMAIL_NOT_FOUND);
        }
        userRepository.save(UserConverter.fromUserCreateDtoToModel(user));
    }

    @Override
    public List<UserGetDto> getAll() {
        return UserConverter.fromUserModelToDto(userRepository.findAll());
    }

    @Override
    public void update(long id, UserCreateDto user) throws UserNotFoundException, UserNotFoundException, EmailNotFoundException {
        User newUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
        if (userRepository.findByEmail(user.email()).isPresent() && !newUser.getEmail().equals(user.email())) {
            throw new EmailNotFoundException(EMAIL_ALREADY_EXISTS);
        }
        userRepository.save(newUser);
    }


    @Override
    public void put(long id, UserCreateDto user) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
        User newUser = UserConverter.fromUserCreateDtoToModel(user);
        newUser.setId(id);
        userRepository.save(newUser);
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
    }


    @Override
    public void delete(long id, UserCreateDto user) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
        userRepository.deleteById(id);
    }
}
