# Script to start both backend and frontend servers
Write-Host "🚀 Starting Backend Server..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'Coffeshop-backend-app'; npm run dev"

Start-Sleep -Seconds 2

Write-Host "🚀 Starting Frontend Server..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'vite-dashboard'; npm run dev"

Write-Host "✅ Both servers are starting in separate windows!" -ForegroundColor Yellow
Write-Host "Backend: http://localhost:3000" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Cyan

