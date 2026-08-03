package com.carrental.crudservice.entity;

import com.carrental.crudservice.entity.enums.FuelType;
import com.carrental.crudservice.entity.enums.VehicleStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
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
    private VehicleStatus status = VehicleStatus.Available;

    public Vehicle() {
    }

    public Vehicle(Integer vehicleId, Integer userId, Integer modelId, String registrationNumber, FuelType fuelType, BigDecimal rentPerDay, VehicleStatus status) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.modelId = modelId;
        this.registrationNumber = registrationNumber;
        this.fuelType = fuelType;
        this.rentPerDay = rentPerDay;
        this.status = status != null ? status : VehicleStatus.Available;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public static VehicleBuilder builder() {
        return new VehicleBuilder();
    }

    public static class VehicleBuilder {
        private Integer vehicleId;
        private Integer userId;
        private Integer modelId;
        private String registrationNumber;
        private FuelType fuelType;
        private BigDecimal rentPerDay;
        private VehicleStatus status = VehicleStatus.Available;

        public VehicleBuilder vehicleId(Integer vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public VehicleBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public VehicleBuilder modelId(Integer modelId) {
            this.modelId = modelId;
            return this;
        }

        public VehicleBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public VehicleBuilder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public VehicleBuilder rentPerDay(BigDecimal rentPerDay) {
            this.rentPerDay = rentPerDay;
            return this;
        }

        public VehicleBuilder status(VehicleStatus status) {
            this.status = status;
            return this;
        }

        public Vehicle build() {
            return new Vehicle(vehicleId, userId, modelId, registrationNumber, fuelType, rentPerDay, status);
        }
    }
}
