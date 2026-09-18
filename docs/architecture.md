# Banking Application Architecture Overview

## 1. System Architecture

The Banking Application follows a clean client-server architecture designed for modularity, testability, and clear separation of concerns across platforms.

```
┌───────────────────────────────┐
│       Android Client          │
│   (Kotlin + Jetpack Compose)  │
│          MVVM Pattern         │
└──────────────┬────────────────┘
               │ HTTPS / JSON REST
               ▼
┌───────────────────────────────┐
│     Spring Boot Backend       │
│    (Java 17 / Layered Arch)   │
│  Controller -> Service -> Repo│
└──────────────┬────────────────┘
               │ JDBC / Flyway
               ▼
┌───────────────────────────────┐
│       MySQL Database          │
│ (users, accounts, transactions│
└───────────────────────────────┘
```

---

## 2. Backend Architecture (Spring Boot)

The backend follows a standard 4-tier layered architecture:

1. **Controller Layer (`com.bankingapp.controller`)**:
   - Manages incoming HTTP requests and responses.
   - Enforces bean validation annotations (`@Valid`).
   - Delegates all business execution immediately to the service layer.
   - Returns standardized `ApiResponse<T>` wrappers.

2. **Service Layer (`com.bankingapp.service`)**:
   - Contains all domain logic, validation rules, and business workflows.
   - Acts as the sole boundary for transaction management (`@Transactional`).
   - Maps between internal JPA entities and external DTOs.

3. **Repository Layer (`com.bankingapp.repository`)**:
   - Spring Data JPA interfaces extending `JpaRepository`.
   - Optimized for relational queries and constraints.

4. **Entity & DTO Layer (`com.bankingapp.entity`, `com.bankingapp.dto`)**:
   - JPA Entities map database tables (`users`, `accounts`, `transactions`).
   - Request and Response DTOs isolate database internals from public API consumers.

5. **Error & Exception Handling (`com.bankingapp.exception`)**:
   - Centralized via `@RestControllerAdvice` in `GlobalExceptionHandler`.
   - Returns consistent error structures (`status`, `error`, `message`, `timestamp`).

---

## 3. Mobile Architecture (Android / Kotlin)

The mobile client is built on Modern Android Architecture with **MVVM (Model-View-ViewModel)** and **Jetpack Compose**:

1. **UI Layer (`com.bankingapp.ui`)**:
   - **Screens**: Dedicated Composable screens observing StateFlow from ViewModels.
   - **Components**: Reusable Compose UI elements (buttons, text inputs, headers, error banners).
   - **Navigation**: Type-safe navigation graph using Jetpack Navigation Compose.
   - **Theme**: Material 3 theming (typography, color palettes, shapes).

2. **ViewModel Layer (`com.bankingapp.viewmodel`)**:
   - Manages UI state using Kotlin Coroutines and `StateFlow`.
   - Processes user actions and calls repositories.
   - Decoupled from Android UI framework dependencies.

3. **Data Layer (`com.bankingapp.data`)**:
   - **Repositories (`data.repository`)**: Act as the single source of truth for ViewModels.
   - **Remote (`data.remote`)**: Retrofit service interfaces and HTTP client configuring `BuildConfig.BASE_URL`.
   - **Model (`data.model`)**: Kotlin data classes mirroring backend DTOs.

---

## 4. Development & Tooling

| Component | Technology | Tool / IDE |
|---|---|---|
| Mobile Client | Kotlin, Jetpack Compose, Retrofit, Coroutines | Android Studio |
| Backend API | Java 17, Spring Boot 3, Spring Data JPA, Flyway | IntelliJ IDEA |
| Database | MySQL 8.0+ | DataGrip / MySQL Workbench |
