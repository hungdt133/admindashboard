package com.coffeeshop.admin.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.coffeeshop.admin.MainActivity
import com.coffeeshop.admin.R
import com.coffeeshop.admin.network.ApiClient
import com.coffeeshop.admin.network.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var demoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        progressBar = findViewById(R.id.progress_bar)
        demoTextView = findViewById(R.id.demo_text)

        demoTextView.text = """
            Demo Credentials:
            Admin: nguyenvana / admin
            Staff: tranthib / staff
        """.trimIndent()
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = android.view.View.VISIBLE
        loginButton.isEnabled = false

        val loginRequest = LoginRequest(username, password)

        ApiClient.apiService.login(loginRequest).enqueue(object : Callback<com.coffeeshop.admin.network.LoginResponse> {
            override fun onResponse(
                call: Call<com.coffeeshop.admin.network.LoginResponse>,
                response: Response<com.coffeeshop.admin.network.LoginResponse>
            ) {
                progressBar.visibility = android.view.View.GONE
                loginButton.isEnabled = true

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Save token and user data
                        saveAuthData(loginResponse.token, loginResponse.user.toString())

                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        
                        // Navigate to main activity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Login failed"
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.coffeeshop.admin.network.LoginResponse>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                loginButton.isEnabled = true
                Toast.makeText(
                    this@LoginActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveAuthData(token: String, userData: String) {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("auth_token", token)
            putString("user_data", userData)
            apply()
        }
    }
}
