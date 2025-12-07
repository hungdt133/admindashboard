package com.coffeeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.ActivityLoginBinding
import com.coffeeshop.models.LoginRequest
import com.coffeeshop.utils.SharedPrefs
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefs: SharedPrefs
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefs = SharedPrefs(this)
        
        // Check if already logged in
        if (sharedPrefs.isLoggedIn()) {
            navigateToMain()
            return
        }
        
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }
    
    private fun performLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.btnLogin.isEnabled = false
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(username, password)
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    val user = loginResponse.user
                    val token = loginResponse.token
                    
                    if (user == null || token == null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Dữ liệu đăng nhập không hợp lệ",
                            Toast.LENGTH_LONG
                        ).show()
                        return@launch
                    }
                    
                    // Check if user is admin or staff
                    if (user.role == "admin" || user.role == "staff") {
                        // Save credentials
                        sharedPrefs.saveToken(token)
                        sharedPrefs.saveUser(
                            user._id ?: "",
                            user.name ?: "",
                            user.role ?: ""
                        )
                        
                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        
                        navigateToMain()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Bạn không có quyền truy cập! Chỉ admin và staff được phép.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Đăng nhập thất bại"
                    Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Lỗi kết nối: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.btnLogin.isEnabled = true
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

