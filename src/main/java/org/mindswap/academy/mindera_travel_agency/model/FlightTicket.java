package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight_tickets")
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fName;
    private String email;
    private String phone;
    private Long ticketNumber;
    private Long priceId;
    private Long flightId;
    private float duration;
    private String seatNumber;
    private int price;
    private String fareClass;
    private int maxLuggageWeight;
    private boolean carryOnLuggage;
    @ManyToOne
    private Invoice invoice;
}

