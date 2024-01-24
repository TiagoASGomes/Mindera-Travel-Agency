package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserCreateDto user) {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        if (userRepository.findByUsername(user.userName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        userRepository.save(UserConverter.fromUserCreateDtoToModel(user));
    }

    @Override
    public List<UserGetDto> getAll() {
        return UserConverter.fromUserModelToDto(userRepository.findAll());
    }

    @Override
    public void update(long id, UserCreateDto user) {
        User dbUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (userRepository.findByEmail(user.email()).isPresent() && !dbUser.getEmail().equals(user.email())) {
            throw new RuntimeException("Email already exists");
        }
    }

    @Override
    public void put(long id, UserCreateDto user) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        User newUser = UserConverter.fromUserCreateDtoToModel(user);
        newUser.setId(id);
        userRepository.save(newUser);
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void delete(long id, UserCreateDto user) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }
}
