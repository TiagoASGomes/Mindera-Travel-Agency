package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "room_info")
@Data
public class RoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private String roomNumber;
    private int numberOfBeds;
    private int pricePerNight;
    @Column(unique = true)
    private Long externalId;
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private HotelReservation hotelReservation;
}
