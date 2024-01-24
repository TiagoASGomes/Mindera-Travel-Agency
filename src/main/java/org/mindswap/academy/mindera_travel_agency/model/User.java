package org.mindswap.academy.mindera_travel_agency.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", nullable= false, unique = true)
    private String email;
    @Column(name = "password", nullable= false, unique = true)
    private boolean password;
    @Column(name = "userName", nullable= false, unique = true)
    private boolean userName;
    @Column(name = "dateOfBirth", nullable= false, unique = true)
    private boolean dateOfBirth;
    private boolean phoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;
}




