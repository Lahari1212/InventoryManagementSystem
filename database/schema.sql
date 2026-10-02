-- =====================================================================
-- Inventory Management System Database Schema
-- Database: SQLite
-- =====================================================================

-- Table: products
-- Notes on data types in SQLite:
--   - id: INTEGER PRIMARY KEY AUTOINCREMENT generates unique IDs automatically
--   - name & category: TEXT NOT NULL strings
--   - price: DECIMAL(10, 2) NOT NULL currency value
--   - quantity: INTEGER NOT NULL count of units in stock
--   - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP records creation time
CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Optional initial seed data for testing
INSERT INTO products (name, category, price, quantity) VALUES
('Wireless Mouse', 'Electronics', 25.99, 50),
('Mechanical Keyboard', 'Electronics', 79.50, 30),
('Desk Lamp', 'Home & Office', 19.99, 20),
('Ceramic Coffee Mug', 'Kitchen', 9.50, 100),
('Notebook A5', 'Stationery', 4.25, 150);
