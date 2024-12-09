package com.example.halamanlogin.Model

data class ApiResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: List<Product> // Data produk dalam bentuk list
)
