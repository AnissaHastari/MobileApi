package com.example.halamanlogin.Network

import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.SignupResponse
import com.example.halamanlogin.Model.ApiResponse
import com.example.halamanlogin.Model.EditResponse
import com.example.halamanlogin.Model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
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

    @GET("api/items/{item_id}")
    fun getProductDetail(@Path("id") productId: String): Call<ApiResponse>

    @GET("api/pengguna/{penggunaId}")
    fun getUserProfile(@Path("penggunaId") penggunaId: String): Call<UserResponse>

    @FormUrlEncoded
    @PUT("api/pengguna/{penggunaId}")
    fun updateUserProfile(
        @Path("penggunaId") penggunaId: String,
        @Field("Nama_pengguna") namaPengguna: String,
        @Field("Email") email: String,
        @Field("No_Telepon") noTelepon: String,
        @Field("Alamat") alamat: String): Call<EditResponse>

//    @Multipart
//    @PUT("api/pengguna/{penggunaId}")
//    fun updateimage(
//        @Path("penggunaId") penggunaId: String,
//        @Part imageprofile: MultipartBody.Part?
//    ): Call<EditResponse>

}
