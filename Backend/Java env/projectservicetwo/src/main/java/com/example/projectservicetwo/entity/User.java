package com.example.projectservicetwo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(length = 15)
	private String phone;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false)
	private String role = "customer";

	@Column(columnDefinition = "TEXT")
	private String address;

	@Column(nullable = false)
	private String status = "active";

	@Column(name = "adhar_card", unique = true, length = 20)
	private String adharCard;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	// Default Constructor
	public User() {
	}

	// Parameterized Constructor
	public User(Integer id, String name, String email, String phone, String password, String role, String address,
			String status, String adharCard, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.address = address;
		this.status = status;
		this.adharCard = adharCard;
		this.createdAt = createdAt;
	}

	// Getters

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public String getAddress() {
		return address;
	}

	public String getStatus() {
		return status;
	}

	public String getAdharCard() {
		return adharCard;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// Setters

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAdharCard(String adharCard) {
		this.adharCard = adharCard;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	// toString()

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", phone='" + phone + '\''
				+ ", password='" + password + '\'' + ", role='" + role + '\'' + ", address='" + address + '\''
				+ ", status='" + status + '\'' + ", adharCard='" + adharCard + '\'' + ", createdAt=" + createdAt + '}';
	}
}