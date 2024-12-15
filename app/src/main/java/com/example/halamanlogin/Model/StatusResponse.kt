package com.example.halamanlogin.Model

data class StatusResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: List<stat> // Data profile pengguna
)

data class itemStatusResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: List<statitem> // Data profile pengguna
)

data class RentStatusResponse(
    val status: String,   // Status misalnya "true" atau "false"
    val message: String,  // Pesan dari API
    val data: List<statrent> // Data profile pengguna
)