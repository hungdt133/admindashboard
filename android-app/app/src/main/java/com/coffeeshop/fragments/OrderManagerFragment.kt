package com.coffeeshop.fragments

import android.os.Bundle
import com.coffeeshop.dialogs.OrderDetailDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coffeeshop.R
import com.coffeeshop.adapters.OrderAdapter
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.FragmentOrderManagerBinding
import com.coffeeshop.models.Order
import com.coffeeshop.utils.SharedPrefs
import kotlinx.coroutines.launch

class OrderManagerFragment : Fragment() {
    private var _binding: FragmentOrderManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var sharedPrefs: SharedPrefs
    private var orders = mutableListOf<Order>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderManagerBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefs = SharedPrefs(requireContext())
        
        setupRecyclerView()
        fetchOrders()
        
        binding.swipeRefresh.setOnRefreshListener {
            fetchOrders()
        }
    }
    
    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(orders) { order ->
            showOrderDetailDialog(order)
        }
        
        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }
    
    private fun fetchOrders() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val token = sharedPrefs.getToken()
                if (token == null) {
                    Toast.makeText(requireContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                val apiService = RetrofitClient.getApiServiceWithToken(token)
                val response = apiService.getOrders()
                
                android.util.Log.d("OrderManager", "Response code: ${response.code()}")
                
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    android.util.Log.d("OrderManager", "Response body size: ${responseBody?.size ?: 0}")
                    
                    if (responseBody != null && responseBody.isNotEmpty()) {
                        orders.clear()
                        orders.addAll(responseBody.sortedByDescending { it.orderDate ?: "" })
                        android.util.Log.d("OrderManager", "Loaded ${orders.size} orders")
                        orderAdapter.notifyDataSetChanged()
                        
                        binding.tvEmpty.visibility = View.GONE
                        binding.recyclerViewOrders.visibility = View.VISIBLE
                    } else {
                        android.util.Log.d("OrderManager", "No orders found")
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.recyclerViewOrders.visibility = View.GONE
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("OrderManager", "Error: ${response.code()}, $errorBody")
                    Toast.makeText(
                        requireContext(),
                        "Lỗi tải đơn hàng: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Lỗi: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
    
    private fun showOrderDetailDialog(order: Order) {
        val dialog = OrderDetailDialog(order) {
            fetchOrders()
        }
        dialog.show(parentFragmentManager, "OrderDetailDialog")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

