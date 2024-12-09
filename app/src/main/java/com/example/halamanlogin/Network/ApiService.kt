package com.example.halamanlogin.Network

import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.SignupResponse
import com.example.halamanlogin.Model.User
import com.example.halamanlogin.Model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @POST("api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/pengguna")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>

    @GET("api/items/") // Endpoint API untuk mendapatkan daftar produk
    fun getProducts(): Call<ApiResponse>

    @GET("products/{id}")
    fun getProductDetails(@Path("id") productId: String): Call<Product>

    @GET("api/pengguna")
    fun getUserProfile(@Header("penggunaId") penggunaId: String): Call<User>

    @Multipart
    @PUT("api/pengguna")
    fun updateUserProfileWithImage(
        @Part("Nama_pengguna") namaPengguna: RequestBody,
        @Part("Email") email: RequestBody,
        @Part("No_Telepon") noTelepon: RequestBody,
        @Part("Alamat") alamat: RequestBody,
        @Part("image_path") profileImage: MultipartBody.Part
    ): Call<User>


    @PUT("user/profile")
    fun updateUserProfile(@Body user: com.example.halamanlogin.Model.User): Call<User>
}
