package com.example.halamanlogin.Network

import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.SignupResponse
import com.example.halamanlogin.Model.User
import com.example.halamanlogin.Model.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @POST("api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/pengguna")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @GET("api/items/") // Endpoint API untuk mendapatkan daftar produk
    fun getProducts(): Call<ApiResponse>

    @GET("api/items/{item_id}")
    fun getProductDetail(@Path("id") productId: String): Call<ApiResponse>

    @GET("user/profile")
    fun getUserProfile(): Call<User>

    @PUT("user/profile")
    fun updateUserProfile(@Body user: com.example.halamanlogin.Model.User): Call<User>
}
