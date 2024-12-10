package com.example.halamanlogin.Model

import okhttp3.MultipartBody


data class Edit(
    val Nama_pengguna: String,
    val Email: String,
    val No_Telepon: String,
    val Alamat: String,
    val Profile_path: MultipartBody.Part
)

data class EditRes(
    val Nama_pengguna: List<String>,
    val Email: List<String>,
    val No_Telepon: List<String>,
    val Alamat: List<String>,
    val Profile_path: List<String>
)