package com.example.halamanlogin.Model

data class RentalHistoryItem(
    val rent_id: Int,
    val product_name: String,
    val durasi: Int,
    val tgl_pengembalian: String,
    val harga_total: Double,
    val status: Int,
    val image_path: String
)