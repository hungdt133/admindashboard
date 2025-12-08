package com.coffeeshop.api

import com.coffeeshop.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    // Orders
    @GET("orders")
    suspend fun getOrders(): Response<List<Order>>
    
    @PATCH("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body request: StatusUpdateRequest
    ): Response<Order>
    
    // Combos
    @GET("combos")
    suspend fun getCombos(): Response<List<Combo>>
    
    @POST("combos")
    suspend fun createCombo(@Body combo: ComboRequest): Response<Combo>
    
    @PUT("combos/{id}")
    suspend fun updateCombo(
        @Path("id") id: String,
        @Body combo: ComboRequest
    ): Response<Combo>
    
    @DELETE("combos/{id}")
    suspend fun deleteCombo(@Path("id") id: String): Response<Unit>
    
    // Face Enrollment (Admin only)
    @POST("face/enroll-face")
    suspend fun enrollFace(@Body request: FaceEnrollRequest): Response<FaceEnrollResponse>
    
    @GET("face/face-status/{userId}")
    suspend fun getFaceStatus(@Path("userId") userId: String): Response<FaceStatusResponse>
}

