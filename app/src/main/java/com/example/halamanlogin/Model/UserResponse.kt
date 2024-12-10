package com.example.halamanlogin.Model

data class UserResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: List<User> // Data profile pengguna
)