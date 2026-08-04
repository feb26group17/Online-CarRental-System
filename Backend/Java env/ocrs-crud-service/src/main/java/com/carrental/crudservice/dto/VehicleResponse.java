package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.FuelType;
import com.carrental.crudservice.entity.enums.VehicleStatus;

import java.math.BigDecimal;

public class VehicleResponse {
    private Integer vehicleId;
    private Integer userId;
    private Integer modelId;
    private String modelName;
    private String brandName;
    private Integer seatingCapacity;
    private String registrationNumber;
    private FuelType fuelType;
    private BigDecimal rentPerDay;
    private VehicleStatus status;

    public VehicleResponse() {
    }

    public VehicleResponse(Integer vehicleId, Integer userId, Integer modelId, String modelName, String brandName, Integer seatingCapacity, String registrationNumber, FuelType fuelType, BigDecimal rentPerDay, VehicleStatus status) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.modelId = modelId;
        this.modelName = modelName;
        this.brandName = brandName;
        this.seatingCapacity = seatingCapacity;
        this.registrationNumber = registrationNumber;
        this.fuelType = fuelType;
        this.rentPerDay = rentPerDay;
        this.status = status;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
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

    public static VehicleResponseBuilder builder() {
        return new VehicleResponseBuilder();
    }

    public static class VehicleResponseBuilder {
        private Integer vehicleId;
        private Integer userId;
        private Integer modelId;
        private String modelName;
        private String brandName;
        private Integer seatingCapacity;
        private String registrationNumber;
        private FuelType fuelType;
        private BigDecimal rentPerDay;
        private VehicleStatus status;

        public VehicleResponseBuilder vehicleId(Integer vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public VehicleResponseBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public VehicleResponseBuilder modelId(Integer modelId) {
            this.modelId = modelId;
            return this;
        }

        public VehicleResponseBuilder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public VehicleResponseBuilder brandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public VehicleResponseBuilder seatingCapacity(Integer seatingCapacity) {
            this.seatingCapacity = seatingCapacity;
            return this;
        }

        public VehicleResponseBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public VehicleResponseBuilder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public VehicleResponseBuilder rentPerDay(BigDecimal rentPerDay) {
            this.rentPerDay = rentPerDay;
            return this;
        }

        public VehicleResponseBuilder status(VehicleStatus status) {
            this.status = status;
            return this;
        }

        public VehicleResponse build() {
            return new VehicleResponse(vehicleId, userId, modelId, modelName, brandName, seatingCapacity, registrationNumber, fuelType, rentPerDay, status);
        }
    }
}
