# Banking Application (Full-Stack Multi-Client)

A full-stack banking platform designed with a clean multi-client architecture powered by a single shared backend API.

---

## 📁 Project Structure

```text
banking-app/
├── backend/       # Spring Boot (Java) REST API
├── web/           # React web frontend application
├── mobile/        # React Native (Android-focused) mobile app
├── .gitignore     # Unified gitignore for Java, Node, and React Native
└── README.md      # Root project documentation
```

---

## 🚀 Getting Started

### 1. Backend (`backend/`) — Spring Boot REST API

The backend serves as the central data and business logic hub for both web and mobile clients.

- **Tech Stack:** Java 17+, Spring Boot 3.x, Spring Data JPA, MySQL/PostgreSQL, Lombok, Maven.
- **Port:** Defaults to `8080`.
- **CORS Configuration:** Configured in `CorsConfig.java` to support requests from React web (`http://localhost:3000` / `http://localhost:5173`) and mobile clients.

#### Running the Backend:
```bash
cd backend
mvn clean spring-boot:run
```
Or build the JAR:
```bash
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

---

### 2. Web Client (`web/`) — React Web Frontend

A responsive web application for desktop and browser-based banking operations.

- **Tech Stack:** React, Axios.
- **Environment:** Configure `.env` with `REACT_APP_API_BASE_URL=http://localhost:8080/api`.

#### Running the Web Client:
```bash
cd web
npm install
npm start
```
Runs at `http://localhost:3000`.

---

### 3. Mobile Client (`mobile/`) — React Native App

An Android-focused mobile application for mobile banking operations.

- **Tech Stack:** React Native CLI, Axios.
- **Android Emulator Network Note:**
  - Android emulators run inside a virtual network where `localhost` refers to the emulator itself.
  - To access the host machine's Spring Boot backend from an Android emulator, use **`http://10.0.2.2:8080/api`** instead of `http://localhost:8080/api`.
  - For physical Android devices connected via USB/Wi-Fi, use your local network IP (e.g., `http://192.168.x.x:8080/api`) or configure ADB reverse proxy (`adb reverse tcp:8080 tcp:8080`).

#### Running the Mobile Client:
```bash
cd mobile
npm install
npm run android
```

---

## 🛡️ Architecture & Clean Separation

- **Backend:** Exposes RESTful endpoints, manages transactional database state, security, validation, and business entities.
- **Web & Mobile Clients:** Independent client applications with decoupled presentation layers consuming the REST API through designated API service modules (`bankingApi.js`).
