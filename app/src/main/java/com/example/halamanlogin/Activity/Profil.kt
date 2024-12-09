package com.example.halamanlogin.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.User
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var editProfileTextView: TextView
    private lateinit var startRentButton: Button
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Menyambungkan komponen UI
        profileImageView = findViewById(R.id.itemImage)
        nameTextView = findViewById(R.id.nameText)
        emailTextView = findViewById(R.id.email)
        phoneTextView = findViewById(R.id.phoneNumber)
        addressTextView = findViewById(R.id.address)
        editProfileTextView = findViewById(R.id.editProfileText)
        startRentButton = findViewById(R.id.startRentButton)
        signOutButton = findViewById(R.id.signOutButton)

        // Ambil data profil pengguna dari API
        fetchUserProfile()

        // Tombol Edit Profile
        editProfileTextView.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Tombol Mulai Sewa
        startRentButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        // Tombol Sign Out
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun fetchUserProfile() {
        val retrofit = RetrofitInstance.apiService
        retrofit.getUserProfile().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Update UI dengan data pengguna
                        nameTextView.text = "Nama Pengguna: ${user.name}"
                        emailTextView.text = "Email: ${user.email}"
                        phoneTextView.text = "Nomor Telepon: ${user.phone}"
                        addressTextView.text = "Alamat: ${user.address}"
                        // Set gambar profil atau data lainnya
                        // profileImageView.setImageResource(user.profileImage)
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signOut() {
        // Logic untuk logout dan kembali ke halaman login
        Toast.makeText(this@ProfileActivity, "Signed out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
