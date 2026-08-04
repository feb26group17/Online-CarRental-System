USE car_rental_db;

-- ─── 1. SEED CAR BRANDS ───────────────────────────────────────────────────
INSERT IGNORE INTO brand (brand_id, bname) VALUES
(1, 'Toyota'),
(2, 'Hyundai'),
(3, 'Honda'),
(4, 'Tata Motors'),
(5, 'Mahindra'),
(6, 'Maruti Suzuki'),
(7, 'BMW'),
(8, 'Mercedes-Benz');

-- ─── 2. SEED CAR MODELS ───────────────────────────────────────────────────
INSERT IGNORE INTO model (model_id, brand_id, model_name, seating_capacity) VALUES
-- Toyota Models
(1, 1, 'Fortuner', 7),
(2, 1, 'Innova Crysta', 7),
(3, 1, 'Urban Cruiser', 5),
(4, 1, 'Camry', 5),

-- Hyundai Models
(5, 2, 'Creta', 5),
(6, 2, 'Verna', 5),
(7, 2, 'Tucson', 5),
(8, 2, 'i20', 5),

-- Honda Models
(9, 3, 'City', 5),
(10, 3, 'Civic', 5),
(11, 3, 'Amaze', 5),

-- Tata Motors Models
(12, 4, 'Harrier', 5),
(13, 4, 'Safari', 7),
(14, 4, 'Nexon', 5),
(15, 4, 'Altroz', 5),

-- Mahindra Models
(16, 5, 'Thar', 4),
(17, 5, 'XUV700', 7),
(18, 5, 'Scorpio-N', 7),
(19, 5, 'Bolero', 7),

-- Maruti Suzuki Models
(20, 6, 'Swift', 5),
(21, 6, 'Baleno', 5),
(22, 6, 'Ertiga', 7),
(23, 6, 'Brezza', 5),

-- BMW Models
(24, 7, 'X5', 5),
(25, 7, '3 Series', 5),

-- Mercedes-Benz Models
(26, 8, 'C-Class', 5),
(27, 8, 'GLE', 5);

-- ─── 3. SEED VEHICLES ─────────────────────────────────────────────────────
-- Ensure at least one owner account exists in `users` (id = 1)
INSERT IGNORE INTO users (id, name, email, phone, password, role, status, adhar_card)
VALUES (1, 'Admin Owner', 'admin@carrental.com', '9999999999', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lIy', 'admin', 'active', '123456789012');

INSERT IGNORE INTO vehicle (vehicle_id, user_id, model_id, registration_number, fuel_type, rent_per_day, status) VALUES
(1, 1, 1, 'MH-01-AX-1001', 'Diesel', 4500.00, 'Available'),
(2, 1, 2, 'MH-02-BY-2002', 'Diesel', 3800.00, 'Available'),
(3, 1, 5, 'MH-03-CZ-3003', 'Petrol', 2800.00, 'Available'),
(4, 1, 9, 'KA-01-MA-4004', 'Petrol', 2500.00, 'Available'),
(5, 1, 12, 'MH-12-PQ-5005', 'Diesel', 3500.00, 'Available'),
(6, 1, 16, 'MH-14-RS-6006', 'Diesel', 3200.00, 'Available'),
(7, 1, 17, 'KA-05-TU-7007', 'Diesel', 4200.00, 'Available'),
(8, 1, 20, 'DL-01-AB-8008', 'CNG', 1800.00, 'Available'),
(9, 1, 24, 'MH-02-VW-9009', 'Petrol', 8500.00, 'Available'),
(10, 1, 26, 'MH-01-XY-9999', 'Battery', 9500.00, 'Available');
