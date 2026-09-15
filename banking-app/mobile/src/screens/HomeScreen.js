import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View, ActivityIndicator } from 'react-native';
import bankingApi from '../services/bankingApi';

export default function HomeScreen() {
  const [status, setStatus] = useState('Connecting...');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    bankingApi
      .checkHealth()
      .then((data) => {
        setStatus(data.status || 'Connected');
      })
      .catch((err) => {
        setStatus('Backend Unreachable (Check 10.0.2.2 config)');
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Mobile Banking</Text>
      <Text style={styles.subtitle}>Android-Focused React Native Client</Text>
      
      <View style={styles.card}>
        <Text style={styles.cardTitle}>Backend Status:</Text>
        {loading ? (
          <ActivityIndicator size="small" color="#0284c7" />
        ) : (
          <Text style={styles.statusText}>{status}</Text>
        )}
      </View>

      <Text style={styles.note}>
        Note: Ensure backend is running and Android emulator accesses it via http://10.0.2.2:8080.
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    backgroundColor: '#f1f5f9',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#0f172a',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 14,
    color: '#64748b',
    marginBottom: 24,
  },
  card: {
    width: '100%',
    padding: 16,
    borderRadius: 8,
    backgroundColor: '#ffffff',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
    marginBottom: 16,
  },
  cardTitle: {
    fontSize: 14,
    color: '#475569',
    marginBottom: 6,
  },
  statusText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#0369a1',
  },
  note: {
    fontSize: 12,
    color: '#94a3b8',
    textAlign: 'center',
  },
});
