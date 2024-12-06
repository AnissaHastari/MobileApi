package com.example.halamanlogin

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, val user: User)
data class User(val id: Int, val name: String, val email: String)

interface ApiService {
    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}
