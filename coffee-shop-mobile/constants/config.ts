/**
 * API Configuration for Coffee Shop Mobile App
 * 
 * IMPORTANT: Update the API_URL to match your backend server's IP address
 * 
 * To find your backend IP:
 * 1. Run backend on your computer
 * 2. Find your computer's local IP address:
 *    - Windows: Open Command Prompt and type: ipconfig
 *    - Look for "IPv4 Address" (usually 192.168.x.x)
 * 3. Update API_URL below with that IP
 */

export const API_CONFIG = {
  // Update this to your backend server IP address
  // Format: http://192.168.x.x:3000
  API_URL: 'http://192.168.1.100:3000',
  
  // Timeout for API requests (in milliseconds)
  TIMEOUT: 10000,
  
  // Retry configuration
  RETRY_ATTEMPTS: 3,
  RETRY_DELAY: 1000,
};

export const STORAGE_KEYS = {
  TOKEN: 'token',
  USER: 'user',
  LAST_CHECK_IN: 'lastCheckIn',
};

export const DEMO_CREDENTIALS = {
  ADMIN: {
    username: 'nguyenvana',
    password: 'admin',
    role: 'admin',
  },
  STAFF: [
    {
      username: 'tranthib',
      password: 'staff',
      role: 'staff',
    },
    {
      username: 'phamminhc',
      password: 'staff',
      role: 'staff',
    },
    {
      username: 'levand',
      password: 'staff',
      role: 'staff',
    },
  ],
};

// For debugging - set to true to see API calls in console
export const DEBUG_MODE = true;
