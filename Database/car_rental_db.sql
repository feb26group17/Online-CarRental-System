CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- ─── 1. USERS ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    phone       VARCHAR(15),
    password    VARCHAR(255) NOT NULL,
    role        ENUM('customer', 'owner', 'admin') NOT NULL DEFAULT 'customer',
    address     TEXT,
    status      ENUM('active', 'blocked') NOT NULL DEFAULT 'active',
    adhar_card  VARCHAR(20) UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ─── 2. BRAND ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS brand (
    brand_id INT AUTO_INCREMENT PRIMARY KEY,
    bname    VARCHAR(50) NOT NULL UNIQUE
);

-- ─── 3. MODEL ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS model (
    model_id         INT AUTO_INCREMENT PRIMARY KEY,
    brand_id         INT NOT NULL,
    model_name       VARCHAR(50) NOT NULL,
    seating_capacity INT,
    FOREIGN KEY (brand_id) REFERENCES brand(brand_id)
);

-- ─── 4. VEHICLE ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT NOT NULL,
    model_id             INT NOT NULL,
    registration_number  VARCHAR(20) NOT NULL UNIQUE,
    fuel_type            ENUM('Diesel','Petrol','CNG','Battery'),
    rent_per_day         DECIMAL(10,2) NOT NULL,
    status               ENUM('Available','Booked','Maintenance') DEFAULT 'Available',
    FOREIGN KEY (user_id)  REFERENCES users(id),
    FOREIGN KEY (model_id) REFERENCES model(model_id)
);

-- ─── 5. CUSTOMER ──────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS customer (
    customer_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,
    driving_license VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── 6. BOOKING ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS booking (
    booking_id   INT AUTO_INCREMENT PRIMARY KEY,
    customer_id  INT NOT NULL,
    vehicle_id   INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pickup_date  DATE NOT NULL,
    return_date  DATE NOT NULL,
    status       ENUM('Pending','Confirmed','Cancelled','Completed') DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id)
);

-- ─── 7. PAYMENT ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payment (
    payment_id     INT AUTO_INCREMENT PRIMARY KEY,
    booking_id     INT NOT NULL,
    amt            DECIMAL(10,2) NOT NULL,
    payment_method ENUM('UPI','Credit Card','Debit Card','Net Banking','Cash'),
    payment_status ENUM('Pending','Paid','Failed','Refunded') DEFAULT 'Pending',
    payment_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);

-- ─── 8. REFUND ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refund (
    refund_id    INT AUTO_INCREMENT PRIMARY KEY,
    payment_id   INT NOT NULL,
    ref_amount   DECIMAL(10,2),
    reason       TEXT,
    refund_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);


ALTER TABLE booking
    ADD COLUMN drop_city VARCHAR(50);