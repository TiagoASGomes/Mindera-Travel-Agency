package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("dev")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.deleted = false", nativeQuery = true)
    Page<User> findAllActive(Pageable page);
}
