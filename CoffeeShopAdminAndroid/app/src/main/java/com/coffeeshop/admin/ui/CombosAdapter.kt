package com.coffeeshop.admin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coffeeshop.admin.R
import com.coffeeshop.admin.models.Combo

class CombosAdapter(private val combos: List<Combo>) : RecyclerView.Adapter<CombosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val combo = combos[position]
        holder.bind(combo)
    }

    override fun getItemCount(): Int = combos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.combo_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.combo_description)
        private val priceTextView: TextView = itemView.findViewById(R.id.combo_price)
        private val discountTextView: TextView = itemView.findViewById(R.id.combo_discount)
        private val includesTextView: TextView = itemView.findViewById(R.id.combo_includes)

        fun bind(combo: Combo) {
            nameTextView.text = combo.name
            descriptionTextView.text = combo.description
            
            val priceText = "₫${combo.discountedPrice.toInt()} / ₫${combo.originalPrice.toInt()}"
            priceTextView.text = priceText
            
            discountTextView.text = "${combo.discount}% OFF"
            includesTextView.text = "Includes: ${combo.includes.joinToString(", ")}"
        }
    }
}
