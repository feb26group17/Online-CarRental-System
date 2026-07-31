package com.example.projectservicetwo.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
	
	public enum PaymentMethod {

	    UPI,
	    CREDIT_CARD,
	    DEBIT_CARD,
	    NET_BANKING,
	    CASH
	}

	
	public enum PaymentStatus {

	    Pending,
	    Paid,
	    Failed,
	    Refunded
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;


    // Many payments belong to one booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;


    @Column(name = "amt", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.Pending;


    @Column(name = "payment_date", updatable = false)
    private LocalDateTime paymentDate;


    // Default Constructor
    public Payment() {
    }


    // Parameterized Constructor
    public Payment(Booking booking, BigDecimal amount,
                   PaymentMethod paymentMethod) {

        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.Pending;
        this.paymentDate = LocalDateTime.now();
    }


    // Automatically set payment date before saving
    @PrePersist
    public void prePersist() {

        this.paymentDate = LocalDateTime.now();

        if(this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.Pending;
        }
    }


    // Getters and Setters

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }


    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}