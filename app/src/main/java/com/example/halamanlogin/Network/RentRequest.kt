package com.example.halamanlogin.Network

data class RentRequest(
    val pengguna_id: String,
    val product_id: Int,
    val durasi: Int,
    val harga_total: Double
)
