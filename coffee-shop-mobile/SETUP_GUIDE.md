# Coffee Shop Mobile App - Complete Setup Guide

## Step-by-Step Installation & Configuration

### 1. Prerequisites

Make sure you have installed:
- Node.js v18+ ([download](https://nodejs.org/))
- npm (comes with Node.js)
- Expo CLI: `npm install -g expo-cli`
- Android Emulator (via Android Studio) OR physical Android device

### 2. Project Setup

```bash
# Navigate to the mobile app directory
cd "d:\HUNG\Coffee.Shop.Android.APp\coffee-shop-mobile"

# Install dependencies (should already be done)
npm install
```

### 3. Configure Backend Connection

**IMPORTANT: This step is required for the app to work!**

Find your backend server's IP address:

#### On Windows (where backend runs):
1. Open Command Prompt (cmd.exe)
2. Type: `ipconfig`
3. Look for "IPv4 Address" under "Wireless LAN adapter" or "Ethernet adapter"
   - Usually looks like: `192.168.1.100` or `10.0.0.x`

#### Update Configuration Files:

Update the IP address in these files to match your backend server:

**File 1: `app/(tabs)/index.tsx`** (Line 6)
```typescript
const API_URL = 'http://YOUR_BACKEND_IP:3000';
```

**File 2: `app/(tabs)/combos.tsx`** (Line 6)
```typescript
const API_URL = 'http://YOUR_BACKEND_IP:3000';
```

**File 3: `app/(tabs)/orders.tsx`** (Line 6)
```typescript
const API_URL = 'http://YOUR_BACKEND_IP:3000';
```

**File 4: `app/(tabs)/checkin.tsx`** (Line 6)
```typescript
const API_URL = 'http://YOUR_BACKEND_IP:3000';
```

**Example:** If your IP is `192.168.1.50`:
```typescript
const API_URL = 'http://192.168.1.50:3000';
```

### 4. Start the Backend Server

Make sure the backend is running:

```bash
cd "d:\HUNG\Coffee.Shop.Android.APp\Coffee Shop Android APp\Coffeshop-backend-app"
npm start
```

You should see:
```
🚀 Server running on port 3000
✅ Connected to MongoDB
```

### 5. Start the Mobile App

```bash
cd "d:\HUNG\Coffee.Shop.Android.APp\coffee-shop-mobile"
npm start
```

You should see:
```
expo-router: Waiting for project to load...
Starting Metro Bundler
```

### 6. Run on Android

**Option A: Android Emulator**
1. Open Android Studio
2. Start an Android Virtual Device (AVD)
3. In the Expo CLI terminal, press `a`
4. The app will install and run

**Option B: Expo Go App**
1. Install "Expo Go" from Google Play Store
2. In the Expo CLI terminal, scan the QR code with your phone
3. The app will load in Expo Go

**Option C: Physical Android Device**
1. Connect Android device via USB
2. Enable USB debugging in Developer Options
3. In the Expo CLI terminal, press `a`

### 7. Login to the App

Use these demo credentials:

**Admin Account:**
- Username: `nguyenvana`
- Password: `admin`

**Staff Accounts:**
- Username: `tranthib` / Password: `staff`
- Username: `phamminhc` / Password: `staff`
- Username: `levand` / Password: `staff`

### 8. Test Features

1. **Login Tab**: Enter credentials and test login/logout
2. **Combos Tab**: View all available combos from database
3. **Orders Tab**: View orders and update their status
4. **Check-In Tab**: Grant camera permission and take a photo

## Troubleshooting

### App won't start
```bash
# Clear cache and restart
npm start -- --clear
```

### Cannot connect to backend
- [ ] Backend is running on port 3000?
- [ ] Updated API_URL in all 4 files?
- [ ] Is the IP address correct? (check with `ipconfig`)
- [ ] Android device/emulator on same WiFi network?
- [ ] Firewall allowing port 3000?

### Camera not working
- [ ] Camera permission granted?
- [ ] Device has a camera?
- [ ] Try: Settings → Apps → Coffee Shop Admin → Permissions → Camera (Allow)

### Login fails
- [ ] Check username/password spelling
- [ ] Backend MongoDB running?
- [ ] Backend server shows "Connected to MongoDB"?

### Module not found errors
```bash
# Reinstall all dependencies
rm -r node_modules
npm install
npm start -- --clear
```

## Project Structure

```
coffee-shop-mobile/
├── app/
│   ├── (tabs)/
│   │   ├── _layout.tsx      # Tab navigation setup
│   │   ├── index.tsx        # Login screen
│   │   ├── combos.tsx       # Combo list
│   │   ├── orders.tsx       # Order management
│   │   └── checkin.tsx      # Check-in with camera
│   └── _layout.tsx          # Root layout
├── constants/
│   └── config.ts            # API configuration
├── app.json                 # App configuration & permissions
├── package.json             # Dependencies
└── MOBILE_README.md         # App documentation
```

## Building APK for Distribution

### Create signed APK:
```bash
npm install -g eas-cli
eas login
eas build --platform android
```

### Choose build type:
- `preview` - For testing
- `production` - For Google Play release

The APK will be ready to download after build completes.

## Important Notes

⚠️ **Security Reminders:**
- Never commit actual API URLs with production IPs
- Keep demo credentials for development only
- Implement proper authentication in production
- Validate all user input on backend
- Use HTTPS in production

## Next Steps

After successful setup:
1. Test all features thoroughly
2. Check network connectivity and error handling
3. Review camera permissions on Android
4. Test with different network conditions
5. Build APK when ready for distribution

## Quick Command Reference

```bash
# Start development server
npm start

# Clear cache and start
npm start -- --clear

# Install packages
npm install

# Build for Android (requires EAS account)
eas build --platform android

# View logs
npm start -- --verbose

# Reset project state
rm -rf .expo node_modules && npm install
```

## Support & Debugging

Check logs for errors:
1. Expo CLI terminal
2. Android logcat
3. Network tab in Expo DevTools

For more help:
- Check backend `server.js` for API errors
- Verify MongoDB connection in backend logs
- Ensure all environment variables are set
