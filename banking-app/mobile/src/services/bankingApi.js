import axios from 'axios';
import { Platform } from 'react-native';

/**
 * =============================================================================
 * IMPORTANT NETWORK NOTE FOR MOBILE (ANDROID EMULATOR vs iOS / PHYSICAL DEVICE)
 * =============================================================================
 * - Android Emulator: Always use 'http://10.0.2.2:8080/api' to connect to the
 *   Spring Boot backend running on your host machine. 'localhost' resolves
 *   internally to the emulator itself, causing connection refused errors.
 * - iOS Simulator: Use 'http://localhost:8080/api'.
 * - Physical Devices: Replace with your development machine's local LAN IP
 *   (e.g., 'http://192.168.1.100:8080/api') or configure 'adb reverse tcp:8080 tcp:8080'.
 * =============================================================================
 */
const DEFAULT_API_URL = Platform.select({
  android: 'http://10.0.2.2:8080/api',
  ios: 'http://localhost:8080/api',
  default: 'http://10.0.2.2:8080/api',
});

const apiClient = axios.create({
  baseURL: DEFAULT_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Response interceptor for handling API errors uniformly across mobile
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('Mobile API Error:', error.response || error.message);
    return Promise.reject(error);
  }
);

/**
 * Banking API Service Stub (Mirroring Web API Service)
 */
export const bankingApi = {
  // Health check
  checkHealth: async () => {
    const response = await apiClient.get('/health');
    return response.data;
  },

  // Account endpoints stub
  getAccounts: async () => {
    const response = await apiClient.get('/accounts');
    return response.data;
  },

  getAccountById: async (id) => {
    const response = await apiClient.get(`/accounts/${id}`);
    return response.data;
  },

  createAccount: async (accountData) => {
    const response = await apiClient.post('/accounts', accountData);
    return response.data;
  },

  deposit: async (accountId, amount) => {
    const response = await apiClient.post(`/accounts/${accountId}/deposit`, { amount });
    return response.data;
  },

  withdraw: async (accountId, amount) => {
    const response = await apiClient.post(`/accounts/${accountId}/withdraw`, { amount });
    return response.data;
  },

  transfer: async (fromAccountId, toAccountId, amount) => {
    const response = await apiClient.post('/accounts/transfer', {
      fromAccountId,
      toAccountId,
      amount,
    });
    return response.data;
  },
};

export default bankingApi;
