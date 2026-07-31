package com.example.projectservicetwo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
public class Vehicle {

	public enum VehicleStatus {

		Available, Booked, Maintenance
	}

	public enum FuelType {

		Diesel, Petrol, CNG, Battery
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vehicle_id")
	private Integer vehicleId;

	// Many vehicles can belong to one owner(user)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User owner;

	// Many vehicles can have one model
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id", nullable = false)
	private Model model;

	@Column(name = "registration_number", nullable = false, unique = true, length = 20)
	private String registrationNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "fuel_type")
	private FuelType fuelType;

	@Column(name = "rent_per_day", nullable = false, precision = 10, scale = 2)
	private BigDecimal rentPerDay;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private VehicleStatus status = VehicleStatus.Available;

	// Default Constructor
	public Vehicle() {
	}

	// Parameterized Constructor
	public Vehicle(User owner, Model model, String registrationNumber, FuelType fuelType, BigDecimal rentPerDay) {

		this.owner = owner;
		this.model = model;
		this.registrationNumber = registrationNumber;
		this.fuelType = fuelType;
		this.rentPerDay = rentPerDay;
		this.status = VehicleStatus.Available;
	}

	// Set default status before saving
	@PrePersist
	public void prePersist() {

		if (this.status == null) {
			this.status = VehicleStatus.Available;
		}
	}

	// Getters and Setters

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
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