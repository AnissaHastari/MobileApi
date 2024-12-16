package com.example.halamanlogin.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.halamanlogin.Network.LoginRequest
import com.example.halamanlogin.Network.LoginResponse
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_page)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.login_page_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validasi Input
            if (username.isEmpty()) {
                usernameEditText.error = "Username wajib diisi"
                usernameEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password wajib diisi"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Inisiasi Login
            loginUser(username, password)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        Log.d(TAG, "Mencoba login dengan username: $username")

        RetrofitInstance.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "onResponse: ${response.code()} - ${response.message()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d(TAG, "LoginResponse: $loginResponse")

                    if (loginResponse != null) {
                        Log.d(TAG, "Status: ${loginResponse.status}")
                        Log.d(TAG, "Message: ${loginResponse.message}")
                        Log.d(TAG, "penggunaid: ${loginResponse.wallet}")

                        // Sesuaikan kondisi berdasarkan nilai status dari API
                        if (loginResponse.status == 1 || loginResponse.status == 0) { // Asumsikan status=0 adalah sukses
                            val penggunaId = loginResponse.pengguna_id
                            val wallet = loginResponse.wallet

                            val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit()
                                .putBoolean("IsLoggedIn", true)
                                .putString("penggunaId", penggunaId)
                                .putString("wallet", wallet)
                                .apply()

                            Log.d(TAG, "Pengguna ID disimpan: $penggunaId")  // Tambahkan log ini untuk memverifikasi
                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Tampilkan pesan gagal login
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse.message.ifEmpty { "Username atau Password salah" },
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Login gagal: ${loginResponse.message}")
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Respon dari server tidak valid", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "LoginResponse null")
                    }
                } else {
                    // Tangani error HTTP
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan pada server: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error server: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Tangani kegagalan jaringan
                Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Kegagalan jaringan: ${t.message}", t)
            }
        })
    }
}