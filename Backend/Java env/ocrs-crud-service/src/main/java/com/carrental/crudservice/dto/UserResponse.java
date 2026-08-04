package com.carrental.crudservice.dto;

import com.carrental.crudservice.entity.enums.Role;
import com.carrental.crudservice.entity.enums.UserStatus;

import java.time.LocalDateTime;

public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private UserStatus status;
    private String address;
    private String adharCard;
    private String drivingLicense;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(Integer id, String name, String email, String phone, Role role, UserStatus status, String address, String adharCard, String drivingLicense, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.address = address;
        this.adharCard = adharCard;
        this.drivingLicense = drivingLicense;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private Integer id;
        private String name;
        private String email;
        private String phone;
        private Role role;
        private UserStatus status;
        private String address;
        private String adharCard;
        private String drivingLicense;
        private LocalDateTime createdAt;

        public UserResponseBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserResponseBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserResponseBuilder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public UserResponseBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserResponseBuilder adharCard(String adharCard) {
            this.adharCard = adharCard;
            return this;
        }

        public UserResponseBuilder drivingLicense(String drivingLicense) {
            this.drivingLicense = drivingLicense;
            return this;
        }

        public UserResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(id, name, email, phone, role, status, address, adharCard, drivingLicense, createdAt);
        }
    }
}
