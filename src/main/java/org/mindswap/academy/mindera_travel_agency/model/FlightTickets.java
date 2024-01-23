package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;

@Entity
@Table(name = "flights_tickets")
public class FlightTickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
