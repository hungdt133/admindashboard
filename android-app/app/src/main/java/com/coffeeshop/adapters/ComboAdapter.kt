package com.coffeeshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coffeeshop.R
import com.coffeeshop.models.Combo
import java.text.NumberFormat
import java.util.*

class ComboAdapter(
    private val combos: List<Combo>,
    private val onEditClick: (Combo) -> Unit,
    private val onDeleteClick: (Combo) -> Unit
) : RecyclerView.Adapter<ComboAdapter.ComboViewHolder>() {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_combo, parent, false)
        return ComboViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ComboViewHolder, position: Int) {
        val combo = combos[position]
        holder.bind(combo)
    }
    
    override fun getItemCount() = combos.size
    
    inner class ComboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivComboImage)
        private val tvName: TextView = itemView.findViewById(R.id.tvComboName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvComboDescription)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvComboPrice)
        private val tvDiscount: TextView = itemView.findViewById(R.id.tvDiscount)
        private val btnEdit: View = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: View = itemView.findViewById(R.id.btnDelete)
        
        fun bind(combo: Combo) {
            tvName.text = combo.name ?: "N/A"
            tvDescription.text = combo.description ?: ""
            
            val discount = combo.discount ?: 0.0
            if (discount > 0) {
                tvPrice.text = currencyFormat.format(combo.discountedPrice ?: 0.0)
                tvDiscount.text = "-${discount.toInt()}%"
                tvDiscount.visibility = View.VISIBLE
            } else {
                tvPrice.text = currencyFormat.format(combo.basePrice ?: 0.0)
                tvDiscount.visibility = View.GONE
            }
            
            val imageUrl = combo.image_url
            android.util.Log.d("ComboAdapter", "Loading image for ${combo.name}: $imageUrl")
            
            if (!imageUrl.isNullOrBlank()) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivImage)
            } else {
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            
            btnEdit.setOnClickListener { onEditClick(combo) }
            btnDelete.setOnClickListener { onDeleteClick(combo) }
        }
    }
}

