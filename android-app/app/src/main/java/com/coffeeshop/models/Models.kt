package com.coffeeshop.models

import com.google.gson.annotations.SerializedName

// Auth Models
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val _id: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String? = null,
    val faceEnrolled: Boolean? = false
)

// Order Models
data class Order(
    val _id: String,
    val orderDate: String? = null,
    val deliveryAddress: DeliveryAddress? = null,
    val items: List<OrderItem>? = null,
    val subtotal: Double? = 0.0,
    val shippingFee: Double? = 0.0,
    val discountAmount: Double? = 0.0,
    val totalAmount: Double? = 0.0,
    val status: String? = "Pending",
    val note: String? = null
)

data class DeliveryAddress(
    val fullName: String? = null,
    val phone: String? = null,
    val street: String? = null,
    val ward: String? = null,
    val district: String? = null,
    val city: String? = null
)

data class OrderItem(
    val productName: String? = null,
    val quantity: Int? = 1,
    val finalUnitPrice: Double? = 0.0,
    val sizeChosen: String? = null,
    val iceLevel: String? = null,
    val sugarLevel: String? = null,
    val chosenToppings: List<Topping>? = null,
    val itemNote: String? = null
)

data class Topping(
    val name: String? = null,
    val price: Double? = 0.0
)

data class StatusUpdateRequest(
    val status: String
)

// Combo Models
data class Combo(
    val _id: String,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
    val basePrice: Double? = 0.0,
    val discount: Double? = 0.0,
    val discountedPrice: Double? = 0.0,
    val image_url: String? = null,
    val items: List<ComboItem>? = null
)

data class ComboItem(
    val productId: String? = null,
    val productName: String? = null,
    val quantity: Int? = 1
)

data class ComboRequest(
    val name: String,
    val description: String,
    val category: String,
    val basePrice: Double,
    val discount: Double,
    val image_url: String?,
    val items: List<ComboItem>
)

// Face Enrollment Models
data class FaceEnrollRequest(
    val userId: String? = null,
    val faceDescriptor: List<Double>,
    val enrollmentPhoto: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String? = null,
    val password: String? = null
)

data class FaceEnrollResponse(
    val message: String? = null,
    val faceEnrolled: Boolean? = false,
    val user: FaceEnrollUser? = null
)

data class FaceEnrollUser(
    val _id: String? = null,
    val username: String? = null,
    val name: String? = null,
    val faceEnrolled: Boolean? = false
)

data class FaceStatusResponse(
    val faceEnrolled: Boolean? = false,
    val message: String? = null
)

