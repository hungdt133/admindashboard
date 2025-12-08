package com.coffeeshop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coffeeshop.databinding.ActivityAdminDashboardBinding
import com.coffeeshop.fragments.OrdersFragment
import com.coffeeshop.fragments.CombosFragment

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load Orders fragment by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.contentFrame.id, OrdersFragment())
                .commit()
        }

        // Setup tab navigation
        binding.tabOrders.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.contentFrame.id, OrdersFragment())
                .commit()
            updateTabStyles(isOrdersTab = true)
        }

        binding.tabCombos.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(binding.contentFrame.id, CombosFragment())
                .commit()
            updateTabStyles(isOrdersTab = false)
        }

        binding.btnLogout.setOnClickListener {
            // Handle logout
            finish()
        }
    }

    private fun updateTabStyles(isOrdersTab: Boolean) {
        if (isOrdersTab) {
            binding.tabOrders.setBackgroundResource(com.coffeeshop.R.drawable.tab_active_background)
            binding.tabOrders.setTextColor(resources.getColor(android.R.color.holo_purple))
            binding.tabCombos.setBackgroundResource(com.coffeeshop.R.drawable.tab_inactive_background)
            binding.tabCombos.setTextColor(resources.getColor(android.R.color.darker_gray))
        } else {
            binding.tabOrders.setBackgroundResource(com.coffeeshop.R.drawable.tab_inactive_background)
            binding.tabOrders.setTextColor(resources.getColor(android.R.color.darker_gray))
            binding.tabCombos.setBackgroundResource(com.coffeeshop.R.drawable.tab_active_background)
            binding.tabCombos.setTextColor(resources.getColor(android.R.color.holo_purple))
        }
    }
}
