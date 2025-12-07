const express = require("express");
const router = express.Router();
const Attendance = require("../models/attendance.model");

// --- CHECK IN ---
router.post("/check-in", async (req, res) => {
  try {
    const { userId, username, fullName, photo } = req.body;

    if (!userId || !username) {
      return res.status(400).json({ error: "userId and username required" });
    }

    // Check if already checked in today
    const today = new Date().setHours(0, 0, 0, 0);
    const existingCheckIn = await Attendance.findOne({
      userId,
      date: { $gte: new Date(today) },
      status: "checked-in",
    });

    if (existingCheckIn) {
      return res.status(400).json({
        error: "❌ Bạn đã check-in rồi! Vui lòng check-out trước khi check-in lại.",
      });
    }

    const attendance = await Attendance.create({
      userId,
      username,
      fullName,
      checkInTime: new Date(),
      checkInPhoto: photo,
      status: "checked-in",
      date: new Date(today),
    });

    res.status(201).json({
      message: "✅ Check-in thành công!",
      data: attendance,
    });
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// --- CHECK OUT ---
router.post("/check-out", async (req, res) => {
  try {
    const { userId, photo } = req.body;

    if (!userId) {
      return res.status(400).json({ error: "userId required" });
    }

    // Find today's check-in
    const today = new Date().setHours(0, 0, 0, 0);
    const attendance = await Attendance.findOne({
      userId,
      date: { $gte: new Date(today) },
      status: "checked-in",
    });

    if (!attendance) {
      return res.status(404).json({
        error: "❌ Không tìm thấy check-in hôm nay! Vui lòng check-in trước.",
      });
    }

    attendance.checkOutTime = new Date();
    attendance.checkOutPhoto = photo;
    attendance.status = "checked-out";
    attendance.updatedAt = new Date();

    await attendance.save();

    // Calculate working hours
    const workingHours =
      (attendance.checkOutTime - attendance.checkInTime) / (1000 * 60 * 60);

    res.json({
      message: "✅ Check-out thành công!",
      data: attendance,
      workingHours: workingHours.toFixed(2),
    });
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// --- GET TODAY'S ATTENDANCE ---
router.get("/today/:userId", async (req, res) => {
  try {
    const { userId } = req.params;
    const today = new Date().setHours(0, 0, 0, 0);

    const attendance = await Attendance.findOne({
      userId,
      date: { $gte: new Date(today) },
    });

    if (!attendance) {
      return res.json({
        status: "not-checked-in",
        message: "Chưa check-in",
      });
    }

    res.json({
      status: attendance.status,
      data: attendance,
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// --- GET ATTENDANCE HISTORY ---
router.get("/history/:userId", async (req, res) => {
  try {
    const { userId } = req.params;
    const records = await Attendance.find({ userId }).sort({
      checkInTime: -1,
    });

    res.json(records);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// --- GET ALL ATTENDANCE (Admin) ---
router.get("/", async (req, res) => {
  try {
    const records = await Attendance.find().sort({ checkInTime: -1 });
    res.json(records);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
