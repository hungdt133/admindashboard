# Coffee Shop Admin - Android App (Kotlin)

Native Android application built with Kotlin for the Coffee Shop Admin Dashboard. Manage combos, orders, and employee attendance with a native Android experience.

## Features

- **🔐 Login** - Role-based authentication (Admin/Staff)
- **☕ Combos Management** - View and manage coffee combos
- **📦 Order Management** - View orders and update status
- **👤 Employee Check-In** - Camera-based attendance tracking

## Prerequisites

- Android Studio (latest version)
- Android SDK (API level 24+)
- Kotlin 1.9.10+
- Gradle 8.2.0+

## Installation

### 1. Open in Android Studio

```bash
# Open the project in Android Studio
File → Open → CoffeeShopAdminAndroid
```

### 2. Configure Backend Connection

Edit `app/src/main/java/com/coffeeshop/admin/network/ApiClient.kt`:

```kotlin
private const val BASE_URL = "http://192.168.1.100:3000/"
```

Replace `192.168.1.100` with your backend server IP address.

To find your IP:
- **Windows**: Open Command Prompt → `ipconfig` → Look for IPv4 Address
- **Mac/Linux**: Open Terminal → `ifconfig` → Look for inet address

### 3. Start Backend Server

```bash
cd ../Coffeshop-backend-app
npm start
```

Should show:
```
🚀 Server running on port 3000
✅ Connected to MongoDB
```

### 4. Build and Run

**Option A: Using Android Emulator**
1. Click "Run" or press `Shift + F10`
2. Select Android Virtual Device (AVD)
3. App will build and run

**Option B: Using Physical Device**
1. Connect Android device via USB
2. Enable USB Debugging: Settings → Developer Options → USB Debugging
3. Click "Run" button
4. Select your device

## Login Credentials

**Admin Account:**
- Username: `nguyenvana`
- Password: `admin`

**Staff Accounts:**
- Username: `tranthib` / Password: `staff`
- Username: `phamminhc` / Password: `staff`
- Username: `levand` / Password: `staff`

## Project Structure

```
CoffeeShopAdminAndroid/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/coffeeshop/admin/
│   │   │   │   ├── MainActivity.kt              # Main hub
│   │   │   │   ├── ui/
│   │   │   │   │   ├── LoginActivity.kt        # Login screen
│   │   │   │   │   ├── CombosActivity.kt       # Combos list
│   │   │   │   │   ├── CombosAdapter.kt        # Combos recycler adapter
│   │   │   │   │   ├── OrdersActivity.kt       # Orders management
│   │   │   │   │   ├── OrdersAdapter.kt        # Orders recycler adapter
│   │   │   │   │   └── CheckInActivity.kt      # Check-in with camera
│   │   │   │   ├── models/
│   │   │   │   │   └── Models.kt               # Data models
│   │   │   │   └── network/
│   │   │   │       ├── ApiClient.kt            # Retrofit configuration
│   │   │   │       └── ApiService.kt           # API endpoints
│   │   │   ├── res/
│   │   │   │   ├── layout/                     # XML layouts
│   │   │   │   ├── drawable/                   # Drawable resources
│   │   │   │   ├── menu/                       # Menu resources
│   │   │   │   └── values/                     # String/theme resources
│   │   │   └── AndroidManifest.xml             # App manifest
│   ├── build.gradle.kts                        # App build config
│   └── ...
├── build.gradle.kts                            # Project build config
├── settings.gradle.kts                         # Project settings
└── README.md
```

## Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: AndroidX
- **HTTP Client**: Retrofit 2 + OkHttp
- **JSON Serialization**: Gson
- **Camera**: AndroidX Camera
- **Storage**: SharedPreferences
- **Threading**: Coroutines

### Key Components

**1. ApiClient (Singleton)**
- Centralized Retrofit configuration
- HTTP logging interceptor
- Base URL: `http://{BACKEND_IP}:3000/`

**2. ApiService (Interface)**
- Retrofit service endpoints
- Authentication headers
- Request/Response models

**3. Activities**
- **LoginActivity**: Authentication and user management
- **MainActivity**: Navigation hub with bottom tabs
- **CombosActivity**: Display combo list
- **OrdersActivity**: Order management with status updates
- **CheckInActivity**: Camera-based attendance

**4. Adapters**
- **CombosAdapter**: RecyclerView for combos
- **OrdersAdapter**: RecyclerView for orders with inline buttons

