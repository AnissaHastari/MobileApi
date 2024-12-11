package com.example.halamanlogin.Network

data class RentRequest(
    val owner_id: Int,
    val renter_id: Int,
    val item_id: Int,
    val image_path: String,
    val nama_produk: String,
    val durasi: Int,
    val tgl_pengembalian: String,
    val harga_total: Double,
    val status: Int
)
