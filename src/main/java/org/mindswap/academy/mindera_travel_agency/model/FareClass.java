package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private Set<FlightTicket> flightTicket;

    public FareClass(String className) {
        this.className = className;
    }
}
