package com.example.halamanlogin.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.User
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

        // OnClickListener untuk memilih gambar
        profileImageView.setOnClickListener {
            selectImageFromGallery()
        }

        // Tombol simpan untuk memperbarui profil
        saveButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadProfileWithImage()
            } else {
                updateProfileWithoutImage()
            }
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

    private fun uploadProfileWithImage() {
        val filePath = FileUtils.getPath(this, selectedImageUri!!)
        val file = File(filePath)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val imagePart = MultipartBody.Part.createFormData("Profile_path", file.name, requestFile)

        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), nameEditText.text.toString())
        val email = RequestBody.create("text/plain".toMediaTypeOrNull(), emailEditText.text.toString())
        val phone = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneEditText.text.toString())
        val address = RequestBody.create("text/plain".toMediaTypeOrNull(), addressEditText.text.toString())

        RetrofitInstance.apiService.updateUserProfileWithImage(
            name, email, phone, address, imagePart
        ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Profile updated with image", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProfileWithoutImage() {
        val updatedUser = User(
            Nama_pengguna = nameEditText.text.toString(),
            Email = emailEditText.text.toString(),
            No_Telepon = phoneEditText.text.toString(),
            Alamat = addressEditText.text.toString(),
//            wallet = walletEditText.text.toString(),
            image_path = "" // Kosongkan jika tidak ada perubahan
        )

        RetrofitInstance.apiService.updateUserProfile(updatedUser).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 101
    }
}
