-- =============================================================
-- car_rental_db — schema + sample data
-- =============================================================

CREATE DATABASE IF NOT EXISTS car_rental_db;
USE car_rental_db;

-- ─── 1. USERS ────────────────────────────────────────────────
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

-- ─── 2. CUSTOMER ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS customer (
    customer_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,
    address         TEXT,
    driving_license VARCHAR(50),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── 3. CAR_OWNER ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS car_owner (
    owner_id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL UNIQUE,
    address         TEXT,
    driving_license VARCHAR(50),
    status          ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ─── 4. VEHICLE ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicle (
    vehicle_id           INT AUTO_INCREMENT PRIMARY KEY,
    owner_id             INT NOT NULL,
    vehicle_name         VARCHAR(100),
    brand                VARCHAR(50),
    model                VARCHAR(50),
    registration_number  VARCHAR(20) NOT NULL UNIQUE,
    fuel_type            VARCHAR(20),
    seating_capacity     INT,
    rent_per_day         DECIMAL(10,2),
    status               ENUM('Available','Booked','Maintenance') DEFAULT 'Available',
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES car_owner(owner_id)
);

-- ─── 5. VEHICLE_AVAILABILITY ─────────────────────────────────
CREATE TABLE IF NOT EXISTS vehicle_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id      INT NOT NULL,
    available_from  DATE,
    available_to    DATE,
    status          ENUM('Available','Booked','Unavailable') DEFAULT 'Available',
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- ─── 6. BOOKING ──────────────────────────────────────────────
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
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (vehicle_id)  REFERENCES vehicle(vehicle_id)
);

-- ─── 7. PAYMENT ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payment (
    payment_id     INT AUTO_INCREMENT PRIMARY KEY,
    booking_id     INT NOT NULL,
    amount         DECIMAL(10,2) NOT NULL,
    payment_method ENUM('UPI','Credit Card','Debit Card','Net Banking','Cash'),
    payment_status ENUM('Pending','Paid','Failed','Refunded') DEFAULT 'Pending',
    payment_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id)
);

-- ─── 8. REFUND ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refund (
    refund_id     INT AUTO_INCREMENT PRIMARY KEY,
    payment_id    INT NOT NULL,
    refund_amount DECIMAL(10,2),
    refund_reason TEXT,
    refund_status ENUM('Pending','Processed','Rejected','Completed') DEFAULT 'Pending',
    refund_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);

-- =============================================================
-- SAMPLE DATA
-- =============================================================

-- ─── USERS (10 total: 5 customers, 3 owners, 2 admins) ───────
INSERT INTO users (id, name, email, phone, password, role, status, created_at) VALUES
(1,  'John Smith',   'john.smith@example.com',   '9876543210', '$2b$12$hashplaceholder0001', 'customer', 'active',        '2026-01-05 09:12:00'),
(2,  'Priya Sharma',  'priya.sharma@example.com', '9876543211', '$2b$12$hashplaceholder0002', 'customer', 'active',        '2026-01-10 14:30:00'),
(3,  'Amit Verma',    'amit.verma@example.com',   '9876543212', '$2b$12$hashplaceholder0003', 'customer', 'active',        '2026-01-15 11:05:00'),
(4,  'Sara Khan',     'sara.khan@example.com',    '9876543213', '$2b$12$hashplaceholder0004', 'customer', 'active',        '2026-02-01 16:45:00'),
(5,  'David Lee',     'david.lee@example.com',    '9876543214', '$2b$12$hashplaceholder0005', 'customer', 'blocked',       '2026-02-03 08:20:00'),
(6,  'Rohan Mehta',   'rohan.mehta@example.com',  '9876543215', '$2b$12$hashplaceholder0006', 'owner',    'active',        '2026-01-08 10:00:00'),
(7,  'Neha Gupta',    'neha.gupta@example.com',   '9876543216', '$2b$12$hashplaceholder0007', 'owner',    'active',        '2026-01-12 13:15:00'),
(8,  'Vikram Singh',  'vikram.singh@example.com', '9876543217', '$2b$12$hashplaceholder0008', 'owner',    'pending_admin', '2026-03-01 09:50:00'),
(9,  'Admin One',     'admin1@carrental.com',     '9876500001', '$2b$12$hashplaceholder0009', 'admin',    'active',        '2025-12-01 00:00:00'),
(10, 'Admin Two',     'admin2@carrental.com',     '9876500002', '$2b$12$hashplaceholder0010', 'admin',    'active',        '2025-12-01 00:00:00');

-- ─── CUSTOMER PROFILES ────────────────────────────────────────
INSERT INTO customer (customer_id, user_id, address, driving_license, created_at) VALUES
(1, 1, '221 MG Road, Pune, MH',        'MH14-2019-0012345', '2026-01-05 09:20:00'),
(2, 2, '45 Baner Road, Pune, MH',      'MH12-2020-0054321', '2026-01-10 14:40:00'),
(3, 3, '12 Camp Street, Pune, MH',     'MH14-2018-0098765', '2026-01-15 11:15:00'),
(4, 4, '78 Koregaon Park, Pune, MH',   'MH12-2021-0011223', '2026-02-01 16:50:00'),
(5, 5, '3 FC Road, Pune, MH',          'MH14-2017-0033445', '2026-02-03 08:30:00');

