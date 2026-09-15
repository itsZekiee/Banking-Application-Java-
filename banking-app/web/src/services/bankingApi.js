import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Response interceptor for handling API errors uniformly
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle or log global response errors
    console.error('API Error:', error.response || error.message);
    return Promise.reject(error);
  }
);

/**
 * Banking API Service Stub
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
