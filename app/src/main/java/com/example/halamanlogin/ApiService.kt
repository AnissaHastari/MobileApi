package com.example.halamanlogin

import Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, val user: User)
data class User(val id: Int, val name: String, val email: String)

interface ApiService {
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<Void>
    @GET("products")
    suspend fun getProducts(): List<Product>
}

