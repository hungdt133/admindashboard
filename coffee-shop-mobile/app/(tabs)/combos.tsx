import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text, ScrollView, FlatList, TouchableOpacity, ActivityIndicator, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const API_URL = 'http://192.168.1.100:3000'; // Replace with your backend IP

export default function CombosScreen() {
  const [combos, setCombos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    checkLoginAndFetchCombos();
  }, []);

  const checkLoginAndFetchCombos = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      if (token) {
        setIsLoggedIn(true);
        fetchCombos(token);
      } else {
        setIsLoggedIn(false);
        setLoading(false);
      }
    } catch (error) {
      console.log('Error:', error);
      setLoading(false);
    }
  };

  const fetchCombos = async (token) => {
    try {
      const response = await axios.get(`${API_URL}/combos`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setCombos(response.data);
    } catch (error) {
      Alert.alert('Error', 'Failed to fetch combos');
    } finally {
      setLoading(false);
    }
  };

  if (!isLoggedIn) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>Please login first to view combos</Text>
      </View>
    );
  }

  if (loading) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator size="large" color="#8B4513" />
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Combos</Text>
      <FlatList
        data={combos}
        keyExtractor={(item) => item._id}
        scrollEnabled={false}
        renderItem={({ item }) => (
          <View style={styles.comboCard}>
            <Text style={styles.comboName}>{item.name}</Text>
            <Text style={styles.comboDescription}>{item.description}</Text>
            <View style={styles.priceContainer}>
              <Text style={styles.originalPrice}>₫{item.originalPrice.toLocaleString()}</Text>
              <Text style={styles.discountedPrice}>₫{item.discountedPrice.toLocaleString()}</Text>
            </View>
            <Text style={styles.discount}>Discount: {item.discount}%</Text>
            <Text style={styles.includes}>Includes: {item.includes.join(', ')}</Text>
          </View>
        )}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    padding: 10,
  },
  centerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 15,
    color: '#8B4513',
  },
  message: {
    fontSize: 16,
    textAlign: 'center',
    color: '#999',
    marginTop: 50,
  },
  comboCard: {
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 15,
    marginBottom: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  comboName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#8B4513',
    marginBottom: 8,
  },
  comboDescription: {
    fontSize: 14,
    color: '#666',
    marginBottom: 10,
  },
  priceContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  originalPrice: {
    fontSize: 14,
    color: '#999',
    textDecorationLine: 'line-through',
    marginRight: 10,
  },
  discountedPrice: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#e74c3c',
  },
  discount: {
    fontSize: 13,
    color: '#27ae60',
    fontWeight: '600',
    marginBottom: 8,
  },
  includes: {
    fontSize: 12,
    color: '#555',
  },
});
