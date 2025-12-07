package com.coffeeshop.admin.ui

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coffeeshop.admin.R
import com.coffeeshop.admin.models.Order
import com.coffeeshop.admin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context

class OrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var ordersAdapter: OrdersAdapter
    private var ordersList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        initializeViews()
        fetchOrders()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)

        ordersAdapter = OrdersAdapter(ordersList) { orderId, newStatus ->
            updateOrderStatus(orderId, newStatus)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ordersAdapter
    }

    private fun fetchOrders() {
        progressBar.visibility = android.view.View.VISIBLE

        val token = getStoredToken() ?: return
        val authHeader = "Bearer $token"

        ApiClient.apiService.getOrders(authHeader).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                progressBar.visibility = android.view.View.GONE

                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    ordersList.clear()
                    ordersList.addAll(orders)
                    ordersAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@OrdersActivity, "Failed to load orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                Toast.makeText(
                    this@OrdersActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateOrderStatus(orderId: String, newStatus: String) {
        val token = getStoredToken() ?: return
        val authHeader = "Bearer $token"

        val statusUpdate = com.coffeeshop.admin.network.StatusUpdate(newStatus)

        ApiClient.apiService.updateOrderStatus(orderId, statusUpdate, authHeader)
            .enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@OrdersActivity, "Order updated", Toast.LENGTH_SHORT).show()
                        fetchOrders() // Refresh the list
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Toast.makeText(
                        this@OrdersActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun getStoredToken(): String? {
        return getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("auth_token", null)
    }
}
