# Coffee Shop Admin - Mobile App

React Native mobile application for Coffee Shop Admin Dashboard built with Expo. Manage combos, orders, and employee check-ins on the go!

## 🚀 Quick Start

### Prerequisites
- Node.js v18+
- npm or yarn
- Expo CLI: `npm install -g expo-cli`
- Android Emulator or Android device

### Installation

1. Install dependencies:
```bash
npm install
```

2. **IMPORTANT**: Update your backend IP address in these files:
   - `app/(tabs)/index.tsx` (line 6)
   - `app/(tabs)/combos.tsx` (line 6)
   - `app/(tabs)/orders.tsx` (line 6)
   - `app/(tabs)/checkin.tsx` (line 6)

   Find your IP: Open Command Prompt and type `ipconfig`
   
   Update each file:
   ```typescript
   const API_URL = 'http://192.168.1.100:3000'; // Replace with your IP
   ```

3. Start the backend server:
```bash
cd ../Coffeshop-backend-app
npm start
```

4. Start the mobile app:
```bash
npm start
```

5. Run on Android:
   - Press `a` for Android Emulator
   - Or scan QR code with Expo Go app

## 📱 Features

### 🔐 Login
- Role-based authentication (Admin/Staff only)
- Secure token storage
- Session management

### ☕ Combos Management
- View all available coffee combos
- Pricing with discount calculations
- Items included in each combo

### 📦 Order Management
- View all customer orders
- Update order status
- Color-coded status indicators
- Real-time updates

### 👤 Employee Check-In
- Camera-based attendance
- Photo capture for verification
- Real-time check-in recording
- Timestamp tracking

## 🔑 Demo Credentials

**Admin:**
- Username: `nguyenvana`
- Password: `admin`

**Staff:**
- Username: `tranthib` / Password: `staff`
- Username: `phamminhc` / Password: `staff`
- Username: `levand` / Password: `staff`

## 📚 Documentation

- **[SETUP_GUIDE.md](./SETUP_GUIDE.md)** - Complete installation guide
- **[MOBILE_README.md](./MOBILE_README.md)** - App documentation
- **[IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md)** - Technical overview

## 🛠️ Technology Stack

- **Framework**: React Native + Expo
- **Navigation**: Expo Router
- **HTTP Client**: Axios
- **Camera**: expo-camera
- **Storage**: @react-native-async-storage/async-storage
- **Backend**: Node.js + Express (port 3000)
- **Database**: MongoDB

## 📂 Project Structure

```
coffee-shop-mobile/
├── app/
│   ├── (tabs)/
│   │   ├── _layout.tsx        # Tab navigation
│   │   ├── index.tsx          # Login
│   │   ├── combos.tsx         # Combos list
│   │   ├── orders.tsx         # Order management
│   │   └── checkin.tsx        # Check-in
│   └── _layout.tsx
├── components/                # Reusable components
├── constants/                 # App constants
├── app.json                   # Expo config
└── package.json              # Dependencies
```

## 🔗 API Integration

Connects to backend endpoints:
- `POST /login` - Authentication
- `GET /combos` - Fetch combos
- `GET /orders` - Fetch orders
- `PATCH /orders/{id}` - Update status
- `POST /attendance/check-in` - Record check-in

## 🐛 Troubleshooting

**Cannot connect to backend?**
- Verify backend is running (`npm start` in backend folder)
- Check that IP address is correct
- Ensure device is on same network
- Check firewall allows port 3000

**Camera not working?**
- Grant camera permission in Android settings
- Ensure device has a camera
- Try restarting the app

**Login fails?**
- Verify username/password spelling
- Check backend MongoDB connection
- Look for errors in backend logs

**Clear cache:**
```bash
npm start -- --clear
```

## 🏗️ Building for Android

### Development APK:
```bash
npm install -g eas-cli
eas login
eas build --platform android --profile preview
```

### Production APK:
```bash
eas build --platform android --profile production
```

## ⚠️ Important Notes

- Update `API_URL` in all 4 screens before running
- Backend must be running on port 3000
- Ensure network connectivity
- Camera permissions required for check-in
- Demo credentials for testing only

## 📦 Available Scripts

```bash
npm start              # Start development server
npm start -- --clear   # Clear cache and start
npm install            # Install dependencies
eas build              # Build for distribution
```

## 🤝 Integration with Backend

This app works seamlessly with:
- Coffee Shop Admin Backend (Express.js)
- MongoDB database
- Existing API routes
- User authentication system

## 📞 Need Help?

1. Check **SETUP_GUIDE.md** for detailed setup instructions
2. Review **MOBILE_README.md** for feature documentation
3. Check backend logs for API errors
4. Verify network connectivity

## 🎉 You're Ready!

Your Coffee Shop Admin mobile app is configured and ready to use. Start managing your coffee shop from your Android device!

---

**Built with ❤️ using React Native & Expo**


```bash
npm run reset-project
```

This command will move the starter code to the **app-example** directory and create a blank **app** directory where you can start developing.

## Learn more

To learn more about developing your project with Expo, look at the following resources:

- [Expo documentation](https://docs.expo.dev/): Learn fundamentals, or go into advanced topics with our [guides](https://docs.expo.dev/guides).
- [Learn Expo tutorial](https://docs.expo.dev/tutorial/introduction/): Follow a step-by-step tutorial where you'll create a project that runs on Android, iOS, and the web.

## Join the community

Join our community of developers creating universal apps.

- [Expo on GitHub](https://github.com/expo/expo): View our open source platform and contribute.
- [Discord community](https://chat.expo.dev): Chat with Expo users and ask questions.
