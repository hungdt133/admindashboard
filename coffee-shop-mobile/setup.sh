#!/bin/bash
# Quick Start Script for Coffee Shop Mobile App

echo "======================================"
echo "Coffee Shop Admin - Mobile App Setup"
echo "======================================"
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js first."
    exit 1
fi

echo "✅ Node.js found: $(node -v)"
echo ""

# Navigate to project
cd "$(dirname "$0")"

echo "📦 Installing dependencies..."
npm install

if [ $? -eq 0 ]; then
    echo "✅ Dependencies installed successfully!"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

echo ""
echo "======================================"
echo "Setup Complete!"
echo "======================================"
echo ""
echo "📋 IMPORTANT: Before running the app:"
echo ""
echo "1. Update Backend IP Address in:"
echo "   - app/(tabs)/index.tsx"
echo "   - app/(tabs)/combos.tsx"
echo "   - app/(tabs)/orders.tsx"
echo "   - app/(tabs)/checkin.tsx"
echo ""
echo "   Find your IP with: ipconfig"
echo "   Set: const API_URL = 'http://YOUR_IP:3000';"
echo ""
echo "2. Start Backend Server:"
echo "   cd ../Coffeshop-backend-app"
echo "   npm start"
echo ""
echo "3. Start Mobile App:"
echo "   npm start"
echo ""
echo "4. Run on Android:"
echo "   Press 'a' for Android Emulator"
echo ""
echo "📚 For detailed instructions, read:"
echo "   - SETUP_GUIDE.md"
echo "   - MOBILE_README.md"
echo ""
echo "👤 Demo Credentials:"
echo "   Admin: nguyenvana / admin"
echo "   Staff: tranthib / staff"
echo ""
echo "======================================"
