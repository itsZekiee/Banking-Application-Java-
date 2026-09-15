# Mobile Client (React Native - Android Focused)

React Native mobile client for the Banking Application.

## ⚠️ Important Android Network Configuration

- When testing in an **Android Emulator**, `localhost` points to the emulator device itself, not your development computer.
- Use **`http://10.0.2.2:8080/api`** as the backend base URL (pre-configured in `src/services/bankingApi.js`).
- If using a physical Android device connected over Wi-Fi, update the API base URL to your machine's local IP address (e.g., `http://192.168.1.X:8080/api`) or run:
  ```bash
  adb reverse tcp:8080 tcp:8080
  ```

## Getting Started

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start Metro bundler:
   ```bash
   npm start
   ```

3. Run on Android:
   ```bash
   npm run android
   ```
