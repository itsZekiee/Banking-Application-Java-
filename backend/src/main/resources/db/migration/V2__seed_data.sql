-- ==========================================================
-- V2__seed_data.sql
-- Seed Initial Data for Banking Application
-- ==========================================================

-- 1. Insert Initial Users
INSERT INTO users (id, username, email, password_hash, full_name, created_at, updated_at)
VALUES
    (1, 'johndoe', 'john@example.com', 'Password123', 'John Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'janesmith', 'jane@example.com', 'Password123', 'Jane Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- 2. Insert Initial Accounts
INSERT INTO accounts (id, account_number, account_type, balance, currency, status, user_id, created_at, updated_at)
VALUES
    (1, '1234567890', 'SAVINGS', 1500.0000, 'USD', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '9876543210', 'CHECKING', 3200.5000, 'USD', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '5555666677', 'CHECKING', 850.0000, 'USD', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- 3. Insert Initial Transactions
INSERT INTO transactions (id, transaction_reference, source_account_id, target_account_id, amount, transaction_type, status, description, timestamp)
VALUES
    (1, 'TXN-INIT-001', NULL, 1, 1000.0000, 'DEPOSIT', 'SUCCESS', 'Initial savings deposit', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (2, 'TXN-INIT-002', NULL, 2, 3200.5000, 'DEPOSIT', 'SUCCESS', 'Opening balance deposit', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (3, 'TXN-INIT-003', NULL, 1, 500.0000, 'DEPOSIT', 'SUCCESS', 'Payroll direct deposit', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Update Identity Sequences
SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1), true);
SELECT setval(pg_get_serial_sequence('accounts', 'id'), COALESCE((SELECT MAX(id) FROM accounts), 1), true);
SELECT setval(pg_get_serial_sequence('transactions', 'id'), COALESCE((SELECT MAX(id) FROM transactions), 1), true);
