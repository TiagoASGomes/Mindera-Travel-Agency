package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_reservations")
@Data
public class HotelReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int pricePerNight;
    private int durationOfStay;
    private int totalPrice;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private HotelInfo hotelInfo;
    @OneToOne(mappedBy = "hotelReservation",
            fetch = FetchType.EAGER)
    private Invoice invoice;
}
