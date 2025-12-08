# Script tự động setup và chạy Backend + Frontend
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   SETUP COFFEE SHOP PROJECT" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra Node.js đã cài chưa
Write-Host "🔍 Kiểm tra Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version
    $npmVersion = npm --version
    Write-Host "✅ Node.js: $nodeVersion" -ForegroundColor Green
    Write-Host "✅ npm: $npmVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Node.js chưa được cài đặt!" -ForegroundColor Red
    Write-Host "Vui lòng cài Node.js từ: https://nodejs.org/" -ForegroundColor Yellow
    Write-Host "Sau đó chạy lại script này." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "📦 Cài đặt dependencies cho Backend..." -ForegroundColor Yellow
Set-Location "Coffeshop-backend-app"
if (Test-Path "node_modules") {
    Write-Host "⚠️  node_modules đã tồn tại, bỏ qua..." -ForegroundColor Yellow
} else {
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Lỗi khi cài dependencies cho Backend!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Write-Host "✅ Backend dependencies đã được cài đặt!" -ForegroundColor Green
}

Write-Host ""
Write-Host "📦 Cài đặt dependencies cho Frontend..." -ForegroundColor Yellow
Set-Location "..\vite-dashboard"
if (Test-Path "node_modules") {
    Write-Host "⚠️  node_modules đã tồn tại, bỏ qua..." -ForegroundColor Yellow
} else {
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Lỗi khi cài dependencies cho Frontend!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Write-Host "✅ Frontend dependencies đã được cài đặt!" -ForegroundColor Green
}

Set-Location ..

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ SETUP HOÀN TẤT!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📝 Lưu ý:" -ForegroundColor Yellow
Write-Host "   - Đảm bảo bạn đã tạo file .env trong thư mục Coffeshop-backend-app" -ForegroundColor Yellow
Write-Host "   - File .env cần có: MONGO_URI, JWT_SECRET, PORT (tùy chọn)" -ForegroundColor Yellow
Write-Host ""
Write-Host "🚀 Để chạy servers, sử dụng:" -ForegroundColor Cyan
Write-Host "   .\start-servers.ps1" -ForegroundColor White
Write-Host ""

