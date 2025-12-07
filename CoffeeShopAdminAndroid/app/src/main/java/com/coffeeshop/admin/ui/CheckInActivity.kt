package com.coffeeshop.admin.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.coffeeshop.admin.R
import com.coffeeshop.admin.network.ApiClient
import com.coffeeshop.admin.network.CheckInRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CheckInActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var captureButton: Button
    private lateinit var openCameraButton: Button
    private lateinit var instructionsTextView: TextView
    private lateinit var lastCheckInTextView: TextView
    private lateinit var progressBar: ProgressBar

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        initializeViews()
        cameraExecutor = Executors.newSingleThreadExecutor()
        setupListeners()
    }

    private fun initializeViews() {
        previewView = findViewById(R.id.preview_view)
        captureButton = findViewById(R.id.capture_button)
        openCameraButton = findViewById(R.id.open_camera_button)
        instructionsTextView = findViewById(R.id.instructions)
        lastCheckInTextView = findViewById(R.id.last_checkin)
        progressBar = findViewById(R.id.progress_bar)

        captureButton.visibility = android.view.View.GONE
        previewView.visibility = android.view.View.GONE

        instructionsTextView.text = """
            Instructions:
            1. Tap "Open Camera for Check-In"
            2. Position your face in the camera
            3. Tap the camera icon to take a photo
            4. System will process your attendance
        """.trimIndent()
    }

    private fun setupListeners() {
        openCameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE
                )
            }
        }

        captureButton.setOnClickListener {
            captureImage()
        }
    }

    private fun startCamera() {
        openCameraButton.visibility = android.view.View.GONE
        previewView.visibility = android.view.View.VISIBLE
        captureButton.visibility = android.view.View.VISIBLE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CheckInActivity", "Use case binding failed", exc)
                Toast.makeText(this, "Failed to open camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {
                    val bitmap = image.image?.let { convertYUVToRGB(it) }
                    image.close()

                    if (bitmap != null) {
                        performCheckIn(bitmap)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("CheckInActivity", "Photo capture failed: ${exc.message}", exc)
                    Toast.makeText(
                        this@CheckInActivity,
                        "Failed to capture photo: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun convertYUVToRGB(image: android.media.Image): Bitmap {
        val nv21 = ByteArray(image.width * image.height * 3 / 2)
        val planes = image.planes
        var pixelStride = planes[1].pixelStride

        if (pixelStride == 1) {
            val buffer = planes[0].buffer
            buffer.get(nv21, 0, nv21.size)
        } else {
            val pixelBuffer = planes[1].buffer
            val packed = ByteArray(pixelBuffer.remaining())
            pixelBuffer.get(packed)

            val uvPixelStride = planes[1].pixelStride
            if (uvPixelStride == 1) {
                System.arraycopy(packed, 0, nv21, image.width * image.height, packed.size)
            } else {
                for (i in packed.indices step uvPixelStride) {
                    nv21[image.width * image.height + i / 2] = packed[i]
                }
            }
        }

        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        decodeYUV420SP(nv21, image.width, image.height, bitmap)
        
        return bitmap
    }

    private fun decodeYUV420SP(nv21: ByteArray, width: Int, height: Int, bitmap: Bitmap) {
        val frameSize = width * height
        val rgb = IntArray(frameSize)

        for (j in 0 until height) {
            for (i in 0 until width) {
                val y = (nv21[j * width + i].toInt() and 0xff)
                val uvPixelStride = if (i % 2 == 0) 0 else 2
                val u = (nv21[frameSize + (j / 2) * width + i - uvPixelStride].toInt() and 0xff) - 128
                val v = (nv21[frameSize + (j / 2) * width + i - uvPixelStride + 1].toInt() and 0xff) - 128

                var r = (y + 1.402f * v).toInt()
                var g = (y - 0.344f * u - 0.714f * v).toInt()
                var b = (y + 1.772f * u).toInt()

                r = when {
                    r > 255 -> 255
                    r < 0 -> 0
                    else -> r
                }
                g = when {
                    g > 255 -> 255
                    g < 0 -> 0
                    else -> g
                }
                b = when {
                    b > 255 -> 255
                    b < 0 -> 0
                    else -> b
                }

                rgb[j * width + i] = -0x1000000 or (r shl 16) or (g shl 8) or b
            }
        }

        bitmap.setPixels(rgb, 0, width, 0, 0, width, height)
    }

    private fun performCheckIn(bitmap: Bitmap) {
        progressBar.visibility = android.view.View.VISIBLE
        captureButton.isEnabled = false

        // Convert bitmap to base64
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
        val photoDataUrl = "data:image/jpg;base64,$base64Image"

        val token = getStoredToken() ?: return
        val authHeader = "Bearer $token"

        val checkInRequest = CheckInRequest(
            employeeId = getStoredUserId() ?: "",
            photo = photoDataUrl
        )

        ApiClient.apiService.checkIn(checkInRequest, authHeader)
            .enqueue(object : Callback<com.coffeeshop.admin.network.AttendanceResponse> {
                override fun onResponse(
                    call: Call<com.coffeeshop.admin.network.AttendanceResponse>,
                    response: Response<com.coffeeshop.admin.network.AttendanceResponse>
                ) {
                    progressBar.visibility = android.view.View.GONE
                    captureButton.isEnabled = true

                    if (response.isSuccessful) {
                        val attendanceResponse = response.body()
                        if (attendanceResponse != null) {
                            lastCheckInTextView.text = "Check-In: ${attendanceResponse.status} at ${attendanceResponse.timestamp}"
                            Toast.makeText(this@CheckInActivity, "Check-in successful!", Toast.LENGTH_SHORT).show()
                            
                            // Close camera
                            openCameraButton.visibility = android.view.View.VISIBLE
                            previewView.visibility = android.view.View.GONE
                            captureButton.visibility = android.view.View.GONE
                        }
                    } else {
                        Toast.makeText(this@CheckInActivity, "Check-in failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<com.coffeeshop.admin.network.AttendanceResponse>,
                    t: Throwable
                ) {
                    progressBar.visibility = android.view.View.GONE
                    captureButton.isEnabled = true
                    Toast.makeText(
                        this@CheckInActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getStoredToken(): String? {
        return getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("auth_token", null)
    }

    private fun getStoredUserId(): String? {
        return getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("user_id", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
