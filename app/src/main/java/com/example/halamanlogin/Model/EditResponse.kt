package com.example.halamanlogin.Model

data class EditResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: EditRes? // Data profile pengguna
)