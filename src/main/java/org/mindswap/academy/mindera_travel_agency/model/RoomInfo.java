package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_info")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private int roomNumber;
    private int numberOfBeds;
    private int pricePerNight;
    private Long externalId;
    @ManyToOne(fetch = FetchType.EAGER)
    private HotelReservation hotelReservation;
}
