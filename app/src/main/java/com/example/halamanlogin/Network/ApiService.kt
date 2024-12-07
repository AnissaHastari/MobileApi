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
    @POST("pengguna")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("pengguna")
    fun signup(@Body signupRequest: SignupRequest): Call<Void>


    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{id}")
    fun getProductDetails(@Path("id") productId: String): Call<Product>

    @GET("user/profile")
    fun getUserProfile(): Call<User>

    @PUT("user/profile")
    fun updateUserProfile(@Body user: com.example.halamanlogin.Model.User): Call<User>
}
