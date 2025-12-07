# QUICK REFERENCE - Coffee Shop Mobile App

## 🚀 Start Here

### 1️⃣ Update IP Address (REQUIRED!)
Find your computer IP: Open CMD → `ipconfig` → Look for IPv4 Address

Edit these 4 files and change `API_URL`:
- `app/(tabs)/index.tsx`
- `app/(tabs)/combos.tsx`
- `app/(tabs)/orders.tsx`
- `app/(tabs)/checkin.tsx`

Change this line:
```typescript
const API_URL = 'http://192.168.1.100:3000'; // ← Change 192.168.1.100
```

### 2️⃣ Start Backend
```bash
cd ../Coffeshop-backend-app
npm start
# Should show: ✅ Connected to MongoDB
```

### 3️⃣ Start Mobile App
```bash
npm start
# Press 'a' for Android
```

### 4️⃣ Login with Demo Account
**Admin**: nguyenvana / admin
**Staff**: tranthib / staff

## 📱 App Screens

| Screen | Purpose | Features |
|--------|---------|----------|
| **Login** | Authentication | Sign in, Logout, User info |
| **Combos** | Product List | View combos, pricing, discounts |
| **Orders** | Order Mgmt | View orders, update status |
| **Check-In** | Attendance | Take photo, record check-in |

## ⚙️ Configuration Files

- `app.json` - App settings & permissions
- `app/(tabs)/_layout.tsx` - Navigation tabs
- `constants/config.ts` - API configuration

## 🔧 Common Commands

```bash
npm start              # Start dev server
npm start -- --clear   # Clear cache
npm install            # Install packages
eas build              # Build APK
```

## ❌ Fix Common Issues

**"Cannot connect to backend"**
- ✅ Backend running? (`npm start` in backend folder)
- ✅ IP address updated in all 4 files?
- ✅ Same WiFi network?

**"Camera permission denied"**
- ✅ Settings → Apps → Coffee Shop Admin → Permissions → Camera (Allow)

**"App won't start"**
- ✅ Run: `npm start -- --clear`

## 📚 Full Documentation

- **SETUP_GUIDE.md** - Step-by-step setup
- **MOBILE_README.md** - Complete app docs
- **IMPLEMENTATION_SUMMARY.md** - Technical details

## 🎯 Quick Checklist

- [ ] Backend IP address updated in all 4 screens
- [ ] Backend server running on port 3000
- [ ] Mobile app started with `npm start`
- [ ] Android emulator/device ready
- [ ] Can log in with demo credentials
- [ ] Combos tab shows items
- [ ] Orders tab shows orders
- [ ] Check-in camera works

## 📞 Help

1. Check backend IP address (most common issue)
2. Restart everything and clear cache
3. Check network connectivity
4. Read SETUP_GUIDE.md for detailed help

---
**Version**: 1.0.0 | **Status**: Ready to Use ✅
