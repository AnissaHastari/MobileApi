package com.example.halamanlogin.Model

data class Product(
    val item_id: String,
    val nama_produk: String,
    val image_path: String,
    val deskripsi: String,
    var harga: String,
    val status: Int
)