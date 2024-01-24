package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "hotel_info")
@Data
public class HotelInfo {
    // TODO: verificar propriedades com o external API
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String rating;
    private Long externalId;
    @OneToMany(mappedBy = "hotelInfo")
    private Set<RoomInfo> roomInfo;
    @OneToOne(mappedBy = "hotelInfo")
    private HotelReservation hotelReservation;


}
