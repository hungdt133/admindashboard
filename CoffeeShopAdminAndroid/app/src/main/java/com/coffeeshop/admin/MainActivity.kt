package com.coffeeshop.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.coffeeshop.admin.ui.LoginActivity
import com.coffeeshop.admin.ui.CombosActivity
import com.coffeeshop.admin.ui.OrdersActivity
import com.coffeeshop.admin.ui.CheckInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Context
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.stringPreferencesKey
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var container: FrameLayout
    
    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USER_KEY = "user_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is logged in
        val token = getStoredToken()
        
        if (token.isNullOrEmpty()) {
            // Navigate to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)
            initializeNavigation()
        }
    }

    private fun initializeNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        container = findViewById(R.id.container)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_combos -> {
                    startActivity(Intent(this, CombosActivity::class.java))
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                }
                R.id.nav_checkin -> {
                    startActivity(Intent(this, CheckInActivity::class.java))
                }
                R.id.nav_logout -> {
                    logout()
                }
            }
            true
        }
    }

    private fun logout() {
        clearStoredData()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getStoredToken(): String? {
        return getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString(TOKEN_KEY, null)
    }

    private fun clearStoredData() {
        getSharedPreferences("auth", Context.MODE_PRIVATE).edit {
            remove(TOKEN_KEY)
            remove(USER_KEY)
        }
    }
}
