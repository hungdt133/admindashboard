import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text, ScrollView, FlatList, TouchableOpacity, ActivityIndicator, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const API_URL = 'http://192.168.1.100:3000'; // Replace with your backend IP

export default function OrdersScreen() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    checkLoginAndFetchOrders();
  }, []);

  const checkLoginAndFetchOrders = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      if (token) {
        setIsLoggedIn(true);
        fetchOrders(token);
      } else {
        setIsLoggedIn(false);
        setLoading(false);
      }
    } catch (error) {
      console.log('Error:', error);
      setLoading(false);
    }
  };

  const fetchOrders = async (token) => {
    try {
      const response = await axios.get(`${API_URL}/orders`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders(response.data);
    } catch (error) {
      Alert.alert('Error', 'Failed to fetch orders');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (orderId, newStatus) => {
    try {
      const token = await AsyncStorage.getItem('token');
      await axios.patch(`${API_URL}/orders/${orderId}`, { status: newStatus }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchOrders(token);
      Alert.alert('Success', 'Order updated');
    } catch (error) {
      Alert.alert('Error', 'Failed to update order');
    }
  };

  if (!isLoggedIn) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>Please login first to view orders</Text>
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

  const getStatusColor = (status) => {
    switch (status) {
      case 'pending': return '#f39c12';
      case 'confirmed': return '#3498db';
      case 'preparing': return '#e74c3c';
      case 'ready': return '#27ae60';
      default: return '#95a5a6';
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Orders</Text>
      <FlatList
        data={orders}
        keyExtractor={(item) => item._id}
        scrollEnabled={false}
        renderItem={({ item }) => (
          <View style={styles.orderCard}>
            <Text style={styles.orderId}>Order #{item._id.slice(-6).toUpperCase()}</Text>
            <Text style={styles.details}>Customer: {item.customerName}</Text>
            <Text style={styles.details}>Items: {item.items.length}</Text>
            <Text style={styles.details}>Total: ₫{item.totalPrice.toLocaleString()}</Text>
            
            <View style={styles.statusContainer}>
              <Text style={[styles.status, { color: getStatusColor(item.status) }]}>
                Status: {item.status.toUpperCase()}
              </Text>
            </View>

            <View style={styles.buttonContainer}>
              <TouchableOpacity 
                style={[styles.statusButton, { backgroundColor: '#f39c12' }]}
                onPress={() => handleStatusChange(item._id, 'pending')}
              >
                <Text style={styles.buttonText}>Pending</Text>
              </TouchableOpacity>
              <TouchableOpacity 
                style={[styles.statusButton, { backgroundColor: '#3498db' }]}
                onPress={() => handleStatusChange(item._id, 'confirmed')}
              >
                <Text style={styles.buttonText}>Confirmed</Text>
              </TouchableOpacity>
              <TouchableOpacity 
                style={[styles.statusButton, { backgroundColor: '#e74c3c' }]}
                onPress={() => handleStatusChange(item._id, 'preparing')}
              >
                <Text style={styles.buttonText}>Preparing</Text>
              </TouchableOpacity>
              <TouchableOpacity 
                style={[styles.statusButton, { backgroundColor: '#27ae60' }]}
                onPress={() => handleStatusChange(item._id, 'ready')}
              >
                <Text style={styles.buttonText}>Ready</Text>
              </TouchableOpacity>
            </View>
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
  orderCard: {
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
  orderId: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#8B4513',
    marginBottom: 8,
  },
  details: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  statusContainer: {
    marginVertical: 10,
  },
  status: {
    fontSize: 14,
    fontWeight: 'bold',
  },
  buttonContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 10,
    gap: 8,
  },
  statusButton: {
    flex: 1,
    minWidth: '48%',
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: 6,
    alignItems: 'center',
  },
  buttonText: {
    color: 'white',
    fontSize: 12,
    fontWeight: '600',
  },
});
