package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flights_tickets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fName;
    private String email;
    private String phone;
    @Column(unique = true)
    private Long ticketNumber;
    private String seatNumber;
    private int price;
    private int maxLuggageWeight;
    private boolean carryOnLuggage;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Invoice invoice;
    @ManyToOne(fetch = FetchType.EAGER)
    private FareClass fareClass;
}
