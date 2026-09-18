# Banking Application REST API Specification

**Base URL**: `/api/v1`

All responses follow a standard envelope:
```json
{
  "success": true,
  "message": "Operation description",
  "data": { ... },
  "timestamp": "2026-09-18T17:35:00Z"
}
```

Error responses:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation or business rule error message",
  "details": ["field: reason"],
  "timestamp": "2026-09-18T17:35:00Z"
}
```

---

## 1. Authentication & Users

### `POST /api/v1/auth/login`
- **Description**: Authenticate user credentials.
- **Request Body**:
```json
{
  "usernameOrEmail": "johndoe",
  "password": "Password123"
}
```
- **Response**: `200 OK`
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "mock-jwt-token-...",
    "tokenType": "Bearer",
    "userId": 1,
    "username": "johndoe",
    "email": "john@example.com"
  }
}
```

### `POST /api/v1/users`
- **Description**: Register a new user.
- **Request Body**:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "Password123",
  "fullName": "John Doe"
}
```
- **Response**: `201 Created`

### `GET /api/v1/users/{id}`
- **Description**: Fetch user details by User ID.
- **Response**: `200 OK`

### `GET /api/v1/users/username/{username}`
- **Description**: Fetch user details by username.
- **Response**: `200 OK`

---

## 2. Accounts

### `POST /api/v1/accounts`
- **Description**: Create a new bank account for a user.
- **Request Body**:
```json
{
  "userId": 1,
  "accountType": "SAVINGS",
  "initialDeposit": 100.00,
  "currency": "USD"
}
```
- **Response**: `201 Created`

### `GET /api/v1/accounts/{accountNumber}`
- **Description**: Fetch account details by account number.
- **Response**: `200 OK`

### `GET /api/v1/accounts/user/{userId}`
- **Description**: Fetch all accounts belonging to a user.
- **Response**: `200 OK`

### `GET /api/v1/accounts/{accountNumber}/balance`
- **Description**: Retrieve current balance of an account.
- **Response**: `200 OK`

---

## 3. Transactions

### `POST /api/v1/transactions/deposit`
- **Description**: Deposit money into an account.
- **Request Body**:
```json
{
  "accountNumber": "1234567890",
  "amount": 250.00,
  "description": "Salary check deposit"
}
```
- **Response**: `201 Created`

### `POST /api/v1/transactions/withdraw`
- **Description**: Withdraw money from an account.
- **Request Body**:
```json
{
  "accountNumber": "1234567890",
  "amount": 50.00,
  "description": "ATM withdrawal"
}
```
- **Response**: `201 Created`

### `POST /api/v1/transactions/transfer`
- **Description**: Transfer funds from one account to another.
- **Request Body**:
```json
{
  "sourceAccountNumber": "1234567890",
  "targetAccountNumber": "9876543210",
  "amount": 75.00,
  "description": "Rent payment"
}
```
- **Response**: `201 Created`

### `GET /api/v1/transactions/account/{accountNumber}`
- **Description**: Get transaction history for an account.
- **Response**: `200 OK`

### `GET /api/v1/transactions/{reference}`
- **Description**: Look up a transaction by reference string.
- **Response**: `200 OK`
