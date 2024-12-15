package com.example.halamanlogin.Network

import com.example.halamanlogin.Model.ApiResponse
import com.example.halamanlogin.Model.EditResponse
import com.example.halamanlogin.Model.HistoryResponse
import com.example.halamanlogin.Model.RentResponse
import com.example.halamanlogin.Model.RentStatusResponse
import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.SignupResponse
import com.example.halamanlogin.Model.StatusResponse
import com.example.halamanlogin.Model.UserResponse
import com.example.halamanlogin.Model.itemStatusResponse
import com.example.halamanlogin.Model.walletResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @POST("/api/rent_item")
    fun rentItem(@Body rentRequest: RentRequest): Call<RentResponse>

    @FormUrlEncoded
    @PUT("api/pengguna/status/{penggunaId}")
    fun updateUserStatus(
        @Path("penggunaId") penggunaId: String,
        @Field("status") status: String): Call<StatusResponse>

    @FormUrlEncoded
    @PUT("api/items/status/{item_id}")
    fun updateitemstatus(
        @Path("item_id") item_id: String,
        @Field("status") status: String): Call<itemStatusResponse>

    @GET("/api/rent_item/{pembeli_id}")
    fun getRentalHistory(@Path("pembeli_id") penggunaId: String): Call<HistoryResponse>

    // Endpoint untuk memperbarui status penyewaan
    @FormUrlEncoded
    @PUT("/api/rent_item/status/{rent_id}")
    fun updateRentalStatus(
        @Path("rent_id") rentId: String,
        @Field ("status")status: String): Call<RentStatusResponse>

    @FormUrlEncoded
    @PUT("/api/pengguna/wallet/{penggunaId}")
    fun updatewallet(
        @Path("penggunaId") penggunaId: String,
        @Field ("wallet")wallet: Double): Call<walletResponse>

//    @Multipart
//    @PUT("api/pengguna/{penggunaId}")
//    fun updateimage(
//        @Path("penggunaId") penggunaId: String,
//        @Part imageprofile: MultipartBody.Part?
//    ): Call<EditResponse>

}
