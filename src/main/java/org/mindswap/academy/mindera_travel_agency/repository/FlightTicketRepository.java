package org.mindswap.academy.mindera_travel_agency.repository;

import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightTicketRepository extends JpaRepository<FlightTicket, Long> {
    Optional<FlightTicket> findByTicketNumber(Long ticketNumber);

    @Query("SELECT ft FROM flights_tickets ft\n" +
            "inner join invoices i ON i.id = ft.invoice_id \n" +
            "inner join users u ON u.id = i.user_id \n" +
            "where u.id = ?1")
    List<FlightTicket> findAllByUserId(Long userId);

    List<FlightTicket> findAllByInvoiceId(Long invoiceId);
}
