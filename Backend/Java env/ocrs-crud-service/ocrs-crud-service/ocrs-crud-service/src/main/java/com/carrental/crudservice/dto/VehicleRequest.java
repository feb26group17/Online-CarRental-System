package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.FuelType;
import com.carrental.crudservice.entity.enums.VehicleStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class VehicleRequest {

    @NotNull(message = "modelId is required")
    private Integer modelId;

    @NotNull(message = "registrationNumber is required")
    private String registrationNumber;

    @NotNull(message = "fuelType is required")
    private FuelType fuelType;

    @NotNull(message = "rentPerDay is required")
    private BigDecimal rentPerDay;

    private VehicleStatus status;

    public VehicleRequest() {
    }

    public VehicleRequest(Integer modelId, String registrationNumber, FuelType fuelType, BigDecimal rentPerDay, VehicleStatus status) {
        this.modelId = modelId;
        this.registrationNumber = registrationNumber;
        this.fuelType = fuelType;
        this.rentPerDay = rentPerDay;
        this.status = status;
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

    public static VehicleRequestBuilder builder() {
        return new VehicleRequestBuilder();
    }

    public static class VehicleRequestBuilder {
        private Integer modelId;
        private String registrationNumber;
        private FuelType fuelType;
        private BigDecimal rentPerDay;
        private VehicleStatus status;

        public VehicleRequestBuilder modelId(Integer modelId) {
            this.modelId = modelId;
            return this;
        }

        public VehicleRequestBuilder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public VehicleRequestBuilder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public VehicleRequestBuilder rentPerDay(BigDecimal rentPerDay) {
            this.rentPerDay = rentPerDay;
            return this;
        }

        public VehicleRequestBuilder status(VehicleStatus status) {
            this.status = status;
            return this;
        }

        public VehicleRequest build() {
            return new VehicleRequest(modelId, registrationNumber, fuelType, rentPerDay, status);
        }
    }
}
