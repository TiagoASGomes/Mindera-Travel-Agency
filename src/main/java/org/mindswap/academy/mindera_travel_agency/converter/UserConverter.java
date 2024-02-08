package org.mindswap.academy.mindera_travel_agency.converter;

import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The UserConverter class is responsible for converting User objects between different representations.
 * It provides methods to convert UserCreateDto to User model, User model to UserGetDto, and List of User models to List of UserGetDto.
 */
@Component
public class UserConverter {
    /**
     * Converts a UserCreateDto object to a User model object.
     *
     * @param user the UserCreateDto object to be converted
     * @return the converted User model object
     */
    public User fromUserCreateDtoToModel(UserCreateDto user) {
        return User.builder()
                .email(user.email())
                .password(user.password())
                .userName(user.userName())
                .dateOfBirth(user.dateOfBirth())
                .phoneNumber(user.phoneNumber())
                .vat(user.vat())
                .build();
    }

    /**
     * Converts a User model object to a UserGetDto object.
     *
     * @param user the User model object to be converted
     * @return the converted UserGetDto object
     */
    public UserGetDto fromUserModelToGetDto(User user) {
        return new UserGetDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getDateOfBirth(),
                user.getPhoneNumber()
        );
    }

    /**
     * Converts a List of User model objects to a List of UserGetDto objects.
     *
     * @param users the List of User model objects to be converted
     * @return the converted List of UserGetDto objects
     */
    public List<UserGetDto> fromUserModelListToGetDto(List<User> users) {
        return users.stream()
                .map(this::fromUserModelToGetDto)
                .toList();
    }


}
