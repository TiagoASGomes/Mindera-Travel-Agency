package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.model.User;

import java.util.List;

public class UserConverter {
    public static User fromUserCreateDtoToModel(UserCreateDto user) {
        return User.builder()
                .email(user.email())
                .password(user.password())
                .userName(user.userName())
                .dateOfBirth(user.dateOfBirth())
                .phoneNumber(user.phoneNumber())
                .build();
    }


    public static UserGetDto fromUserModelToDto(User user) {
        return new UserGetDto(
                user.getuserName(),
                user.getEmail(),
                user.getPassword(),
                user.getId(),
                user.getPhoneNumber(),
                user.getDateOfBirth();

    }

    public static List<UserGetDto> fromUserModelToDto(List<User> users) {
        return users.stream().map(UserConverter::fromUserModelToDto).toList();
    }
}
