package com.carrental.crudservice.entity;

import com.carrental.crudservice.entity.converter.RoleConverter;
import com.carrental.crudservice.entity.converter.UserStatusConverter;
import com.carrental.crudservice.entity.enums.Role;
import com.carrental.crudservice.entity.enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Read/administer-only mirror of user-service's `users` table — this
 * service never creates users or checks passwords (that's user-service's
 * job), it only needs to list users and toggle status for the admin
 * screens, so the password column is intentionally omitted here.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, columnDefinition = "ENUM('customer','owner','admin')")
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false, columnDefinition = "ENUM('active','blocked')")
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "adhar_card", unique = true, length = 20)
    private String adharCard;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Integer id, String name, String email, String phone, Role role, String address, UserStatus status, String adharCard, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.status = status != null ? status : UserStatus.ACTIVE;
        this.adharCard = adharCard;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Integer id;
        private String name;
        private String email;
        private String phone;
        private Role role;
        private String address;
        private UserStatus status = UserStatus.ACTIVE;
        private String adharCard;
        private LocalDateTime createdAt;

        public UserBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public UserBuilder adharCard(String adharCard) {
            this.adharCard = adharCard;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public User build() {
            return new User(id, name, email, phone, role, address, status, adharCard, createdAt);
        }
    }
}
