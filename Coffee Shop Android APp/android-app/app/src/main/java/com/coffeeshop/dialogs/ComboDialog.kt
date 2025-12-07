package com.coffeeshop.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.coffeeshop.R
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.DialogComboBinding
import com.coffeeshop.models.Combo
import com.coffeeshop.models.ComboItem
import com.coffeeshop.models.ComboRequest
import kotlinx.coroutines.launch

class ComboDialog(
    private val combo: Combo? = null,
    private val onSaveSuccess: () -> Unit
) : DialogFragment() {
    
    private var _binding: DialogComboBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogComboBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (combo != null) {
            // Edit mode
            binding.tvTitle.text = "Sửa Combo"
            binding.etName.setText(combo.name ?: "")
            binding.etDescription.setText(combo.description ?: "")
            binding.etCategory.setText(combo.category ?: "")
            binding.etBasePrice.setText((combo.basePrice ?: 0.0).toString())
            binding.etDiscount.setText((combo.discount ?: 0.0).toString())
            binding.etImageUrl.setText(combo.image_url ?: "")
            
            // Load items
            combo.items?.forEach { item ->
                addItemRow(item.productName ?: "", item.quantity ?: 1)
            }
        } else {
            // Add mode
            binding.tvTitle.text = "Thêm Combo Mới"
        }
        
        binding.btnAddItem.setOnClickListener {
            addItemRow("", 1)
        }
        
        binding.btnSave.setOnClickListener {
            saveCombo()
        }
        
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
    
    private fun addItemRow(productName: String = "", quantity: Int = 1) {
        val itemView = layoutInflater.inflate(R.layout.item_combo_item_input, binding.containerItems, false)
        val etProductName = itemView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etProductName)
        val etQuantity = itemView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etQuantity)
        val btnRemove = itemView.findViewById<android.widget.Button>(R.id.btnRemoveItem)
        
        etProductName?.setText(productName)
        etQuantity?.setText(quantity.toString())
        
        btnRemove.setOnClickListener {
            binding.containerItems.removeView(itemView)
        }
        
        binding.containerItems.addView(itemView)
    }
    
    private fun saveCombo() {
        val name = binding.etName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val basePriceStr = binding.etBasePrice.text.toString().trim()
        val discountStr = binding.etDiscount.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()
        
        if (name.isEmpty() || basePriceStr.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập tên và giá", Toast.LENGTH_SHORT).show()
            return
        }
        
        val basePrice = basePriceStr.toDoubleOrNull() ?: 0.0
        val discount = discountStr.toDoubleOrNull() ?: 0.0
        
        // Collect items
        val items = mutableListOf<ComboItem>()
        for (i in 0 until binding.containerItems.childCount) {
            val itemView = binding.containerItems.getChildAt(i)
            val etProductName = itemView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etProductName)
            val etQuantity = itemView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etQuantity)
            
            val productName = etProductName?.text?.toString()?.trim() ?: ""
            val quantity = etQuantity?.text?.toString()?.toIntOrNull() ?: 1
            
            if (productName.isNotEmpty()) {
                items.add(ComboItem(productName = productName, quantity = quantity))
            }
        }
        
        val comboRequest = ComboRequest(
            name = name,
            description = description,
            category = category,
            basePrice = basePrice,
            discount = discount,
            image_url = if (imageUrl.isEmpty()) null else imageUrl,
            items = items
        )
        
        binding.btnSave.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = if (combo != null) {
                    RetrofitClient.apiService.updateCombo(combo._id, comboRequest)
                } else {
                    RetrofitClient.apiService.createCombo(comboRequest)
                }
                
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        if (combo != null) "Đã cập nhật combo" else "Đã tạo combo mới",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSaveSuccess()
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
                binding.btnSave.isEnabled = true
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

