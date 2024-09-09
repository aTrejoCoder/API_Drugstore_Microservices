CREATE TABLE inventories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    batch_number VARCHAR(255),
    quantity INT,
    expiration_date DATE,
    location VARCHAR(255),
    date_received DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    product_id INT
);

CREATE TABLE inventories_transaction (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    transaction_type VARCHAR(255),
    quantity INT,
    date DATE,
    created_at TIMESTAMP,
    inventory_id BIGINT,
    employee_id BIGINT,
    FOREIGN KEY (inventory_id) REFERENCES inventories(id) ON DELETE CASCADE
);


INSERT INTO inventories (batch_number, quantity, expiration_date, location, date_received, created_at, updated_at, product_id) VALUES
('BATCH001', 100, '2025-12-31', 'Warehouse A', '2024-08-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
('BATCH002', 150, '2025-11-15', 'Warehouse B', '2024-08-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
('BATCH003', 200, '2024-10-30', 'Warehouse C', '2024-08-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
('BATCH004', 250, '2024-12-31', 'Warehouse A', '2024-08-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
('BATCH005', 300, '2025-01-15', 'Warehouse B', '2024-08-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
('BATCH006', 120, '2024-09-30', 'Warehouse C', '2024-08-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6),
('BATCH007', 180, '2025-07-31', 'Warehouse A', '2024-09-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7),
('BATCH008', 220, '2025-05-31', 'Warehouse B', '2024-09-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8),
('BATCH009', 170, '2024-11-30', 'Warehouse C', '2024-09-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9),
('BATCH010', 130, '2024-10-15', 'Warehouse A', '2024-09-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10),
('BATCH011', 160, '2025-02-28', 'Warehouse B', '2024-09-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 11),
('BATCH012', 190, '2024-12-01', 'Warehouse C', '2024-09-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 12),
('BATCH013', 140, '2024-10-31', 'Warehouse A', '2024-10-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13),
('BATCH014', 210, '2025-03-31', 'Warehouse B', '2024-10-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 14),
('BATCH015', 250, '2025-04-30', 'Warehouse C', '2024-10-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 15),
('BATCH016', 280, '2024-11-15', 'Warehouse A', '2024-10-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 16),
('BATCH017', 300, '2025-06-30', 'Warehouse B', '2024-10-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 17),
('BATCH018', 170, '2025-08-31', 'Warehouse C', '2024-10-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 18),
('BATCH019', 190, '2024-12-15', 'Warehouse A', '2024-11-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 19),
('BATCH020', 220, '2025-01-31', 'Warehouse B', '2024-11-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 20);


INSERT INTO inventories_transaction (transaction_type, quantity, date, created_at, inventory_id, employee_id) VALUES
('IN', 50, '2024-08-02', CURRENT_TIMESTAMP, 1, 1),
('OUT', 30, '2024-08-05', CURRENT_TIMESTAMP, 2, 2),
('IN', 80, '2024-08-10', CURRENT_TIMESTAMP, 3, 3),
('OUT', 20, '2024-08-15', CURRENT_TIMESTAMP, 4, 4),
('IN', 100, '2024-08-20', CURRENT_TIMESTAMP, 5, 5),
('OUT', 60, '2024-08-25', CURRENT_TIMESTAMP, 6, 6),
('IN', 70, '2024-09-01', CURRENT_TIMESTAMP, 7, 7),
('OUT', 40, '2024-09-05', CURRENT_TIMESTAMP, 8, 8),
('IN', 90, '2024-09-10', CURRENT_TIMESTAMP, 9, 9),
('OUT', 50, '2024-09-15', CURRENT_TIMESTAMP, 10, 10),
('IN', 110, '2024-09-20', CURRENT_TIMESTAMP, 11, 11),
('OUT', 80, '2024-09-25', CURRENT_TIMESTAMP, 12, 12),
('IN', 120, '2024-10-01', CURRENT_TIMESTAMP, 13, 13),
('OUT', 70, '2024-10-05', CURRENT_TIMESTAMP, 14, 14),
('IN', 130, '2024-10-10', CURRENT_TIMESTAMP, 15, 15),
('OUT', 60, '2024-10-15', CURRENT_TIMESTAMP, 16, 16),
('IN', 140, '2024-10-20', CURRENT_TIMESTAMP, 17, 17),
('OUT', 90, '2024-10-25', CURRENT_TIMESTAMP, 18, 18),
('IN', 150, '2024-11-01', CURRENT_TIMESTAMP, 19, 19),
('OUT', 80, '2024-11-05', CURRENT_TIMESTAMP, 20, 20);
