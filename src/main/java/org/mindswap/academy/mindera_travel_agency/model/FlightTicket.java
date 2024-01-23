package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "flights_tickets")
public class FlightTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<Invoice> invoices;
}
