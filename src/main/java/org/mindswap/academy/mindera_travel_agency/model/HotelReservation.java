package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotel_reservations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private int hotelPhoneNumber;
    private int pricePerNight;
    private int durationOfStay;
    private int totalPrice;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    @OneToMany(mappedBy = "hotelReservation",
            fetch = FetchType.EAGER)
    private Set<RoomInfo> rooms;
    @OneToOne(fetch = FetchType.EAGER)
    private Invoice invoice;

    public void addRoom(RoomInfo roomInfo) {
        if (rooms == null) {
            rooms = new HashSet<>();
        }
        rooms.add(roomInfo);
        roomInfo.setHotelReservation(this);

    }

    public void removeRoom(Long roomInfoId) {
        if (rooms == null) {
            return;
        }
        rooms.removeIf(roomInfo -> roomInfo.getExternalId().equals(roomInfoId));
    }
}
