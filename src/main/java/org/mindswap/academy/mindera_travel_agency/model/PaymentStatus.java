package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "payment_status")
@Data
public class PaymentStatus {
    //TODO add unique constraint and check in service
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statusName;
    @OneToMany(mappedBy = "paymentStatus")
    private Set<Invoice> invoices;
}
