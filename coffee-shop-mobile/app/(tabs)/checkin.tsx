import React, { useState, useEffect, useRef } from 'react';
import { StyleSheet, View, Text, TouchableOpacity, ActivityIndicator, Alert, ScrollView } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import { CameraView, useCameraPermissions } from 'expo-camera';

const API_URL = 'http://192.168.1.100:3000'; // Replace with your backend IP

export default function CheckInScreen() {
  const [permission, requestPermission] = useCameraPermissions();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showCamera, setShowCamera] = useState(false);
  const [checkInData, setCheckInData] = useState(null);
  const cameraRef = useRef(null);

  useEffect(() => {
    checkLoginStatus();
  }, []);

  const checkLoginStatus = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      if (token) {
        setIsLoggedIn(true);
      }
    } catch (error) {
      console.log('Error:', error);
    }
  };

  const handleCameraPermission = async () => {
    if (!permission) {
      const result = await requestPermission();
      if (!result.granted) {
        Alert.alert('Permission Denied', 'Camera permission is required for check-in');
        return;
      }
    }
    setShowCamera(true);
  };

  const takePicture = async () => {
    if (cameraRef.current) {
      try {
        setLoading(true);
        const photo = await cameraRef.current.takePictureAsync({
          base64: true,
          quality: 0.8,
        });

        const token = await AsyncStorage.getItem('token');
        const user = await AsyncStorage.getItem('user');
        const userData = JSON.parse(user);

        const response = await axios.post(
          `${API_URL}/attendance/check-in`,
          {
            employeeId: userData._id,
            photo: `data:image/jpg;base64,${photo.base64}`,
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setCheckInData(response.data);
        setShowCamera(false);
        Alert.alert('Success', 'Check-in recorded successfully');
      } catch (error) {
        Alert.alert('Error', error.response?.data?.message || 'Check-in failed');
      } finally {
        setLoading(false);
      }
    }
  };

  if (!isLoggedIn) {
    return (
      <View style={styles.container}>
        <Text style={styles.message}>Please login first to check in</Text>
      </View>
    );
  }

  if (showCamera && permission?.granted) {
    return (
      <View style={styles.cameraContainer}>
        <CameraView style={styles.camera} ref={cameraRef} facing="front">
          <View style={styles.cameraControls}>
            <TouchableOpacity 
              style={styles.closeButton}
              onPress={() => setShowCamera(false)}
            >
              <Text style={styles.closeButtonText}>✕</Text>
            </TouchableOpacity>
            <TouchableOpacity 
              style={styles.captureButton}
              onPress={takePicture}
              disabled={loading}
            >
              {loading ? (
                <ActivityIndicator color="white" />
              ) : (
                <Text style={styles.captureButtonText}>📷</Text>
              )}
            </TouchableOpacity>
          </View>
        </CameraView>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.contentContainer}>
      <Text style={styles.title}>Employee Check-In/Out</Text>

      {checkInData && (
        <View style={styles.infoBox}>
          <Text style={styles.infoTitle}>Last Check-In</Text>
          <Text style={styles.infoText}>Status: {checkInData.status}</Text>
          <Text style={styles.infoText}>Time: {new Date(checkInData.timestamp).toLocaleString()}</Text>
        </View>
      )}

      <TouchableOpacity
        style={styles.button}
        onPress={handleCameraPermission}
        disabled={loading}
      >
        <Text style={styles.buttonText}>
          {loading ? 'Processing...' : 'Open Camera for Check-In'}
        </Text>
      </TouchableOpacity>

      <View style={styles.instructionsBox}>
        <Text style={styles.instructionsTitle}>Instructions:</Text>
        <Text style={styles.instruction}>1. Tap "Open Camera for Check-In"</Text>
        <Text style={styles.instruction}>2. Position your face in the camera</Text>
        <Text style={styles.instruction}>3. Tap the camera icon to take a photo</Text>
        <Text style={styles.instruction}>4. System will process your attendance</Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  contentContainer: {
    padding: 15,
  },
  cameraContainer: {
    flex: 1,
    backgroundColor: '#000',
  },
  camera: {
    flex: 1,
  },
  cameraControls: {
    flex: 1,
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 20,
  },
  closeButton: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: 'rgba(255, 255, 255, 0.3)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeButtonText: {
    fontSize: 24,
    color: 'white',
    fontWeight: 'bold',
  },
  captureButton: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: 'rgba(139, 69, 19, 0.8)',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 3,
    borderColor: 'white',
  },
  captureButtonText: {
    fontSize: 40,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#8B4513',
    marginBottom: 20,
  },
  message: {
    fontSize: 16,
    textAlign: 'center',
    color: '#999',
    marginTop: 50,
  },
  button: {
    backgroundColor: '#8B4513',
    padding: 15,
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: 20,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
  infoBox: {
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 15,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  infoTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#8B4513',
    marginBottom: 8,
  },
  infoText: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  instructionsBox: {
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 15,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  instructionsTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#8B4513',
    marginBottom: 10,
  },
  instruction: {
    fontSize: 14,
    color: '#666',
    marginBottom: 8,
    marginLeft: 10,
  },
});
