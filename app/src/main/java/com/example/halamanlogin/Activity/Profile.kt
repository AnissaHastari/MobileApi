package com.example.halamanlogin.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.User
import com.example.halamanlogin.Model.UserResponse
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale
import android.net.Uri
import com.example.halamanlogin.Activity.LoginActivity.Companion
import com.example.halamanlogin.Model.ApiResponse
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var walletTextView: TextView
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
        walletTextView = findViewById(R.id.wallet)
        phoneTextView = findViewById(R.id.phoneNumber)
        addressTextView = findViewById(R.id.address)
        editProfileTextView = findViewById(R.id.editProfileText)
        startRentButton = findViewById(R.id.startRentButton)
        signOutButton = findViewById(R.id.signOutButton)

        // Ambil pengguna ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val penggunaId = sharedPreferences.getString("penggunaId", null) ?: ""

        Log.d(TAG, "penggunaId: $penggunaId")

        // Fetch user profile from API
        loadProfile(penggunaId)

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

    private fun loadProfile(penggunaId: String) {
        // Call Retrofit API to get user profile
        RetrofitInstance.apiService.getUserProfile(penggunaId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        if (userResponse != null && userResponse.status == "true") {
                            val users = userResponse.data
                            Log.d(TAG, "Response: $userResponse")
                            Log.d(TAG, "idResponse: $penggunaId")

                            users.forEach { User ->
                                try {
                                    if (User != null) {
                                        // Prepare profile image URL
                                        val profilePath = if (User.Profile_path.isNotEmpty()) {
                                            "http://192.168.18.2:8000/storage/${User.Profile_path.replace("public/", "")}"
                                        } else {
                                            null
                                        }

                                        val walletFormatted = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                                            .apply {
                                                maximumFractionDigits = 0 // Tidak ada digit desimal
                                            }
                                            .format(User.Wallet)

                                        // Load the image using Picasso
                                        if (profilePath != null) {
                                            Picasso.get()
                                                .load(profilePath)
                                                .placeholder(R.drawable.person) // Placeholder image while loading
                                                .error(R.drawable.person)      // Fallback image if the URL fails
                                                .into(profileImageView)
                                        } else {
                                            profileImageView.setImageResource(R.drawable.person) // Default image for empty path
                                        }



                                        // Update UI
                                        nameTextView.text = User.Nama_pengguna
                                        emailTextView.text = "Email: ${User.Email}"
                                        walletTextView.text = "Wallet: ${walletFormatted}"
                                        phoneTextView.text = "Nomor Telepon: 0${User.No_Telepon}"
                                        addressTextView.text = "Alamat: ${User.Alamat}"
                                    }
                                } catch (e: NumberFormatException) {
                                    Toast.makeText(
                                        this@ProfileActivity,
                                        "Failed to load profile: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Failed to fetch user profile: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
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