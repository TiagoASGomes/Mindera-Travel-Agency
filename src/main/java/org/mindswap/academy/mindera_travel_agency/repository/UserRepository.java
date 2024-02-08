package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface represents the repository for managing User entities.
 * It extends the JpaRepository interface to inherit basic CRUD operations.
 * The repository is specific to the "dev" profile.
 */
@Repository
@Profile("dev")
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user
     * @return an Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all active users.
     *
     * @param page the pageable object for pagination
     * @return a Page containing the active users
     */

    @Query(value = "SELECT * FROM users u WHERE u.deleted = false", nativeQuery = true)
    Page<User> findAllActive(Pageable page);
}
