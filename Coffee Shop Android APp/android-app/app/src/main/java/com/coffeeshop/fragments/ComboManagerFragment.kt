package com.coffeeshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.coffeeshop.R
import com.coffeeshop.adapters.ComboAdapter
import com.coffeeshop.api.RetrofitClient
import com.coffeeshop.databinding.FragmentComboManagerBinding
import com.coffeeshop.dialogs.ComboDialog
import com.coffeeshop.models.Combo
import com.coffeeshop.utils.SharedPrefs
import kotlinx.coroutines.launch

class ComboManagerFragment : Fragment() {
    private var _binding: FragmentComboManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var comboAdapter: ComboAdapter
    private lateinit var sharedPrefs: SharedPrefs
    private var combos = mutableListOf<Combo>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComboManagerBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefs = SharedPrefs(requireContext())
        
        setupRecyclerView()
        fetchCombos()
        
        binding.fabAddCombo.setOnClickListener {
            showComboDialog(null)
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            fetchCombos()
        }
    }
    
    private fun setupRecyclerView() {
        comboAdapter = ComboAdapter(
            combos,
            onEditClick = { combo ->
                showComboDialog(combo)
            },
            onDeleteClick = { combo ->
                deleteCombo(combo)
            }
        )
        
        binding.recyclerViewCombos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = comboAdapter
        }
    }
    
    private fun fetchCombos() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getCombos()
                
                android.util.Log.d("ComboManager", "Response code: ${response.code()}")
                android.util.Log.d("ComboManager", "Response isSuccessful: ${response.isSuccessful}")
                
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    android.util.Log.d("ComboManager", "Response body: $responseBody")
                    
                    if (responseBody != null && responseBody.isNotEmpty()) {
                        combos.clear()
                        combos.addAll(responseBody)
                        android.util.Log.d("ComboManager", "Loaded ${combos.size} combos")
                        
                        // Log first combo for debugging
                        if (combos.isNotEmpty()) {
                            val first = combos[0]
                            android.util.Log.d("ComboManager", "First combo: name=${first.name}, price=${first.basePrice}, image=${first.image_url}")
                        }
                        
                        comboAdapter.notifyDataSetChanged()
                        
                        binding.tvEmpty.visibility = View.GONE
                        binding.recyclerViewCombos.visibility = View.VISIBLE
                    } else {
                        android.util.Log.d("ComboManager", "Response body is null or empty")
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.recyclerViewCombos.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Không có combo nào",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("ComboManager", "Error response: $errorBody")
                    Toast.makeText(
                        requireContext(),
                        "Lỗi tải combo: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                android.util.Log.e("ComboManager", "Exception: ${e.message}", e)
                Toast.makeText(
                    requireContext(),
                    "Lỗi kết nối: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }
    
    private fun deleteCombo(combo: Combo) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteCombo(combo._id)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Đã xóa combo", Toast.LENGTH_SHORT).show()
                    fetchCombos()
                } else {
                    Toast.makeText(requireContext(), "Lỗi xóa combo", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showComboDialog(combo: Combo?) {
        val dialog = ComboDialog(combo) {
            fetchCombos()
        }
        dialog.show(parentFragmentManager, "ComboDialog")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

