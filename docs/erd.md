# Banking Application Entity-Relationship Diagram (ERD)

## 1. Relational Schema Diagram

```
 +-------------------------------------------------------+
 |                        users                          |
 +-------------------------------------------------------+
 | PK  id             BIGINT GENERATED ALWAYS AS IDENTITY|
 |     username       VARCHAR(50)  NOT NULL UNIQUE       |
 |     email          VARCHAR(100) NOT NULL UNIQUE       |
 |     password_hash  VARCHAR(255) NOT NULL              |
 |     full_name      VARCHAR(100) NOT NULL              |
 |     created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()|
 |     updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()|
 +-------------------------------------------------------+
                            │ 1
                            │
                            │ has many
                            ▼ *
 +-------------------------------------------------------+
 |                       accounts                        |
 +-------------------------------------------------------+
 | PK  id             BIGINT GENERATED ALWAYS AS IDENTITY|
 |     account_number VARCHAR(20)  NOT NULL UNIQUE       |
 |     account_type   VARCHAR(20)  NOT NULL              |
 |     balance        NUMERIC(19,4) NOT NULL DEFAULT 0.0 |
 |     currency       VARCHAR(3)   NOT NULL DEFAULT 'USD'|
 |     status         VARCHAR(20)  NOT NULL DEFAULT 'ACT'|
 | FK  user_id        BIGINT       NOT NULL              |
 |     created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()|
 |     updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()|
 +-------------------------------------------------------+
            │ 1                               │ 1
            │                                 │
            │ source (0..*)                   │ target (0..*)
            ▼                                 ▼
 +-------------------------------------------------------+
 |                     transactions                      |
 +-------------------------------------------------------+
 | PK  id                    BIGINT GENERATED IDENTITY   |
 |     transaction_reference VARCHAR(64) NOT NULL UNIQUE |
 | FK  source_account_id     BIGINT NULL                 |
 | FK  target_account_id     BIGINT NULL                 |
 |     amount                NUMERIC(19,4) NOT NULL      |
 |     transaction_type      VARCHAR(20) NOT NULL        |
 |     status                VARCHAR(20) NOT NULL        |
 |     description           VARCHAR(255) NULL           |
 |     timestamp             TIMESTAMPTZ NOT NULL DEFAULT|
 +-------------------------------------------------------+
```

---

## 2. Table Definitions & Constraints

### `users`
- Primary Key: `id` (identity)
- Unique Constraints: `uk_users_username` on `username`, `uk_users_email` on `email`

### `accounts`
- Primary Key: `id` (identity)
- Unique Constraints: `uk_accounts_account_number` on `account_number`
- Foreign Key: `fk_accounts_user` (`user_id` -> `users.id`) ON DELETE RESTRICT
- Check Constraint: `chk_account_balance_non_negative` (`balance >= 0`)
- Indexes: `idx_accounts_user_id`, `idx_accounts_account_number`

### `transactions`
- Primary Key: `id` (identity)
- Unique Constraints: `uk_transactions_reference` on `transaction_reference`
- Foreign Keys:
  - `fk_transactions_source` (`source_account_id` -> `accounts.id`) ON DELETE RESTRICT
  - `fk_transactions_target` (`target_account_id` -> `accounts.id`) ON DELETE RESTRICT
- Check Constraint: `chk_transaction_amount_positive` (`amount > 0`)
- Indexes:
  - `idx_transactions_source_account_id`
  - `idx_transactions_target_account_id`
  - `idx_transactions_timestamp` (DESC)
