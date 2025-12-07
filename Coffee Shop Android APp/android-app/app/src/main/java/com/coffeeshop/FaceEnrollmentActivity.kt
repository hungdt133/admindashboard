package com.coffeeshop

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.ActivityFaceEnrollmentBinding
import com.coffeeshop.models.FaceEnrollRequest
import com.coffeeshop.utils.SharedPrefs
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceEnrollmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaceEnrollmentBinding
    private lateinit var sharedPrefs: SharedPrefs
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var faceDetector: FaceDetector
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceEnrollmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefs = SharedPrefs(this)
        
        if (!sharedPrefs.isAdmin()) {
            Toast.makeText(this, "Chỉ admin mới có quyền đăng ký Face ID", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        setupFaceDetector()
        setupCamera()
        
        binding.btnCapture.setOnClickListener {
            captureImage()
        }
        
        cameraExecutor = Executors.newSingleThreadExecutor()
    }
    
    private fun setupFaceDetector() {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
        faceDetector = FaceDetection.getClient(options)
    }
    
    private fun setupCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }
    
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            
            imageCapture = ImageCapture.Builder().build()
            
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
                Toast.makeText(this, "Lỗi khởi động camera: ${exc.message}", Toast.LENGTH_LONG).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun captureImage() {
        val imageCapture = imageCapture ?: return
        
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            getOutputFile()
        ).build()
        
        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@FaceEnrollmentActivity,
                        "Lỗi chụp ảnh: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // TODO: Process image and enroll face
                    Toast.makeText(
                        this@FaceEnrollmentActivity,
                        "Ảnh đã được chụp. Đang xử lý...",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Process face enrollment here
                }
            }
        )
    }
    
    private fun getOutputFile() = java.io.File(
        getExternalFilesDir(null),
        "face_enrollment_${System.currentTimeMillis()}.jpg"
    )
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Cần quyền truy cập camera để đăng ký Face ID",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        faceDetector.close()
    }
    
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

