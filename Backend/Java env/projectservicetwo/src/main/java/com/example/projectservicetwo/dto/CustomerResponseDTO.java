package com.example.projectservicetwo.dto;

public class CustomerResponseDTO {

    private Integer customerId;
    private String drivingLicense;
    private UserResponseDTO user;

    public CustomerResponseDTO() {
    }

    public CustomerResponseDTO(Integer customerId, String drivingLicense, UserResponseDTO user) {
        this.customerId = customerId;
        this.drivingLicense = drivingLicense;
        this.user = user;
    }

    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}