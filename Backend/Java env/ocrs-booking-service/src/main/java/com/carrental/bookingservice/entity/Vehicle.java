package com.carrental.bookingservice.entity;

import com.carrental.bookingservice.entity.enums.FuelType;
import com.carrental.bookingservice.entity.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "model_id", nullable = false)
    private Integer modelId;

    @Column(name = "registration_number", nullable = false, unique = true, length = 20)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    @Column(name = "rent_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentPerDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private VehicleStatus status = VehicleStatus.Available;
}
