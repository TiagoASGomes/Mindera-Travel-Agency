package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "room_info")
@Data
public class RoomInfo {
    // TODO: verificar propriedades com o external API
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int pricePerNight;
    private int capacity;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private HotelInfo hotelInfo;
}