-- ─── CAR OWNER PROFILES ───────────────────────────────────────
INSERT INTO car_owner (owner_id, user_id, address, driving_license, status, created_at) VALUES
(1, 6, '9 Hinjewadi Phase 2, Pune, MH', 'MH14-2015-0055667', 'Approved', '2026-01-08 10:10:00'),
(2, 7, '22 Aundh, Pune, MH',            'MH12-2016-0077889', 'Approved', '2026-01-12 13:25:00'),
(3, 8, '5 Wakad, Pune, MH',             'MH14-2019-0099001', 'Pending',  '2026-03-01 10:00:00');

-- ─── VEHICLES ─────────────────────────────────────────────────
INSERT INTO vehicle (vehicle_id, owner_id, vehicle_name, brand, model, registration_number, fuel_type, seating_capacity, rent_per_day, status, created_at) VALUES
(1, 1, 'Swift Dzire',     'Maruti Suzuki', 'Dzire',    'MH14AB1234', 'Petrol', 5, 1500.00, 'Booked',    '2026-01-09 10:00:00'),
(2, 1, 'Creta',           'Hyundai',       'Creta',    'MH14CD5678', 'Diesel', 5, 2200.00, 'Available', '2026-01-09 10:05:00'),
(3, 2, 'City',            'Honda',        'City',      'MH12EF9012', 'Petrol', 5, 1800.00, 'Available', '2026-01-13 09:00:00'),
(4, 2, 'Fortuner',        'Toyota',       'Fortuner',  'MH12GH3456', 'Diesel', 7, 4500.00, 'Maintenance','2026-01-13 09:10:00'),
(5, 3, 'Nexon EV',        'Tata',         'Nexon EV',  'MH14IJ7890', 'Electric', 5, 2000.00, 'Available', '2026-03-02 11:00:00'),
(6, 3, 'Innova Crysta',   'Toyota',       'Innova',    'MH14KL1122', 'Diesel', 7, 3200.00, 'Booked',    '2026-03-02 11:10:00');

-- ─── VEHICLE AVAILABILITY ──────────────────────────────────────
INSERT INTO vehicle_availability (availability_id, vehicle_id, available_from, available_to, status) VALUES
(1, 1, '2026-07-01', '2026-07-05', 'Booked'),
(2, 1, '2026-07-06', '2026-08-31', 'Available'),
(3, 2, '2026-07-01', '2026-09-30', 'Available'),
(4, 3, '2026-07-01', '2026-09-30', 'Available'),
(5, 4, '2026-07-01', '2026-07-20', 'Unavailable'),
(6, 5, '2026-07-01', '2026-09-30', 'Available'),
(7, 6, '2026-07-15', '2026-07-22', 'Booked'),
(8, 6, '2026-07-23', '2026-09-30', 'Available');

-- ─── BOOKINGS ───────────────────────────────────────────────────
INSERT INTO booking (booking_id, customer_id, vehicle_id, pickup_date, return_date, total_days, total_amount, booking_status, booking_date) VALUES
(1, 1, 1, '2026-07-01', '2026-07-05', 4, 6000.00,  'Confirmed', '2026-06-25 12:00:00'),
(2, 2, 3, '2026-06-10', '2026-06-13', 3, 5400.00,  'Completed', '2026-06-05 09:30:00'),
(3, 3, 6, '2026-07-15', '2026-07-22', 7, 22400.00, 'Confirmed', '2026-07-10 15:20:00'),
(4, 4, 2, '2026-05-20', '2026-05-22', 2, 4400.00,  'Cancelled', '2026-05-15 10:00:00'),
(5, 1, 5, '2026-08-01', '2026-08-03', 2, 4000.00,  'Pending',   '2026-07-25 08:45:00'),
(6, 5, 4, '2026-04-10', '2026-04-12', 2, 9000.00,  'Cancelled', '2026-04-01 11:10:00'),
(7, 3, 3, '2026-03-15', '2026-03-18', 3, 5400.00,  'Completed', '2026-03-10 14:00:00'),
(8, 2, 1, '2026-09-01', '2026-09-04', 3, 4500.00,  'Pending',   '2026-07-28 17:30:00');

-- ─── PAYMENTS ────────────────────────────────────────────────────
INSERT INTO payment (payment_id, booking_id, amount, payment_method, payment_status, payment_date) VALUES
(1, 1, 6000.00,  'UPI',          'Paid',     '2026-06-25 12:05:00'),
(2, 2, 5400.00,  'Credit Card',  'Paid',     '2026-06-05 09:35:00'),
(3, 3, 22400.00, 'Net Banking',  'Paid',     '2026-07-10 15:25:00'),
(4, 4, 4400.00,  'Debit Card',   'Refunded', '2026-05-15 10:05:00'),
(5, 5, 4000.00,  'UPI',          'Pending',  '2026-07-25 08:50:00'),
(6, 6, 9000.00,  'Credit Card',  'Refunded', '2026-04-01 11:15:00'),
(7, 7, 5400.00,  'Cash',         'Paid',     '2026-03-10 14:05:00'),
(8, 8, 4500.00,  'UPI',          'Pending',  '2026-07-28 17:35:00');

-- ─── REFUNDS ─────────────────────────────────────────────────────
INSERT INTO refund (refund_id, payment_id, refund_amount, refund_reason, refund_status, refund_date) VALUES
(1, 4, 4400.00, 'Customer cancelled booking within free-cancellation window', 'Completed', '2026-05-16 09:00:00'),
(2, 6, 9000.00, 'Vehicle unavailable due to unscheduled maintenance',         'Completed', '2026-04-02 10:30:00');
