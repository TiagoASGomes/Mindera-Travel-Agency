package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fare_classes")
@Data
@NoArgsConstructor
public class FareClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String className;
    @OneToMany(mappedBy = "fareClass")
    private FlightTicket flightTicket;

    public FareClass(String className) {
        this.className = className;
    }
}
