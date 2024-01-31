package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("dev")
public interface FlightTicketRepository extends JpaRepository<FlightTicket, Long> {
    Optional<FlightTicket> findByTicketNumber(Long ticketNumber);

    @Query(name = "SELECT ft FROM flights_tickets ft where ft.invoice_id = ?1", nativeQuery = true)
    List<FlightTicket> findAllByInvoiceId(Long invoiceId);
}
