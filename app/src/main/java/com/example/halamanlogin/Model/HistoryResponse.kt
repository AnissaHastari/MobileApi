package com.example.halamanlogin.Model

import com.example.halamanlogin.Model.RentalHistoryItem

data class HistoryResponse(
    val status: String,
    val data: List<RentalHistoryItem>
)
