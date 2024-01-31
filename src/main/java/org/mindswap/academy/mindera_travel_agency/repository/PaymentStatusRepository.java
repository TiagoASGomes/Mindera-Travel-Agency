package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.PaymentStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("dev")
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByStatusName(String statusName);
}
