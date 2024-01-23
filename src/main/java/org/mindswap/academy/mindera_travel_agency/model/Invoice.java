package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private User user;
    @OneToOne
    private Hotel hotel;
    @OneToMany(mappedBy = "invoice")
    private Set<FlightTicket> flightTickets;
}
