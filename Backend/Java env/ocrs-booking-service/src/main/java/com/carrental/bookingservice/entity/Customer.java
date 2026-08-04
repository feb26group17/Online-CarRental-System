package com.carrental.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "driving_license", length = 50)
    private String drivingLicense;
}
