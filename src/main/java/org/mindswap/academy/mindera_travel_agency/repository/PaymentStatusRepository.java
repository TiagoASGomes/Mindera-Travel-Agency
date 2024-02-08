package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface represents the repository for managing PaymentStatus entities.
 * It extends the JpaRepository interface to inherit basic CRUD operations.
 */
@Repository
@Profile("dev")
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    /**
     * Finds a PaymentStatus entity by its status name.
     *
     * @param statusName the status name to search for
     * @return an Optional containing the found PaymentStatus entity, or an empty Optional if not found
     */
    Optional<PaymentStatus> findByStatusName(String statusName);
}
