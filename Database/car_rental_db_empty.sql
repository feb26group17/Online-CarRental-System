CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- ─── 1. USERS (master identity/auth table) ──────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(15),
    password   VARCHAR(255) NOT NULL,
    role       ENUM('customer', 'owner', 'admin') NOT NULL DEFAULT 'customer',
    status     ENUM('active', 'pending_admin', 'blocked') NOT NULL DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ─── 2. CUSTOMER (profile table, linked to users) ───────────────────────────
CREATE TABLE IF NOT EXISTS customer (
    customer_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,        -- FK → users.id (one-to-one)
    full_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    phone           VARCHAR(15),
    address         TEXT,
    driving_license VARCHAR(50),
    password        VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── 3. CAR_OWNER (profile table, linked to users) ──────────────────────────
CREATE TABLE IF NOT EXISTS car_owner (
    owner_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,        -- FK → users.id (one-to-one)
    full_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    phone           VARCHAR(15),
    address         TEXT,
    driving_license VARCHAR(50),
    password        VARCHAR(255) NOT NULL,
    status          ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── 4. VEHICLE ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id       INT AUTO_INCREMENT PRIMARY KEY,
    owner_id         INT NOT NULL,
    vehicle_name     VARCHAR(100),
    brand            VARCHAR(50),
    model            VARCHAR(50),
    fuel_type        VARCHAR(20),
    seating_capacity INT,
    rent_per_day     DECIMAL(10,2),
    status           ENUM('Available','Booked','Maintenance') DEFAULT 'Available',
    FOREIGN KEY (owner_id) REFERENCES car_owner(owner_id)
);

-- ─── 5. VEHICLE_AVAILABILITY ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicle_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id      INT NOT NULL,
    available_from  DATE,
    available_to    DATE,
    status          ENUM('Available','Booked','Unavailable') DEFAULT 'Available',
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- ─── 6. BOOKING ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS booking (
    booking_id     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id    INT NOT NULL,
    vehicle_id     INT NOT NULL,
    pickup_date    DATE NOT NULL,
    return_date    DATE NOT NULL,
    total_days     INT,
    total_amount   DECIMAL(10,2),
    booking_status ENUM('Pending','Confirmed','Cancelled','Completed') DEFAULT 'Pending',
    booking_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id)
);

-- ─── 7. PAYMENT ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payment (
    payment_id     INT AUTO_INCREMENT PRIMARY KEY,
    booking_id     INT NOT NULL,
    amount         DECIMAL(10,2) NOT NULL,
    payment_method ENUM('UPI','Credit Card','Debit Card','Net Banking','Cash'),
    payment_status ENUM('Pending','Paid','Failed','Refunded') DEFAULT 'Pending',
    payment_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);

-- ─── 8. REFUND ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refund (
    refund_id     INT AUTO_INCREMENT PRIMARY KEY,
    payment_id    INT NOT NULL,
    refund_amount DECIMAL(10,2),
    refund_reason TEXT,
    refund_status ENUM('Pending','Processed','Rejected','Completed') DEFAULT 'Pending',
    refund_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);
