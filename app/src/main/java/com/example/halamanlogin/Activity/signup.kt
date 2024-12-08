// Signup.kt
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
import com.example.halamanlogin.Model.SignupRequest
import com.example.halamanlogin.Model.SignupResponse
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signup_page)

        // Initialize views
        usernameEditText = findViewById(R.id.emailEditText) // Change ID if necessary
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        signUpButton = findViewById(R.id.btnSignUp)

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signupRequest = SignupRequest(username, password)
            sendSignupRequest(signupRequest)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun sendSignupRequest(signupRequest: SignupRequest) {
        val apiService = RetrofitInstance.apiService
        apiService.signup(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null) {
                        if (signupResponse.status == "true") {
                            Toast.makeText(this@Signup, "${signupResponse.message} Register Berhasil. Silahkan Login", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@Signup, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Assuming the API sends back user data on failure
                            val errorData = signupResponse.data
                            if (errorData != null) {
                                Toast.makeText(
                                    this@Signup,
                                    "Username: ${errorData.username}\nPassword: ${errorData.password}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(this@Signup, "Registration failed: ${signupResponse.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@Signup, "Unexpected response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup, "Sign up failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@Signup, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
