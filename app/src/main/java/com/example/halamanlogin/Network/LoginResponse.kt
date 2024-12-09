// LoginResponse.kt
package com.example.halamanlogin.Network

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: UserData?,
    val pengguna_id: String,
    val wallet: String
)

data class UserData(
    val username: String?,
    val token: String?
)
