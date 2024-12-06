package com.example.halamanlogin

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.halamanlogin.R
import Product

class DetailProductActivity : AppCompatActivity() {

    private lateinit var productName: TextView
    private lateinit var productImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        productName = findViewById(R.id.productName)
        productImage = findViewById(R.id.productImage)

        // Mendapatkan data dari Intent
        val product = intent.getParcelableExtra<Product>("product")

        // Menampilkan data produk pada UI
        if (product != null) {
            productName.text = product.name
            Glide.with(this).load(product.imageUrl).into(productImage)
        }
    }
}
