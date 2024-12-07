package com.example.halamanlogin.Model

import java.io.Serializable

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: String,
    val location: String,
    val description: String
) : Serializable
