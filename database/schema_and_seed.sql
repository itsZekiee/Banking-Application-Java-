-- ==========================================================
-- Banking Application - Database Schema & Seed Data
-- Compatible with MySQL 8.0+
-- Database: pbc_db
-- ==========================================================

CREATE DATABASE IF NOT EXISTS pbc_db;
USE pbc_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_accounts_user_id (user_id),
    INDEX idx_accounts_account_number (account_number),
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    CONSTRAINT chk_account_balance_non_negative CHECK (balance >= 0)
);

-- 3. Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_reference VARCHAR(64) NOT NULL UNIQUE,
    source_account_id BIGINT,
    target_account_id BIGINT,
    amount DECIMAL(19, 4) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    description VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_transactions_source_account_id (source_account_id),
    INDEX idx_transactions_target_account_id (target_account_id),
    INDEX idx_transactions_timestamp (timestamp DESC),
    CONSTRAINT fk_transactions_source FOREIGN KEY (source_account_id) REFERENCES accounts (id) ON DELETE RESTRICT,
    CONSTRAINT fk_transactions_target FOREIGN KEY (target_account_id) REFERENCES accounts (id) ON DELETE RESTRICT,
    CONSTRAINT chk_transaction_amount_positive CHECK (amount > 0)
);

-- ==========================================================
-- 4. Initial Seed Data
-- ==========================================================

-- Insert Initial Users
INSERT IGNORE INTO users (id, username, email, password_hash, full_name, created_at, updated_at)
VALUES
    (1, 'johndoe', 'john@example.com', 'Password123', 'John Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'janesmith', 'jane@example.com', 'Password123', 'Jane Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Initial Accounts
INSERT IGNORE INTO accounts (id, account_number, account_type, balance, currency, status, user_id, created_at, updated_at)
VALUES
    (1, '1234567890', 'SAVINGS', 1500.0000, 'USD', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '9876543210', 'CHECKING', 3200.5000, 'USD', 'ACTIVE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '5555666677', 'CHECKING', 850.0000, 'USD', 'ACTIVE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Initial Transactions
INSERT IGNORE INTO transactions (id, transaction_reference, source_account_id, target_account_id, amount, transaction_type, status, description, timestamp)
VALUES
    (1, 'TXN-INIT-001', NULL, 1, 1000.0000, 'DEPOSIT', 'SUCCESS', 'Initial savings deposit', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
    (2, 'TXN-INIT-002', NULL, 2, 3200.5000, 'DEPOSIT', 'SUCCESS', 'Opening balance deposit', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)),
    (3, 'TXN-INIT-003', NULL, 1, 500.0000, 'DEPOSIT', 'SUCCESS', 'Payroll direct deposit', CURRENT_TIMESTAMP);
