package com.example.halamanlogin.Model

data class RentResponse(
    val status: String,
    val message: String,
    val data: List<RentData> // Optional, tergantung API respons
)

data class RentData(
    val rent_id: Int,         // ID transaksi yang baru dibuat
    val owner_id: Int,
    val renter_id: Int,
    val item_id: Int,
    val nama_produk: String,
    val durasi: Int,
    val tgl_pengembalian: String,
    val harga_total: Double,
    val status: Int,
    val created_at: String,   // Waktu pembuatan transaksi
    val updated_at: String    // Waktu terakhir pembaruan
)

