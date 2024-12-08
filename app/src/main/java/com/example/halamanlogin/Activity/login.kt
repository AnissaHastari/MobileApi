// LoginActivity.kt
package com.example.halamanlogin.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.halamanlogin.Activity.HomeActivity
import com.example.halamanlogin.Network.LoginRequest
import com.example.halamanlogin.Network.LoginResponse
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText // Changed from emailEditText to usernameEditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)  // Ensure this layout exists

        // Initialize views with updated IDs
        usernameEditText = findViewById(R.id.usernameEditText) // Changed ID from emailEditText
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.login_page_button) // Ensure this ID matches your XML

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Username dan Password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)

        RetrofitInstance.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        if (loginResponse.status == "true" || loginResponse.status.equals("success", ignoreCase = true)) {
                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            // Navigate to HomeActivity
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Handle login failure with message from API
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse.message.ifEmpty { "Username atau Password salah" },
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Respon dari server tidak valid", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle HTTP error responses
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan pada server: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network failures or unexpected errors
                Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
