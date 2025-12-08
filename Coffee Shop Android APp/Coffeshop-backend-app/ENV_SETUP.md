# Hướng dẫn cấu hình file .env

Tạo file `.env` trong thư mục `Coffeshop-backend-app` với nội dung sau:

```env
# MongoDB Connection String
# Thay đổi thành connection string MongoDB của bạn
# Ví dụ local: mongodb://localhost:27017/coffeeshop
# Hoặc MongoDB Atlas: mongodb+srv://username:password@cluster.mongodb.net/coffeeshop
MONGO_URI=mongodb://localhost:27017/coffeeshop

# JWT Secret Key (dùng để mã hóa token đăng nhập)
# Nên đặt một chuỗi ngẫu nhiên, bảo mật
JWT_SECRET=your_secret_key_here_change_this_to_something_secure

# Server Port (tùy chọn, mặc định là 3000)
PORT=3000
```

## Các biến môi trường:

- **MONGO_URI** (BẮT BUỘC): Connection string để kết nối MongoDB
- **JWT_SECRET** (KHUYẾN NGHỊ): Secret key để mã hóa JWT token. Nếu không có, sẽ dùng giá trị mặc định (không an toàn cho production)
- **PORT** (TÙY CHỌN): Port cho server, mặc định là 3000

