package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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
    private String hotelName;
    private String hotelAddress;
    private String hotelPhoneNumber;
    private int pricePerNight;
    private int durationOfStay;
    private int totalPrice;
    private LocalDate arrivalDate;
    private LocalDate leaveDate;
    @OneToMany(mappedBy = "hotelReservation",
            cascade = CascadeType.ALL)
    private Set<RoomInfo> rooms;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Invoice invoice;

    public void addRoom(RoomInfo roomInfo) {
        if (rooms == null) {
            rooms = new HashSet<>();
        }
        rooms.add(roomInfo);
        roomInfo.setHotelReservation(this);

    }

    public void removeRoom(Long roomId) {
        if (rooms == null) {
            return;
        }
        rooms.removeIf(roomInfo -> Objects.equals(roomInfo.getId(), roomId));
    }


}
