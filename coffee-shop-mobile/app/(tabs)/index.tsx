import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text, TextInput, TouchableOpacity, Alert, ScrollView } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const API_URL = 'http://192.168.1.100:3000'; // Replace with your backend IP

export default function LoginScreen() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    checkLoginStatus();
  }, []);

  const checkLoginStatus = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      const user = await AsyncStorage.getItem('user');
      if (token && user) {
        setIsLoggedIn(true);
        setUserInfo(JSON.parse(user));
      }
    } catch (error) {
      console.log('Error checking login status:', error);
    }
  };

  const handleLogin = async () => {
    if (!username || !password) {
      Alert.alert('Error', 'Please enter username and password');
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post(`${API_URL}/login`, {
        username,
        password,
      });

      if (response.data.token) {
        await AsyncStorage.setItem('token', response.data.token);
        await AsyncStorage.setItem('user', JSON.stringify(response.data.user));
        setIsLoggedIn(true);
        setUserInfo(response.data.user);
        Alert.alert('Success', 'Login successful!');
        setUsername('');
        setPassword('');
      }
    } catch (error) {
      Alert.alert('Error', error.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('user');
      setIsLoggedIn(false);
      setUserInfo(null);
      Alert.alert('Success', 'Logged out successfully');
    } catch (error) {
      Alert.alert('Error', 'Logout failed');
    }
  };

  if (isLoggedIn && userInfo) {
    return (
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.card}>
          <Text style={styles.title}>Welcome!</Text>
          <Text style={styles.infoLabel}>User: <Text style={styles.infoValue}>{userInfo.username}</Text></Text>
          <Text style={styles.infoLabel}>Role: <Text style={styles.infoValue}>{userInfo.role}</Text></Text>
          <Text style={styles.infoLabel}>Name: <Text style={styles.infoValue}>{userInfo.fullName}</Text></Text>
          <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
            <Text style={styles.buttonText}>Logout</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    );
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.card}>
        <Text style={styles.title}>Coffee Shop Admin</Text>
        <Text style={styles.subtitle}>Login</Text>

        <TextInput
          style={styles.input}
          placeholder="Username"
          value={username}
          onChangeText={setUsername}
          placeholderTextColor="#999"
        />

        <TextInput
          style={styles.input}
          placeholder="Password"
          value={password}
          onChangeText={setPassword}
          secureTextEntry
          placeholderTextColor="#999"
        />

        <TouchableOpacity
          style={[styles.button, loading && styles.buttonDisabled]}
          onPress={handleLogin}
          disabled={loading}
        >
          <Text style={styles.buttonText}>{loading ? 'Logging in...' : 'Login'}</Text>
        </TouchableOpacity>

        <Text style={styles.demoText}>Demo Credentials:</Text>
        <Text style={styles.demoCredentials}>Admin: nguyenvana / admin</Text>
        <Text style={styles.demoCredentials}>Staff: tranthib / staff</Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
    backgroundColor: '#f5f5f5',
  },
  card: {
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 10,
    color: '#8B4513',
  },
  subtitle: {
    fontSize: 18,
    textAlign: 'center',
    marginBottom: 20,
    color: '#666',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    marginBottom: 15,
    fontSize: 16,
    color: '#000',
  },
  button: {
    backgroundColor: '#8B4513',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 10,
  },
  buttonDisabled: {
    opacity: 0.5,
  },
  logoutButton: {
    backgroundColor: '#e74c3c',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 10,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
  demoText: {
    marginTop: 20,
    fontSize: 14,
    fontWeight: 'bold',
    color: '#666',
  },
  demoCredentials: {
    fontSize: 13,
    color: '#999',
    marginTop: 5,
  },
  infoLabel: {
    fontSize: 16,
    marginBottom: 10,
    color: '#666',
  },
  infoValue: {
    fontWeight: 'bold',
    color: '#8B4513',
  },
});

