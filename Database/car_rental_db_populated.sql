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


-- ══════════════════════════════════════════════════════════════════════════
-- DUMMY DATA
-- Every password below is a REAL bcrypt hash (not a placeholder), so you can
-- actually log in with these accounts through the app / Postman:
--   Admin login   -> admin@carrental.com   / Admin@123
--   Everyone else -> <their email>         / test123
-- ══════════════════════════════════════════════════════════════════════════

-- ─── USERS ───────────────────────────────────────────────────────────────────
-- id 1: admin
-- id 2-4: customers (active)
-- id 5-6: owners still pending_admin approval
-- id 7: owner already approved
INSERT INTO users (name, email, phone, password, role, status) VALUES
('Admin',         'admin@carrental.com',    '9999999999', '$2b$10$V4Ew.jnIgYugaE1HiOxHIuPLak6QRuhItGXfrvUpJI1Kn.hruJbfW', 'admin',    'active'),
('Neha Verma',    'neha.verma@gmail.com',   '9812345670', '$2b$10$Zl8TU.8pKOjoI6k43oaskO0R5uy1FjzxTurz/PjD/eDIUnHS4x/ZK', 'customer', 'active'),
('Ashish Singh',  'ashish@gmail.com',       '9876543210', '$2b$10$5BTW28G98VYJzJ.4jWnIYOJaalr26b4d/4qgYoFto84XA8UvmAw6.', 'customer', 'active'),
('Srinivas',      'srinivas@gmail.com',     '9876543211', '$2b$10$s4TQeARUHZJMDEK/GfxqFePp4V/xFUSNJKMEE/pTAskW0VNyGmLWG', 'customer', 'active'),
('Vikram Rao',    'vikram.rao@gmail.com',   '9765432109', '$2b$10$vC6XanQtwgZCCvT9nOjNjeeU/5BWnBIv5uoSZaPd6G3t0luYLBCi6', 'owner',    'pending_admin'),
('Rahul Mehta',   'rahul@gmail.com',        '9876500000', '$2b$10$PnCLmYbXHOZWpQAGL8.joeu.5388EJXlXhP4fBj3dIfyE4Kdq5Szm', 'owner',    'pending_admin'),
('Arjun Kapoor',  'arjun.kapoor@gmail.com', '9988776655', '$2b$10$ErRInIEej.E2Qp1NChvC/OL8GTh6eRRcXdDua8Y4SAdBlRi6bdO4e', 'owner',    'active');

-- ─── CUSTOMER (linked to users 2, 3, 4) ─────────────────────────────────────
INSERT INTO customer (user_id, full_name, email, phone, address, driving_license, password) VALUES
(2, 'Neha Verma',   'neha.verma@gmail.com', '9812345670', '56 Church Street, Bengaluru',      'KA05-20220056789', '$2b$10$Zl8TU.8pKOjoI6k43oaskO0R5uy1FjzxTurz/PjD/eDIUnHS4x/ZK'),
(3, 'Ashish Singh', 'ashish@gmail.com',     '9876543210', '123 MG Road, Mumbai',               'MH12-20230012345', '$2b$10$5BTW28G98VYJzJ.4jWnIYOJaalr26b4d/4qgYoFto84XA8UvmAw6.'),
(4, 'Srinivas',     'srinivas@gmail.com',   '9876543211', '123 MG Road, Mumbai',               'MH12-20230012345', '$2b$10$s4TQeARUHZJMDEK/GfxqFePp4V/xFUSNJKMEE/pTAskW0VNyGmLWG');

-- ─── CAR_OWNER (linked to users 5, 6, 7) ────────────────────────────────────
INSERT INTO car_owner (user_id, full_name, email, phone, address, driving_license, password, status) VALUES
(5, 'Vikram Rao',   'vikram.rao@gmail.com',   '9765432109', '8 Residency Road, Hyderabad',   'TS09-20200043210', '$2b$10$vC6XanQtwgZCCvT9nOjNjeeU/5BWnBIv5uoSZaPd6G3t0luYLBCi6', 'Pending'),
(6, 'Rahul Mehta',  'rahul@gmail.com',        '9876500000', '45 Park Street, Kolkata',       'WB02-20220098765', '$2b$10$PnCLmYbXHOZWpQAGL8.joeu.5388EJXlXhP4fBj3dIfyE4Kdq5Szm', 'Pending'),
(7, 'Arjun Kapoor', 'arjun.kapoor@gmail.com', '9988776655', '14 Koregaon Park, Pune',        'MH12-20190087654', '$2b$10$ErRInIEej.E2Qp1NChvC/OL8GTh6eRRcXdDua8Y4SAdBlRi6bdO4e', 'Approved');

-- ─── VEHICLE (all owned by Arjun, owner_id 3, the only approved owner) ──────
INSERT INTO vehicle (owner_id, vehicle_name, brand, model, fuel_type, seating_capacity, rent_per_day, status) VALUES
(3, 'Swift',  'Maruti Suzuki', '2022', 'Petrol', 5, 1500.00, 'Available'),
(3, 'Creta',  'Hyundai',       '2023', 'Diesel', 5, 2200.00, 'Booked'),
(3, 'Innova', 'Toyota',        '2021', 'Diesel', 7, 2800.00, 'Available');

-- ─── VEHICLE_AVAILABILITY ────────────────────────────────────────────────────
INSERT INTO vehicle_availability (vehicle_id, available_from, available_to, status) VALUES
(1, '2026-08-01', '2026-08-31', 'Available'),
(2, '2026-07-20', '2026-07-30', 'Booked'),
(3, '2026-08-01', '2026-09-01', 'Available');

-- ─── BOOKING ─────────────────────────────────────────────────────────────────
-- booking 1: Neha books the Creta - confirmed, paid
-- booking 2: Ashish books the Swift - cancelled, refunded
INSERT INTO booking (customer_id, vehicle_id, pickup_date, return_date, total_days, total_amount, booking_status) VALUES
(1, 2, '2026-07-25', '2026-07-28', 3, 6600.00, 'Confirmed'),
(2, 1, '2026-08-05', '2026-08-07', 2, 3000.00, 'Cancelled');

-- ─── PAYMENT ─────────────────────────────────────────────────────────────────
INSERT INTO payment (booking_id, amount, payment_method, payment_status) VALUES
(1, 6600.00, 'UPI',          'Paid'),
(2, 3000.00, 'Credit Card',  'Refunded');

-- ─── REFUND (for the cancelled booking's payment) ───────────────────────────
INSERT INTO refund (payment_id, refund_amount, refund_reason, refund_status) VALUES
(2, 3000.00, 'Customer changed travel plans', 'Completed');
