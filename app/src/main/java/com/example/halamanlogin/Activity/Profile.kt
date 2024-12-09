package com.example.halamanlogin.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.ApiResponse
import com.example.halamanlogin.Model.User
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale


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

        // Inisialisasi komponen UI
        profileImageView = findViewById(R.id.itemImage)
        nameTextView = findViewById(R.id.nameText)
        emailTextView = findViewById(R.id.email)
        phoneTextView = findViewById(R.id.phoneNumber)
        addressTextView = findViewById(R.id.address)
        editProfileTextView = findViewById(R.id.editProfileText)
        startRentButton = findViewById(R.id.startRentButton)
        signOutButton = findViewById(R.id.signOutButton)

        // Membuat instance ProfileAdapter

        // Ambil pengguna ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val penggunaId = sharedPreferences.getString("penggunaId", null) ?: ""

        Log.d(TAG, "penggunaid $penggunaId")

        // Fetch user profile dari API


//        private fun loadProfile(penggunaId: String) {
//            // Call Retrofit API to get user profile
//            RetrofitInstance.apiService.getUserProfile(penggunaId = penggunaId)
//                .enqueue(object : Callback<User> {
//                    override fun onResponse(call: Call<User>, response: Response<User>) {
//                        if (response.isSuccessful) {
//                            val user = response.body()
//
//                            if (user != null) {
//                                // Format wallet balance
//                                val formattedWallet = NumberFormat.getNumberInstance(Locale("id", "ID"))
//                                    .format(user.wallet)
//
//                                // Format join date
//
//                                // Prepare profile image URL
//                                val profilePath = if (user.image_path.isNotEmpty()) {
//                                    "http://192.168.18.2:8000/storage/${user.image_path.replace("public/", "")}"
//                                } else {
//                                    "http://192.168.18.2:8000/storage/default_image.jpg"
//                                }
//
//                                // Load image from URL
//                                val uri = Uri.parse(profilePath)
//                                profileImageView.setImageURI(uri)
//
//                                // Update UI
//                                nameTextView.text = user.Nama_pengguna
//                                emailTextView.text = "Email: ${user.Email}"
//                                phoneTextView.text = "Nomor Telepon: ${user.No_Telepon}"
//                                addressTextView.text = "Alamat: ${user.Alamat}"
//
//                                // Display formatted wallet and join date
//                                Toast.makeText(this@ProfileActivity, "Dompet Saya: $formattedWallet", Toast.LENGTH_SHORT).show()
//
//                                // Additional UI updates
//                            }
//                        } else {
//                            Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<User>, t: Throwable) {
//                        Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }

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

    private fun signOut() {
        // Hapus data di SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Navigasi ke halaman login
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}