package com.carrental.userservice.entity;

import com.carrental.userservice.entity.enums.OwnerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Integer ownerId;

    /** FK -> users.id (one-to-one). The master identity/auth record. */
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "driving_license", length = 50)
    private String drivingLicense;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('Pending','Approved','Rejected')")
    @Builder.Default
    private OwnerStatus status = OwnerStatus.Pending;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
