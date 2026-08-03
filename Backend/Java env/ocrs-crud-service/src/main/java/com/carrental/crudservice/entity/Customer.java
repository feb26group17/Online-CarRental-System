package com.carrental.crudservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "driving_license", length = 50)
    private String drivingLicense;

    public Customer() {
    }

    public Customer(Integer customerId, Integer userId, String drivingLicense) {
        this.customerId = customerId;
        this.userId = userId;
        this.drivingLicense = drivingLicense;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private Integer customerId;
        private Integer userId;
        private String drivingLicense;

        public CustomerBuilder customerId(Integer customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public CustomerBuilder drivingLicense(String drivingLicense) {
            this.drivingLicense = drivingLicense;
            return this;
        }

        public Customer build() {
            return new Customer(customerId, userId, drivingLicense);
        }
    }
}
