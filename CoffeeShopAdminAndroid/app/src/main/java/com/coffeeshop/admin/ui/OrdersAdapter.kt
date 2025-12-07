package com.coffeeshop.admin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coffeeshop.admin.R
import com.coffeeshop.admin.models.Order

class OrdersAdapter(
    private val orders: List<Order>,
    private val onStatusChange: (String, String) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.order_id)
        private val customerNameTextView: TextView = itemView.findViewById(R.id.customer_name)
        private val itemsCountTextView: TextView = itemView.findViewById(R.id.items_count)
        private val totalPriceTextView: TextView = itemView.findViewById(R.id.total_price)
        private val statusTextView: TextView = itemView.findViewById(R.id.order_status)
        private val statusButtonsContainer: LinearLayout = itemView.findViewById(R.id.status_buttons)

        fun bind(order: Order) {
            orderIdTextView.text = "Order #${order.id.takeLast(6)}"
            customerNameTextView.text = "Customer: ${order.customerName}"
            itemsCountTextView.text = "Items: ${order.items.size}"
            totalPriceTextView.text = "Total: ₫${order.totalPrice.toInt()}"
            statusTextView.text = "Status: ${order.status.uppercase()}"

            setupStatusButtons(order.id, statusButtonsContainer)
        }

        private fun setupStatusButtons(orderId: String, container: LinearLayout) {
            container.removeAllViews()
            
            val statuses = listOf("pending", "confirmed", "preparing", "ready")
            val colors = listOf(
                android.graphics.Color.parseColor("#f39c12"),
                android.graphics.Color.parseColor("#3498db"),
                android.graphics.Color.parseColor("#e74c3c"),
                android.graphics.Color.parseColor("#27ae60")
            )

            statuses.forEachIndexed { index, status ->
                val button = Button(container.context).apply {
                    text = status.uppercase()
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(colors[index])
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginStart = 4
                        marginEnd = 4
                    }
                    setOnClickListener {
                        onStatusChange(orderId, status)
                    }
                }
                container.addView(button)
            }
        }
    }
}
