package com.example.halamanlogin.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.R
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var productImageView: ImageView
    private lateinit var btnSewa: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        // Initialize views
        tvTitle = findViewById(R.id.tvTitle)
        tvItemName = findViewById(R.id.tvItemName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        productImageView = findViewById(R.id.productImageView)
        btnSewa = findViewById(R.id.btnSewa)

        // Get product details from Intent
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val productDescription = intent.getStringExtra("product_description")
        val productImage = intent.getStringExtra("product_image")
        val productId = intent.getIntExtra("product_id", -1) // Pastikan ID produk dikirim

        // Set the title and product details
        tvTitle.text = "Detail Produk"
        tvItemName.text = productName
        tvPrice.text = "Rp. $productPrice"
        tvDescription.text = productDescription

        // Load the image using Picasso
        Picasso.get().load("http://192.168.1.12:8000$productImage").into(productImageView)

        // Handle tombol Sewa
        btnSewa.setOnClickListener {
            if (productId == -1) {
                Toast.makeText(this, "Produk tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showRentDaysDialog(productId, productName, productPrice)
        }
    }

    private fun showRentDaysDialog(productId: Int, productName: String?, productPrice: String?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rent_days, null)
        val numberPicker: NumberPicker = dialogView.findViewById(R.id.numberPickerDays)
        numberPicker.minValue = 1
        numberPicker.maxValue = 30
        numberPicker.value = 1

        val dialog = AlertDialog.Builder(this)
            .setTitle("Pilih Jumlah Hari Sewa")
            .setView(dialogView)
            .setPositiveButton("Lanjut") { _, _ ->
                val selectedDays = numberPicker.value
                proceedToCheckout(productId, productName, productPrice, selectedDays)
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun proceedToCheckout(productId: Int, productName: String?, productPrice: String?, days: Int) {
        // Parse price to calculate total
        val cleanPrice = productPrice?.replace("[^\\d]".toRegex(), "") // Hapus semua kecuali digit
        val price = cleanPrice?.toDoubleOrNull()
        if (price == null) {
            Toast.makeText(this, "Harga produk tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val totalPrice = price * days

        // Get penggunaId from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val penggunaId = sharedPreferences.getString("penggunaId", null)

        if (penggunaId.isNullOrEmpty()) {
            Toast.makeText(this, "Pengguna tidak terdaftar. Silakan login kembali.", Toast.LENGTH_SHORT).show()
            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Prepare data untuk CheckoutActivity
        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putExtra("product_id", productId)
            putExtra("product_name", productName)
            putExtra("product_price", productPrice)
            putExtra("days", days)
            putExtra("total_price", totalPrice.toString())
            putExtra("pengguna_id", penggunaId)
        }
        startActivity(intent)
    }
}
