# Script kiểm tra Node.js và hướng dẫn cài đặt
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   KIỂM TRA NODE.JS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra Node.js
Write-Host "🔍 Đang kiểm tra Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version 2>$null
    $npmVersion = npm --version 2>$null
    
    if ($nodeVersion -and $npmVersion) {
        Write-Host "✅ Node.js đã được cài đặt!" -ForegroundColor Green
        Write-Host "   Node.js: $nodeVersion" -ForegroundColor Green
        Write-Host "   npm: $npmVersion" -ForegroundColor Green
        Write-Host ""
        Write-Host "🚀 Bạn có thể chạy backend bằng:" -ForegroundColor Cyan
        Write-Host "   cd Coffeshop-backend-app" -ForegroundColor White
        Write-Host "   npm install  (nếu chưa cài dependencies)" -ForegroundColor White
        Write-Host "   npm run dev" -ForegroundColor White
        exit 0
    }
} catch {
    # Continue to error message
}

Write-Host "❌ Node.js chưa được cài đặt hoặc chưa có trong PATH!" -ForegroundColor Red
Write-Host ""
Write-Host "📥 HƯỚNG DẪN CÀI ĐẶT:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Truy cập: https://nodejs.org/" -ForegroundColor White
Write-Host "2. Tải bản LTS (Long Term Support) - khuyến nghị" -ForegroundColor White
Write-Host "3. Chạy file .msi vừa tải" -ForegroundColor White
Write-Host "4. Trong quá trình cài đặt, ĐẢM BẢO chọn 'Add to PATH'" -ForegroundColor White
Write-Host "5. Sau khi cài xong, ĐÓNG và MỞ LẠI PowerShell/terminal" -ForegroundColor White
Write-Host "6. Chạy lại script này để kiểm tra" -ForegroundColor White
Write-Host ""
Write-Host "💡 Sau khi cài Node.js, bạn có thể:" -ForegroundColor Cyan
Write-Host "   - Chạy: .\setup.ps1  (để cài dependencies)" -ForegroundColor White
Write-Host "   - Chạy: .\start-servers.ps1  (để chạy cả FE và BE)" -ForegroundColor White
Write-Host ""

