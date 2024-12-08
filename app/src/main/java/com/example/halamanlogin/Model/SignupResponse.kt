package com.example.halamanlogin.Model

data class SignupResponse(
    val status: String,
    val message: String,
    val data: UserData? // Assuming 'data' contains user information on failure
)

data class UserData(
    val username: String,
    val password: String
)
