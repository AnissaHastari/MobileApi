package com.example.halamanlogin.Network

data class LoginResponse(
    val status: String,
    val message: String? // Bisa berupa pesan kesalahan atau pesan sukses
)