**5. Models**
- User, Combo, Order, Attendance data classes
- Parcelable for bundle passing

## API Integration

### Endpoints Used

```
POST /login                    - User authentication
GET  /combos                   - Fetch all combos
GET  /combos/{id}             - Get single combo
POST /combos                   - Create combo
PUT  /combos/{id}             - Update combo
DELETE /combos/{id}           - Delete combo

GET  /orders                   - Fetch all orders
GET  /orders/{id}             - Get single order
PATCH /orders/{id}            - Update order status

POST /attendance/check-in      - Record attendance
GET  /attendance/records       - Get attendance history
```

### Authentication
- Token stored in SharedPreferences
- Passed in header: `Authorization: Bearer {token}`
- Token automatically included in all requests

## Permissions

Required permissions (in AndroidManifest.xml):
- `INTERNET` - API communication
- `CAMERA` - Employee check-in
- `READ_EXTERNAL_STORAGE` - Photo storage
- `WRITE_EXTERNAL_STORAGE` - Photo storage

## Building APK

### Debug APK
```bash
# Using Gradle
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK
```bash
./gradlew assembleRelease

# APK location: app/build/outputs/apk/release/app-release.apk
```

### Google Play Bundle
```bash
./gradlew bundleRelease
```

## Troubleshooting

### "Cannot resolve API_URL"
- Update `BASE_URL` in `ApiClient.kt`
- Verify backend IP address
- Ensure backend is running on port 3000

### "Login fails"
- Check username/password
- Verify backend is connected to MongoDB
- Check firewall allows port 3000
- Look at logcat for error messages

### "Camera not working"
- Grant camera permission: Settings → Apps → Coffee Shop Admin → Permissions
- Check device has camera hardware
- Restart app and try again
- Check logcat: `adb logcat | grep CheckInActivity`

### "Network timeout"
- Verify backend is running
- Check device is on same network
- Increase timeout in `ApiClient.kt`:
  ```kotlin
  .connectTimeout(60, TimeUnit.SECONDS)
  .readTimeout(60, TimeUnit.SECONDS)
  ```

### "Build fails"
```bash
# Clean and rebuild
./gradlew clean build

# Or use Android Studio:
Build → Clean Project → Rebuild Project
```

## Development

### Viewing Logs
```bash
# Show all logs
adb logcat

# Filter by app tag
adb logcat | grep CoffeeShop

# Specific activity
adb logcat | grep CheckInActivity
```

### Common Gradle Tasks
```bash
./gradlew build              # Build APK
./gradlew test               # Run unit tests
./gradlew connectedAndroidTest  # Run instrumented tests
./gradlew clean              # Clean build
./gradlew lint               # Run linter
```

## Testing

### Manual Testing Checklist
- [ ] Login with demo credentials (admin)
- [ ] View combos list
- [ ] Check combo details display
- [ ] View orders list
- [ ] Update order status
- [ ] Grant camera permission
- [ ] Perform check-in
- [ ] Check-in photo captures
- [ ] Logout functionality works
- [ ] Back button handling
- [ ] Network error handling
- [ ] Permission denial handling

## Performance Tips

1. **Image Optimization**: Compress camera images before upload
2. **Network**: Use connection pooling (already configured)
3. **UI**: Use RecyclerView for large lists
4. **Storage**: Clear cache periodically

## Security Considerations

⚠️ **Development Only**
- Demo credentials are for testing
- Never hardcode real passwords
- Implement proper key storage for production

📱 **Production Recommendations**
- Use HTTPS instead of HTTP
- Implement certificate pinning
- Store tokens in Android Keystore
- Add input validation
- Implement request signing

## Extensions

### Add Product Management
```kotlin
// In ApiService
@POST("products")
fun createProduct(@Body product: Product): Call<Product>
```

### Add Real-Time Updates
```kotlin
// Use WebSocket for live order updates
// Consider using Firebase Cloud Messaging
```

### Add Analytics
```kotlin
// Add Firebase Analytics
// Track user actions and errors
```

## Resources

- [Android Development](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [AndroidX Camera](https://developer.android.com/training/camerax)

## Support

For issues:
1. Check backend logs
2. Review logcat output
3. Verify network connectivity
4. Check API endpoint correctness
5. Validate authentication token

## License

© 2025 Coffee Shop Admin. All rights reserved.

---

**Built with ❤️ using Kotlin & AndroidX**
