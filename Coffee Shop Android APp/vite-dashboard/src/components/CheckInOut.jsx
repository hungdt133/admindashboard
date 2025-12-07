import React, { useRef, useState, useEffect } from "react";
import axios from "axios";
import "./CheckInOut.css";

const CheckInOut = ({ user }) => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [attendanceStatus, setAttendanceStatus] = useState(null);
  const [showCamera, setShowCamera] = useState(false);
  const [stream, setStream] = useState(null);
  const [cameraError, setCameraError] = useState("");

  const API_URL = "http://localhost:3000";

  // Get camera access with better error handling
  const startCamera = async () => {
    try {
      setCameraError("");
      const constraints = {
        video: {
          width: { ideal: 1280 },
          height: { ideal: 720 },
          facingMode: "user"
        },
        audio: false
      };

      const mediaStream = await navigator.mediaDevices.getUserMedia(constraints);
      
      if (videoRef.current) {
        videoRef.current.srcObject = mediaStream;
        setStream(mediaStream);
        setShowCamera(true);
      }
    } catch (err) {
      console.error("Camera error:", err);
      let errorMsg = "❌ Không thể truy cập camera! ";
      
      if (err.name === "NotAllowedError") {
        errorMsg += "Vui lòng cho phép quyền truy cập camera trong trình duyệt.";
      } else if (err.name === "NotFoundError") {
        errorMsg += "Không tìm thấy camera trên thiết bị này.";
      } else if (err.name === "NotReadableError") {
        errorMsg += "Camera đang được sử dụng bởi ứng dụng khác.";
      } else {
        errorMsg += err.message;
      }
      
      setCameraError(errorMsg);
      setMessage(errorMsg);
    }
  };

  // Stop camera
  const stopCamera = () => {
    if (stream) {
      stream.getTracks().forEach((track) => track.stop());
      setStream(null);
    }
    setShowCamera(false);
    setCameraError("");
  };

  // Capture photo
  const capturePhoto = () => {
    const canvas = canvasRef.current;
    const video = videoRef.current;

    if (canvas && video && video.readyState === video.HAVE_ENOUGH_DATA) {
      const ctx = canvas.getContext("2d");
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      ctx.drawImage(video, 0, 0);
      return canvas.toDataURL("image/jpeg", 0.8);
    }
    return null;
  };

  // Check current status
  useEffect(() => {
    fetchAttendanceStatus();
    
    // Check camera availability on mount
    checkCameraAvailability();
  }, []);

  const checkCameraAvailability = async () => {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      const videoDevices = devices.filter(device => device.kind === 'videoinput');
      if (videoDevices.length === 0) {
        setCameraError("⚠️ Không tìm thấy camera trên thiết bị");
      }
    } catch (err) {
      console.error("Error checking camera:", err);
    }
  };

  const fetchAttendanceStatus = async () => {
    try {
      const res = await axios.get(
        `${API_URL}/attendance/today/${user._id}`
      );
      setAttendanceStatus(res.data.status);
    } catch (err) {
      console.error("Error fetching status:", err);
    }
  };

  // Handle Check In
  const handleCheckIn = async () => {
    setLoading(true);
    setMessage("");

    try {
      const photo = capturePhoto();

      if (!photo) {
        setMessage("❌ Không thể chụp ảnh! Vui lòng kiểm tra camera.");
        setLoading(false);
        return;
      }

      const response = await axios.post(`${API_URL}/attendance/check-in`, {
        userId: user._id,
        username: user.username,
        fullName: user.fullName || user.name,
        photo,
      });

      setMessage(response.data.message);
      setAttendanceStatus("checked-in");
      stopCamera();
      fetchAttendanceStatus();
    } catch (err) {
      setMessage(err.response?.data?.error || "❌ Check-in thất bại!");
    } finally {
      setLoading(false);
    }
  };

  // Handle Check Out
  const handleCheckOut = async () => {
    setLoading(true);
    setMessage("");

    try {
      const photo = capturePhoto();

      if (!photo) {
        setMessage("❌ Không thể chụp ảnh! Vui lòng kiểm tra camera.");
        setLoading(false);
        return;
      }

      const response = await axios.post(`${API_URL}/attendance/check-out`, {
        userId: user._id,
        photo,
      });

      setMessage(response.data.message);
      setAttendanceStatus("checked-out");
      stopCamera();
      fetchAttendanceStatus();
    } catch (err) {
      setMessage(err.response?.data?.error || "❌ Check-out thất bại!");
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (date) => {
    return new Date(date).toLocaleString("vi-VN", {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  };

  return (
    <div className="checkinout-container">
      <h2 className="page-title">📸 Check In - Check Out (Face ID)</h2>

      <div className="status-card">
        <div className="status-info">
          <p>
            <strong>👤 Nhân viên:</strong> {user.fullName || user.name}
          </p>
          <p>
            <strong>📊 Trạng thái:</strong>{" "}
            <span
              className={
                attendanceStatus === "checked-in"
                  ? "status-badge checked-in"
                  : attendanceStatus === "checked-out"
                  ? "status-badge checked-out"
                  : "status-badge not-checked"
              }
            >
              {attendanceStatus === "checked-in"
                ? "✅ Đã Check-In"
                : attendanceStatus === "checked-out"
                ? "🏁 Đã Check-Out"
                : "⏳ Chưa Check-In"}
            </span>
          </p>
        </div>
      </div>

      {message && (
        <div
          className={
            message.includes("✅")
              ? "message success-message"
              : "message error-message"
          }
        >
          {message}
        </div>
      )}

      {cameraError && !showCamera && (
        <div className="message warning-message">
          {cameraError}
        </div>
      )}

      {!showCamera ? (
        <div className="action-buttons">
          {attendanceStatus !== "checked-in" && (
            <button
              className="btn-checkin"
              onClick={startCamera}
              disabled={loading}
            >
              📷 Check In
            </button>
          )}
          {attendanceStatus === "checked-in" && (
            <button
              className="btn-checkout"
              onClick={startCamera}
              disabled={loading}
            >
              🏁 Check Out
            </button>
          )}
        </div>
      ) : (
        <div className="camera-section">
          <div className="camera-container">
            <video
              ref={videoRef}
              autoPlay
              playsInline
              muted
              className="video-feed"
            />
            <canvas
              ref={canvasRef}
              style={{ display: "none" }}
            />
          </div>

          <div className="camera-buttons">
            {attendanceStatus !== "checked-in" ? (
              <button
                className="btn-capture"
                onClick={handleCheckIn}
                disabled={loading}
              >
                {loading ? "⏳ Đang xử lý..." : "📸 Chụp & Check In"}
              </button>
            ) : (
              <button
                className="btn-capture"
                onClick={handleCheckOut}
                disabled={loading}
              >
                {loading ? "⏳ Đang xử lý..." : "📸 Chụp & Check Out"}
              </button>
            )}
            <button className="btn-cancel" onClick={stopCamera} disabled={loading}>
              ❌ Hủy
            </button>
          </div>
        </div>
      )}

      <div className="info-box">
        <h3>📋 Hướng dẫn sử dụng:</h3>
        <ul>
          <li>Nhấn nút "Check In" để bắt đầu ca làm việc</li>
          <li>Cho phép quyền truy cập camera khi trình duyệt yêu cầu</li>
          <li>Chụp ảnh khuôn mặt rõ ràng khi hệ thống yêu cầu</li>
          <li>Nhấn "Check Out" khi kết thúc ca làm việc</li>
        </ul>
      </div>
    </div>
  );
};

export default CheckInOut;
