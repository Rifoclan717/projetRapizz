-- =========================
-- INGREDIENTS (PIZZA ONLY)
-- =========================
INSERT INTO Ingredients (name) VALUES
('Tomato sauce'),
('Mozzarella'),
('Ham'),
('Mushrooms'),
('Pepperoni'),
('Olives'),
('Onions'),
('Chicken'),
('Basil'),
('Parmesan');

-- =========================
-- PRODUCTS (PIZZAS ONLY)
-- =========================
INSERT INTO Products (name, basePrice) VALUES
('Margherita', 7.50),
('Pepperoni', 9.00),
('Hawaiian', 9.50),
('Chicken BBQ', 10.50),
('Four Cheese', 9.80);

-- =========================
-- PRODUCT_INGREDIENT
-- =========================

-- Margherita
INSERT INTO ProductIngredient VALUES
(1,1),(2,1),(9,1);

-- Pepperoni
INSERT INTO ProductIngredient VALUES
(1,2),(2,2),(5,2);

-- Hawaiian
INSERT INTO ProductIngredient VALUES
(1,3),(2,3),(3,3);

-- Chicken BBQ (approx BBQ vibe with chicken + onion + cheese base)
INSERT INTO ProductIngredient VALUES
(1,4),(2,4),(8,4),(7,4);

-- Four Cheese
INSERT INTO ProductIngredient VALUES
(1,5),(2,5),(10,5),(9,5),(6,5);

-- =========================
-- CLIENTS
-- =========================
INSERT INTO Clients (email, phoneNumber, firstName, lastName, address, password_hash) VALUES
('alice@example.com', '+33611111111', 'Alice', 'Martin', '10 Rue Paris', 'hash1'),
('bob@example.com', '+33622222222', 'Bob', 'Dupont', '20 Avenue Lyon', 'hash2'),
('carol@example.com', '+33633333333', 'Carol', 'Bernard', '5 Rue Lille', 'hash3');

-- =========================
-- DRIVERS
-- =========================
INSERT INTO Drivers (firstName, lastName, canBike, canDrive) VALUES
('David', 'Lopez', true, false),
('Emma', 'Durand', false, true),
('Lucas', 'Moreau', true, true);

-- =========================
-- VEHICLES
-- =========================
INSERT INTO Vehicles (plateNumber, brand, model, type) VALUES
('BIKE-001', 'Decathlon', 'Rockrider', 'bike'),
('CAR-001', 'Toyota', 'Yaris', 'car'),
('CAR-002', 'Renault', 'Clio', 'car');

-- =========================
-- ORDERS
-- =========================
INSERT INTO Orders (deliveryTime, orderDate, tenthOfGift, clientId, driverId, vehicleId) VALUES
('2026-05-28 12:30:00', '2026-05-28 12:00:00', false, 1, 1, 1),
('2026-05-28 13:10:00', '2026-05-28 12:40:00', true, 2, 2, 2),
('2026-05-28 14:00:00', '2026-05-28 13:20:00', false, 3, 3, 3),
('2026-05-28 18:30:00', '2026-05-28 18:00:00', false, 1, 3, 2);

-- =========================
-- ORDER PRODUCT (PIZZAS ONLY)
-- =========================
INSERT INTO OrderProduct (orderId, productId, quantity) VALUES
(1, 1, 2), -- Margherita
(1, 2, 1), -- Pepperoni

(2, 3, 1), -- Hawaiian
(2, 4, 1), -- Chicken BBQ

(3, 2, 2), -- Pepperoni
(3, 5, 1), -- Four Cheese

(4, 1, 1), -- Margherita
(4, 4, 2); -- Chicken BBQ