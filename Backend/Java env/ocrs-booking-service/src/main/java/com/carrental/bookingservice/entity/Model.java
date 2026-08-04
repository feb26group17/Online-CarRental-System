package com.carrental.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "brand_id", nullable = false)
    private Integer brandId;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;
}
