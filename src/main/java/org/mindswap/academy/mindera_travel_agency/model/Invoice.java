package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalPrice;
    private LocalDateTime paymentDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private PaymentStatus paymentStatus;
    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "invoice",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private HotelReservation hotelReservation;
    @OneToMany(mappedBy = "invoice",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER)
    private Set<FlightTicket> flightTickets;

    public Invoice(User user) {
        this.user = user;
    }
}
