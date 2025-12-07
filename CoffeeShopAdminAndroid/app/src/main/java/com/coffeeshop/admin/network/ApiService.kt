package com.coffeeshop.admin.network

import com.coffeeshop.admin.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    
    // Auth endpoints
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<UserResponse>

    // Combos endpoints
    @GET("combos")
    fun getCombos(
        @Header("Authorization") token: String
    ): Call<List<Combo>>

    @GET("combos/{id}")
    fun getCombo(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Combo>

    @POST("combos")
    fun createCombo(
        @Body combo: Combo,
        @Header("Authorization") token: String
    ): Call<Combo>

    @PUT("combos/{id}")
    fun updateCombo(
        @Path("id") id: String,
        @Body combo: Combo,
        @Header("Authorization") token: String
    ): Call<Combo>

    @DELETE("combos/{id}")
    fun deleteCombo(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Void>

    // Orders endpoints
    @GET("orders")
    fun getOrders(
        @Header("Authorization") token: String
    ): Call<List<Order>>

    @GET("orders/{id}")
    fun getOrder(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Order>

    @POST("orders")
    fun createOrder(
        @Body order: Order,
        @Header("Authorization") token: String
    ): Call<Order>

    @PATCH("orders/{id}")
    fun updateOrderStatus(
        @Path("id") id: String,
        @Body statusUpdate: StatusUpdate,
        @Header("Authorization") token: String
    ): Call<Order>

    // Attendance endpoints
    @POST("attendance/check-in")
    fun checkIn(
        @Body request: CheckInRequest,
        @Header("Authorization") token: String
    ): Call<AttendanceResponse>

    @GET("attendance/records")
    fun getAttendanceRecords(
        @Header("Authorization") token: String
    ): Call<List<Attendance>>
}

// Request/Response models
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val fullName: String,
    val role: String
)

data class UserResponse(
    val message: String,
    val user: User
)

data class StatusUpdate(
    val status: String
)

data class CheckInRequest(
    val employeeId: String,
    val photo: String? = null
)

data class AttendanceResponse(
    val status: String,
    val timestamp: String,
    val message: String
)
