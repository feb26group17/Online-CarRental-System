package com.carrental.crudservice.dto;

public class UserProfileUpdateRequest {
    private String name;
    private String phone;
    private String address;
    private String adharCard;
    private String drivingLicense;

    public UserProfileUpdateRequest() {
    }

    public UserProfileUpdateRequest(String name, String phone, String address, String adharCard, String drivingLicense) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.adharCard = adharCard;
        this.drivingLicense = drivingLicense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
