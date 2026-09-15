import React, { useEffect, useState } from 'react';
import bankingApi from '../services/bankingApi';

const DashboardPage = () => {
  const [backendStatus, setBackendStatus] = useState('Checking...');

  useEffect(() => {
    bankingApi
      .checkHealth()
      .then((res) => setBackendStatus(res.status || 'Connected'))
      .catch(() => setBackendStatus('Backend unreachable (Check server)'));
  }, []);

  return (
    <div style={{ padding: '2rem' }}>
      <h1>Dashboard</h1>
      <p>Backend Connection Status: <strong>{backendStatus}</strong></p>
      <div style={{ marginTop: '1rem', border: '1px solid #ccc', padding: '1rem', borderRadius: '4px' }}>
        <h3>Account Overview</h3>
        <p>No accounts loaded yet. Connect with Spring Boot backend to manage accounts.</p>
      </div>
    </div>
  );
};

export default DashboardPage;
