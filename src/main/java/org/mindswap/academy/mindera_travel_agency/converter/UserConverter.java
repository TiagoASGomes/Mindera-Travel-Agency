package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {
    public User fromUserCreateDtoToModel(UserCreateDto user) {
        return User.builder()
                .email(user.email())
                .password(user.password())
                .userName(user.userName())
                .dateOfBirth(user.dateOfBirth())
                .phoneNumber(user.phoneNumber())
                .build();
    }


    public UserGetDto fromUserModelToGetDto(User user) {
        return new UserGetDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getDateOfBirth(),
                user.getPhoneNumber()
        );
    }

    public List<UserGetDto> fromUserModelListToGetDto(List<User> users) {
        return users.stream()
                .map(this::fromUserModelToGetDto)
                .toList();
    }
}
