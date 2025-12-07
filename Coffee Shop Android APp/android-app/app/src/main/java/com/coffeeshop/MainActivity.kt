package com.coffeeshop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.coffeeshop.databinding.ActivityMainBinding
import com.coffeeshop.fragments.ComboManagerFragment
import com.coffeeshop.fragments.OrderManagerFragment
import com.coffeeshop.utils.SharedPrefs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPrefs
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefs = SharedPrefs(this)
        
        // Check login
        if (!sharedPrefs.isLoggedIn()) {
            navigateToLogin()
            return
        }
        
        setupBottomNavigation()
        loadFragment(OrderManagerFragment())
        
        supportActionBar?.title = "☕ Coffee Shop Admin"
        supportActionBar?.subtitle = "${sharedPrefs.getUserName()} (${sharedPrefs.getUserRole()?.uppercase()})"
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_orders -> {
                    loadFragment(OrderManagerFragment())
                    true
                }
                R.id.nav_combos -> {
                    loadFragment(ComboManagerFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        
        // Show Face Enrollment only for admin
        val faceEnrollmentItem = menu?.findItem(R.id.menu_face_enrollment)
        faceEnrollmentItem?.isVisible = sharedPrefs.isAdmin()
        
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_face_enrollment -> {
                if (sharedPrefs.isAdmin()) {
                    val intent = Intent(this, FaceEnrollmentActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun logout() {
        sharedPrefs.clear()
        navigateToLogin()
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

