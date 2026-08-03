package com.carrental.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Role-specific profile only — name, phone, address and adhar_card all
 * come from `users` now. This table just links a customer back to their
 * `users` row and holds the one customer-specific field, driving_license.
 */
@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    /** FK -> users.id (one-to-one). The master identity/auth record. */
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "driving_license", length = 50)
    private String drivingLicense;
}
