package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;
import org.mindswap.academy.mindera_travel_agency.util.enums.FareClass;

@Entity
@Table(name = "flights_tickets")
@Data
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fName;
    private String email;
    private String phone;
    private Long ticketNumber;
    private String seatNumber;
    private int price;
    private FareClass fareClass;
    private int maxLuggageWeight;
    private boolean carryOnLuggage;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Invoice invoice;
}
