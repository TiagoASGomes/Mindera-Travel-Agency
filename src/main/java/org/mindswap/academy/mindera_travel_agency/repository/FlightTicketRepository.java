package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing FlightTicket entities.
 */
@Repository
@Profile("dev")
public interface FlightTicketRepository extends JpaRepository<FlightTicket, Long> {
    /**
     * Retrieves an optional FlightTicket by its ticket number.
     *
     * @param ticketNumber the ticket number to search for
     * @return an optional FlightTicket object
     */
    Optional<FlightTicket> findByTicketNumber(Long ticketNumber);

    /**
     * Retrieves a list of FlightTickets by the invoice ID.
     *
     * @param invoiceId the invoice ID to search for
     * @return a list of FlightTicket objects
     */
    @Query(name = "SELECT ft FROM flights_tickets ft where ft.invoice_id = ?1", nativeQuery = true)
    List<FlightTicket> findAllByInvoiceId(Long invoiceId);
}
