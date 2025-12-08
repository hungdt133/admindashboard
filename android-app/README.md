# Coffee Shop Android App

Ứng dụng Android quản lý đơn hàng và combo cho Coffee Shop, được phát triển bằng Kotlin.

## Tính năng

- ✅ Đăng nhập với xác thực JWT
- ✅ Quản lý đơn hàng (xem danh sách, cập nhật trạng thái)
- ✅ Quản lý combo (thêm, sửa, xóa)
- ✅ Đăng ký Face ID (chỉ admin)

## Yêu cầu

- Android Studio Hedgehog | 2023.1.1 trở lên
- Android SDK 24 (Android 7.0) trở lên
- Kotlin 1.9.20
- Backend server đang chạy tại `http://localhost:3000`

## Cài đặt

1. Mở project trong Android Studio
2. Sync Gradle files
3. Cấu hình IP backend:
   - Emulator: Sử dụng `10.0.2.2:3000` (đã cấu hình sẵn)
   - Thiết bị thật: Thay đổi `BASE_URL` trong `RetrofitClient.kt` thành IP máy tính của bạn (ví dụ: `http://192.168.1.100:3000/`)
4. Build và chạy ứng dụng

## Cấu trúc Project

```
app/
├── src/main/
│   ├── java/com/coffeeshop/
│   │   ├── api/          # API service và Retrofit client
│   │   ├── models/        # Data models
│   │   ├── fragments/    # Fragments cho Order và Combo
│   │   ├── adapters/     # RecyclerView adapters
│   │   ├── utils/        # SharedPreferences helper
│   │   ├── LoginActivity.kt
│   │   ├── MainActivity.kt
│   │   └── FaceEnrollmentActivity.kt
│   ├── res/
│   │   ├── layout/       # XML layouts
│   │   ├── menu/         # Menu resources
│   │   └── values/       # Strings, colors, themes
│   └── AndroidManifest.xml
```

## Lưu ý

- Đảm bảo backend server đang chạy trước khi sử dụng app
- Đối với thiết bị thật, cần cấu hình đúng IP backend và cho phép cleartext traffic
- Face ID enrollment chỉ dành cho admin

## License

MIT

