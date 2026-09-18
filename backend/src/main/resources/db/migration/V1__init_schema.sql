-- ==========================================================
-- V1__init_schema.sql
-- Initial Schema for Banking Application (MySQL)
-- ==========================================================

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
