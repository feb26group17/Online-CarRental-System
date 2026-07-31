package com.example.projectservicetwo.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "bname", nullable = false, unique = true, length = 50)
    private String bname;

    // Default Constructor
    public Brand() {
    }

    // Parameterized Constructor
    public Brand(Integer brandId, String bname) {
        this.brandId = brandId;
        this.bname = bname;
    }

    // Getters and Setters
    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "brandId=" + brandId +
                ", bname='" + bname + '\'' +
                '}';
    }
}