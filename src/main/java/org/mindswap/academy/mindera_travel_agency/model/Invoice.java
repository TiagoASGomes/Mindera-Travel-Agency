package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalPrice;
    private LocalDateTime paymentDate;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private PaymentStatus paymentStatus;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private User user;
    @OneToOne(mappedBy = "invoice")
    private HotelReservation hotelReservation;
    @OneToMany(mappedBy = "invoice")
    private Set<FlightTicket> flightTickets;
}
