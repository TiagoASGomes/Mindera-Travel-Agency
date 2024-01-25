package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "fare_classes")
@Data
public class FareClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    String className;
}
