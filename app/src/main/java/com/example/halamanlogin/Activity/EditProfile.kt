package com.example.halamanlogin.Activity

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.EditResponse
import com.example.halamanlogin.Utils.FileUtils
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var walletTextView: TextView
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        profileImageView = findViewById(R.id.profileImage)
        nameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        addressEditText = findViewById(R.id.addressEditText)
        saveButton = findViewById(R.id.saveButton)

        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val penggunaId = sharedPreferences.getString("penggunaId", null) ?: ""
        Log.d(TAG, "User ID: $penggunaId")

        // OnClickListener untuk memilih gambar
        profileImageView.setOnClickListener {
            selectImageFromGallery()
        }

        // Tombol simpan untuk memperbarui profil
        saveButton.setOnClickListener {
            updateProfile(penggunaId)
            }

    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            profileImageView.setImageURI(selectedImageUri) // Tampilkan gambar yang dipilih
        }
    }

    private fun updateProfile(penggunaId: String) {
        // Get file path from selected image URI
        val filePath = selectedImageUri?.let { FileUtils.getPath(this, it) }
        if (filePath.isNullOrBlank()) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        // Prepare the file for the request
        val profileImagePart = filePath?.let {
            val file = File(it)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("Profile_path", file.name, requestFile)
        }



        // Prepare text fields for the request, ensure they're non-null
        val Nama_pengguna = nameEditText.text.toString()
        val Email = emailEditText.text.toString()
        val No_telepon = phoneEditText.text.toString()
        val Alamat = addressEditText.text.toString()

        Log.d("UpdateProfile", "Nama Pengguna: ${Nama_pengguna}")
        Log.d("UpdateProfile", "Email: ${Email}")
        Log.d("UpdateProfile", "No Telepon: ${phoneEditText.text.toString()}")
        Log.d("UpdateProfile", "Alamat: ${addressEditText.text.toString()}")
        Log.d("UpdateProfile", "image: ${profileImagePart}")
        // Call Retrofit API to update the user profile
//        RetrofitInstance.apiService.updateimage(
//            penggunaId, profileImagePart
//        )
        RetrofitInstance.apiService.updateUserProfile(
            penggunaId = penggunaId,
            namaPengguna = Nama_pengguna,
            email = Email,
            noTelepon = No_telepon,
            alamat = Alamat
        ).enqueue(object : Callback<EditResponse> {
            override fun onResponse(call: Call<EditResponse>, response: Response<EditResponse>) {
                if (response.isSuccessful) {
                    val editResponse = response.body()
                    Log.d("EditProfileActivity", "API Response: ${response.body()}")
                    if (editResponse != null && editResponse.status == "true") {
                        Log.d(TAG, "Response: $editResponse")


                        try {
                            // Assuming editData contains updated user information
                                // Handle the updated profile information (e.g., update UI, etc.)
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                            startActivity(intent)  // Close the activity after successful update

                        } catch (e: Exception) {
                            Log.d("EditProfileActivity", "Failed to update profile1: ${editResponse?.message ?: "Unknown error"}")
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Error processing the response: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.d("EditProfileActivity", "Failed to update profile2: ${editResponse?.message ?: "Unknown error"}")
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Failed to update profile: ${editResponse?.message ?: "Unknown error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d("EditProfileActivity", "Failed to update profile3: ${response.errorBody()?.string() ?: "Unknown error"}")
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Failed to update profile: ${response.errorBody()?.string() ?: "Unknown error"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EditResponse>, t: Throwable) {
                Log.e("EditProfileActivity", "Error: ${t.message}")
                Toast.makeText(
                    this@EditProfileActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 101
    }
}
