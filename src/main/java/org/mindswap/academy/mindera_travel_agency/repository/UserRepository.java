package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(boolean username);


}
