package com.example.halamanlogin.Model

data class RentResponse(
    val status: String,
    val message: String,
    val data: RentalHistoryItem? = null
)
