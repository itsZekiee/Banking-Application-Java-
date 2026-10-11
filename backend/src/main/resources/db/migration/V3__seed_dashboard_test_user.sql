-- Test credentials for dashboard verification.
-- The account intentionally has no transaction rows.

INSERT IGNORE INTO users (id, username, email, password_hash, full_name, created_at, updated_at)
VALUES
    (3, 'user_test01', 'user01@gmail.com', 'user_test01', 'User Test01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO accounts (id, account_number, account_type, balance, currency, status, user_id, created_at, updated_at)
VALUES
    (4, '1000000003', 'CHECKING', 10000.0000, 'PHP', 'ACTIVE', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
