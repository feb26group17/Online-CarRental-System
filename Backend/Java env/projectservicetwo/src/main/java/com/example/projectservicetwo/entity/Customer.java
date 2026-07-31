package com.example.projectservicetwo.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "driving_license", length = 50)
    private String drivingLicense;

    // Default Constructor
    public Customer() {
    }

    // Parameterized Constructor
    public Customer(Integer customerId, User user, String drivingLicense) {
        this.customerId = customerId;
        this.user = user;
        this.drivingLicense = drivingLicense;
    }

    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", user=" + user +
                ", drivingLicense='" + drivingLicense + '\'' +
                '}';
    }
}
