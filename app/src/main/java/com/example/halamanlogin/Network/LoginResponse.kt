// LoginResponse.kt
package com.example.halamanlogin.Network

data class LoginResponse(
    val status: String,      // e.g., "true" or "false"
    val message: String,
    val data: UserData?      // Optional, contains user information on failure or success
)

data class UserData(
    val username: String,
    val password: String // Typically, you wouldn't send back the password. Ensure this aligns with your API.
)
