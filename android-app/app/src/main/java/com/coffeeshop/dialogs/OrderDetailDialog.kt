package com.coffeeshop.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.coffeeshop.R
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.DialogOrderDetailBinding
import com.coffeeshop.models.Order
import com.coffeeshop.models.StatusUpdateRequest
import com.coffeeshop.utils.SharedPrefs
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class OrderDetailDialog(
    private val order: Order,
    private val onUpdateSuccess: () -> Unit
) : DialogFragment() {
    
    private var _binding: DialogOrderDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefs: SharedPrefs
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefs = SharedPrefs(requireContext())
        
        setupOrderInfo()
        setupStatusSpinner()
        
        binding.btnUpdateStatus.setOnClickListener {
            updateOrderStatus()
        }
        
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
    
    private fun setupOrderInfo() {
        binding.tvOrderId.text = "#${order._id.takeLast(6).uppercase()}"
        
        val address = order.deliveryAddress
        binding.tvCustomerName.text = address?.fullName ?: "Khách vãng lai"
        binding.tvPhone.text = address?.phone ?: "N/A"
        binding.tvAddress.text = buildString {
            append(address?.street ?: "")
            if (!address?.ward.isNullOrBlank()) append(", ${address?.ward}")
            if (!address?.district.isNullOrBlank()) append(", ${address?.district}")
            if (!address?.city.isNullOrBlank()) append(", ${address?.city}")
            if (isEmpty()) append("N/A")
        }
        binding.tvNote.text = order.note ?: "Không có"
        
        // Order items
        val items = order.items ?: emptyList()
        
        val itemsText = StringBuilder()
        if (items.isEmpty()) {
            itemsText.append("Không có món nào")
        } else {
            items.forEachIndexed { index, item ->
                itemsText.append("${index + 1}. ${item.productName ?: "N/A"} x${item.quantity ?: 1}")
                if (!item.sizeChosen.isNullOrBlank()) {
                    itemsText.append(" (Size: ${item.sizeChosen})")
                }
                itemsText.append("\n")
                if (!item.iceLevel.isNullOrBlank() && item.iceLevel != "N/A") {
                    itemsText.append("   • Đá: ${item.iceLevel}")
                }
                if (!item.sugarLevel.isNullOrBlank() && item.sugarLevel != "N/A") {
                    itemsText.append(" • Đường: ${item.sugarLevel}")
                }
                if (!item.itemNote.isNullOrBlank()) {
                    itemsText.append("\n   Ghi chú: ${item.itemNote}")
                }
                itemsText.append("\n\n")
            }
        }
        binding.tvItemsDetail.text = itemsText.toString()
        
        // Summary
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.tvSubtotal.text = currencyFormat.format(order.subtotal ?: 0.0)
        binding.tvShippingFee.text = currencyFormat.format(order.shippingFee ?: 0.0)
        binding.tvDiscount.text = currencyFormat.format(order.discountAmount ?: 0.0)
        binding.tvTotal.text = currencyFormat.format(order.totalAmount ?: 0.0)
        
        binding.tvCurrentStatus.text = order.status ?: "Pending"
    }
    
    private fun setupStatusSpinner() {
        val statuses = arrayOf(
            "Pending",
            "Confirmed",
            "Delivering",
            "Delivered",
            "Completed",
            "Cancelled"
        )
        
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter
        
        // Set current status
        val currentStatus = order.status ?: "Pending"
        val position = statuses.indexOf(currentStatus)
        if (position >= 0) {
            binding.spinnerStatus.setSelection(position)
        }
    }
    
    private fun updateOrderStatus() {
        val selectedStatus = binding.spinnerStatus.selectedItem as String
        
        binding.btnUpdateStatus.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val token = sharedPrefs.getToken()
                if (token == null) {
                    Toast.makeText(requireContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                val apiService = RetrofitClient.getApiServiceWithToken(token)
                val response = apiService.updateOrderStatus(
                    order._id,
                    StatusUpdateRequest(selectedStatus)
                )
                
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Đã cập nhật trạng thái: $selectedStatus",
                        Toast.LENGTH_SHORT
                    ).show()
                    onUpdateSuccess()
                    dismiss()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        requireContext(),
                        "Lỗi: $errorBody",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Lỗi: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.btnUpdateStatus.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

