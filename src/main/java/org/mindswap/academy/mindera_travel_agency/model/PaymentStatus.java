package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "payment_status")
@Data
@NoArgsConstructor
public class PaymentStatus {
    //TODO add unique constraint and check in service
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String statusName;
    @OneToMany(mappedBy = "paymentStatus")
    private Set<Invoice> invoices;

    public PaymentStatus(String statusName) {
        this.statusName = statusName;
    }
}
