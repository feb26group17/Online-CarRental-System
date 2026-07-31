package com.example.projectservicetwo.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;


    // Many Models belong to one Brand
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;


    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;


    @Column(name = "seating_capacity")
    private Integer seatingCapacity;


    // Default Constructor
    public Model() {
    }


    // Parameterized Constructor
    public Model(Brand brand, String modelName, Integer seatingCapacity) {
        this.brand = brand;
        this.modelName = modelName;
        this.seatingCapacity = seatingCapacity;
    }


    // Getters and Setters

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }


    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }
}
