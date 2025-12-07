package com.coffeeshop.admin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val fullName: String,
    val role: String,
    val email: String? = null,
    val phone: String? = null
) : Parcelable

@Parcelize
data class Combo(
    @SerializedName("_id")
    val id: String = "",
    val name: String,
    val description: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val discount: Int,
    val includes: List<String>,
    val createdAt: String? = null,
    val updatedAt: String? = null
) : Parcelable

@Parcelize
data class Order(
    @SerializedName("_id")
    val id: String,
    val customerName: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val status: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
) : Parcelable

@Parcelize
data class OrderItem(
    val comboId: String,
    val name: String,
    val quantity: Int,
    val price: Double
) : Parcelable

@Parcelize
data class Attendance(
    @SerializedName("_id")
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val status: String,
    val checkInTime: String,
    val checkOutTime: String? = null,
    val photo: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) : Parcelable
