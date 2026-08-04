package com.carrental.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "bname", nullable = false, unique = true, length = 50)
    private String bname;
}
