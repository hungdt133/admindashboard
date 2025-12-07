package com.coffeeshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coffeeshop.R
import com.coffeeshop.models.Order
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val orders: List<Order>,
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
        holder.itemView.setOnClickListener { onItemClick(order) }
    }
    
    override fun getItemCount() = orders.size
    
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvCustomer: TextView = itemView.findViewById(R.id.tvCustomer)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        
        fun bind(order: Order) {
            tvOrderId.text = "#${order._id.takeLast(6).uppercase()}"
            tvDate.text = try {
                order.orderDate?.let { dateStr ->
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        .parse(dateStr)?.let {
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi")).format(it)
                        } ?: dateStr
                } ?: "N/A"
            } catch (e: Exception) {
                order.orderDate ?: "N/A"
            }
            tvCustomer.text = order.deliveryAddress?.fullName ?: "Khách vãng lai"
            tvTotal.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.totalAmount ?: 0.0)
            tvStatus.text = order.status ?: "Pending"
        }
    }
}

