package com.example.projectservicetwo.dto;

import com.example.projectservicetwo.entity.Vehicle.FuelType;
import com.example.projectservicetwo.entity.Vehicle.VehicleStatus;

import java.math.BigDecimal;

public class VehicleResponseDTO {

    private Integer vehicleId;
    private UserResponseDTO owner;
    private Integer modelId;
    private String registrationNumber;
    private FuelType fuelType;
    private BigDecimal rentPerDay;
    private VehicleStatus status;

    public VehicleResponseDTO() {
    }

    public VehicleResponseDTO(Integer vehicleId, UserResponseDTO owner, Integer modelId,
                              String registrationNumber, FuelType fuelType,
                              BigDecimal rentPerDay, VehicleStatus status) {
        this.vehicleId = vehicleId;
        this.owner = owner;
        this.modelId = modelId;
        this.registrationNumber = registrationNumber;
        this.fuelType = fuelType;
        this.rentPerDay = rentPerDay;
        this.status = status;
    }

    // Getters and Setters
    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public UserResponseDTO getOwner() {
        return owner;
    }

    public void setOwner(UserResponseDTO owner) {
        this.owner = owner;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public BigDecimal getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(BigDecimal rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}