# Coffee Shop Admin Mobile App

React Native mobile application for Coffee Shop Admin Dashboard built with Expo.

## Features

- **Login** - Role-based authentication (Admin/Staff only)
- **Combos** - View all available coffee combos with pricing
- **Orders** - Manage customer orders and update status
- **Check-In/Out** - Employee attendance with camera capture

## Installation

### Prerequisites
- Node.js (v18+)
- npm or yarn
- Expo CLI: `npm install -g expo-cli`
- Android Emulator or Android device

### Setup

1. Navigate to the project directory:
```bash
cd coffee-shop-mobile
```

2. Install dependencies:
```bash
npm install
```

3. Update API URL in screens:
   - Open `app/(tabs)/index.tsx`
   - Change `API_URL` to your backend server IP address
   - Example: `http://192.168.1.100:3000`
   - Do the same for `app/(tabs)/combos.tsx`, `app/(tabs)/orders.tsx`, and `app/(tabs)/checkin.tsx`

## Running the App

### Development on Android

1. Start Expo:
```bash
npm start
```

2. Press `a` to open Android emulator or scan QR code with Expo Go app

### Build for Android APK

```bash
eas build --platform android --profile preview
```

### Build for Android App Bundle (for Google Play)

```bash
eas build --platform android --profile production
```

## Demo Credentials

**Admin Account:**
- Username: `nguyenvana`
- Password: `admin`

**Staff Account:**
- Username: `tranthib`
- Password: `staff`

## Project Structure

```
coffee-shop-mobile/
├── app/
│   ├── (tabs)/
│   │   ├── _layout.tsx         # Tab navigation
│   │   ├── index.tsx           # Login screen
│   │   ├── combos.tsx          # Combo list screen
│   │   ├── orders.tsx          # Order management screen
│   │   └── checkin.tsx         # Employee check-in screen
│   ├── _layout.tsx             # Root layout
│   └── modal.tsx               # Modal screen
├── components/                 # Reusable components
├── constants/                  # App constants
├── hooks/                      # Custom hooks
├── assets/                     # Images and icons
├── app.json                    # Expo configuration
└── package.json                # Dependencies
```

## Features in Detail

### Login Screen
- Secure authentication against MongoDB backend
- Role-based access control
- Token stored in AsyncStorage
- Logout functionality

### Combos Screen
- Displays all available combos
- Shows pricing with discount calculations
- Lists included items in each combo
- Real-time data from backend

### Orders Screen
- View all pending and completed orders
- Update order status with one tap
- Color-coded status indicators
- Customer and pricing information

### Check-In Screen
- Camera access for employee photo capture
- Real-time attendance recording
- Check-in timestamp tracking
- Photo stored on backend

## Permissions

The app requires the following Android permissions:
- `CAMERA` - For employee check-in photo capture
- `INTERNET` - For API communication
- `READ_EXTERNAL_STORAGE` - For photo storage
- `WRITE_EXTERNAL_STORAGE` - For photo storage

## API Integration

The app connects to the backend API at:
- Default: `http://192.168.1.100:3000`

**Important:** Update the IP address to match your backend server!

### API Endpoints Used:
- `POST /login` - User authentication
- `GET /combos` - Fetch all combos
- `GET /orders` - Fetch all orders
- `PATCH /orders/{id}` - Update order status
- `POST /attendance/check-in` - Record employee check-in

## Troubleshooting

### Camera not working
- Ensure camera permission is granted
- Check that your device has a camera
- Verify the permission in Android settings

### Cannot connect to backend
- Update API_URL to correct backend IP
- Ensure backend server is running
- Check device is on same network as backend
- Verify firewall allows port 3000

### Login fails
- Check username and password
- Ensure backend database is running
- Verify network connectivity

## Development

### Run in development mode:
```bash
npm start
```

### View logs:
```bash
npm start -- --verbose
```

### Clear cache:
```bash
npm start -- --clear
```

## Building

### Prerequisites for EAS builds:
1. Create Expo account: `expo register`
2. Login: `expo login`
3. Install EAS CLI: `npm install -g eas-cli`

### Build configurations available:
- `preview` - For testing on device
- `production` - For Google Play release

## Notes

- Ensure both backend and frontend are properly configured
- Update API URLs in all screen files
- Test on Android emulator before building APK
- Camera permissions require explicit user grant
- All user data is stored securely in AsyncStorage

## Support

For issues or questions, check:
1. Backend is running on correct port (3000)
2. API URLs match your network configuration
3. Permissions are properly granted in Android settings
