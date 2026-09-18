# Banking Application (Full-Stack Monorepo)

A full-stack banking system featuring an Android mobile client built with Kotlin and Jetpack Compose (MVVM) and a Spring Boot backend in Java 17 connected to a MySQL database.

---

## 📁 Repository & Project Structure

```
banking-app/
├── backend/          # Spring Boot REST API (Java 17, Spring Data JPA, Flyway)
├── mobile/           # Android Mobile App (Kotlin, Jetpack Compose, Retrofit, MVVM)
├── docs/             # Technical docs (Architecture, ERD, API Spec)
│   ├── architecture.md
│   ├── erd.md
│   └── api_spec.md
├── database/         # Standalone SQL schema & seed scripts
│   └── schema_and_seed.sql
├── .gitignore        # Root Git ignore covering both stacks
└── README.md         # Monorepo setup and execution instructions
```

---

## 🔗 Remote Repository & Cloning

- **Remote URL**: `https://github.com/itsZekiee/Banking-Application-Java-.git`

To clone and set up the repository locally:

```bash
git clone https://github.com/itsZekiee/Banking-Application-Java-.git
cd Banking-Application-Java-
```

---

## 🗄️ Database Setup & DataGrip Connection

The backend requires a running MySQL instance (MySQL 8.0+ recommended).

### 1. Database Creation
Create the database via MySQL CLI, DataGrip, or MySQL Workbench:
```sql
CREATE DATABASE IF NOT EXISTS pbc_db;
```

### 2. Connect DataGrip to MySQL
1. Open **DataGrip** (or Database tool window in IntelliJ IDEA).
2. Click **+ (New)** -> **Data Source** -> **MySQL**.
3. Configure the connection settings:
   - **Host**: `localhost`
   - **Port**: `3306`
   - **User**: `root` (or your local MySQL user)
   - **Password**: `root` (or your local MySQL password)
   - **Database**: `pbc_db`
4. Click **Test Connection** to ensure connectivity (download drivers if prompted).
5. In DataGrip's **Schemas** tab, ensure `pbc_db` is checked/enabled.

### 3. Populating the Schema & Seed Data
You have two easy ways to initialize and seed the database:

- **Option A (Automatic via Spring Boot Flyway)**:
  Starting the backend application automatically executes all Flyway migrations in `backend/src/main/resources/db/migration/`:
  - `V1__init_schema.sql`: creates `users`, `accounts`, and `transactions` tables with indexes and constraints in `pbc_db`.
  - `V2__seed_data.sql`: populates sample test users (`johndoe`, `janesmith`), sample bank accounts, and transactions.

- **Option B (Manual Execution in DataGrip / MySQL CLI)**:
  Open and run the standalone SQL script:
  [`database/schema_and_seed.sql`](database/schema_and_seed.sql)
  in DataGrip, MySQL Workbench, or via `mysql`:
  ```bash
  mysql -u root -p pbc_db < database/schema_and_seed.sql
  ```
  *Note for DataGrip: After running, right-click the `pbc_db` datasource and click **Refresh (Ctrl+F5 / Cmd+F5)** to view tables and data.*

---

## 🚀 Running the Backend (Spring Boot)

The backend is an independent Maven project located in `backend/`.

### In IntelliJ IDEA:
1. Open IntelliJ IDEA and choose **Open...** -> select the `backend` folder (or open the root and import `backend/pom.xml` as a Maven module).
2. Wait for Maven to index and resolve dependencies.
3. Locate `src/main/java/com/bankingapp/BankingAppApplication.java`.
4. Click the green **Run** icon next to `main()` or press `Shift + F10`.

### Via Terminal / Command Line:
```bash
cd backend
mvn clean spring-boot:run
```

- Backend API Base URL: `http://localhost:8080`
- CORS is pre-configured for local web development and Android emulator traffic.

---

## 📱 Running the Mobile App (Android Studio)

The mobile client is an independent Gradle Android project located in `mobile/`.

### In Android Studio:
1. Open Android Studio and choose **Open...** -> select the `mobile` directory.
2. Wait for Gradle sync to download dependencies and configure the project.
3. Select an Android Virtual Device (AVD / Emulator) running Android API 26+ or connect a physical device.
4. Click **Run 'app'** (`Shift + F10`).

### ⚠️ Android Emulator Networking Note
- When running against a local backend on your development machine, the Android Emulator accesses your host machine via **`http://10.0.2.2:8080/`** (instead of `localhost`).
- The project is configured out of the box with `buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")`.
- If testing on a physical device, replace `10.0.2.2` with your machine's local LAN IP (e.g. `192.168.1.x`) in `mobile/app/build.gradle.kts`.

---

## 🧪 Running Tests

### Backend Tests:
```bash
cd backend
mvn test
```

### Mobile Tests:
```bash
cd mobile
./gradlew test
```

---

## 📖 Architecture & API Documentation

For deeper details, refer to the documentation in `docs/`:
- [`docs/architecture.md`](docs/architecture.md) — Architectural overview, layering, and design patterns.
- [`docs/erd.md`](docs/erd.md) — Relational schema design, entity relationships, and constraints.
- [`docs/api_spec.md`](docs/api_spec.md) — REST API endpoint contracts, request/response models.
