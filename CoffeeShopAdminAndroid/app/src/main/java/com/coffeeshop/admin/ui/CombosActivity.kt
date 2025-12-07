package com.coffeeshop.admin.ui

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coffeeshop.admin.R
import com.coffeeshop.admin.models.Combo
import com.coffeeshop.admin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context

class CombosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var combosAdapter: CombosAdapter
    private var combosList = mutableListOf<Combo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combos)

        initializeViews()
        fetchCombos()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)

        combosAdapter = CombosAdapter(combosList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = combosAdapter
    }

    private fun fetchCombos() {
        progressBar.visibility = android.view.View.VISIBLE

        val token = getStoredToken() ?: return
        val authHeader = "Bearer $token"

        ApiClient.apiService.getCombos(authHeader).enqueue(object : Callback<List<Combo>> {
            override fun onResponse(call: Call<List<Combo>>, response: Response<List<Combo>>) {
                progressBar.visibility = android.view.View.GONE

                if (response.isSuccessful) {
                    val combos = response.body() ?: emptyList()
                    combosList.clear()
                    combosList.addAll(combos)
                    combosAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@CombosActivity, "Failed to load combos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Combo>>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                Toast.makeText(
                    this@CombosActivity,
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
