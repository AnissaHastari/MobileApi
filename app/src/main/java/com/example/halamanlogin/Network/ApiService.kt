package com.example.halamanlogin.Network

import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("signup") // Sesuaikan endpoint
    fun signup(@Body signupRequest: SignupRequest): Call<Void>


    @GET("products")  // Ganti dengan endpoint yang sesuai untuk mengambil semua produk
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")  // Ganti dengan endpoint yang sesuai untuk mengambil detail produk
    fun getProductDetails(@Path("id") productId: String): Call<Product>

    @GET("user/profile")
    fun getUserProfile(): Call<User>

    @PUT("user/profile")
    fun updateUserProfile(@Body user: com.example.halamanlogin.Model.User): Call<User>
}
