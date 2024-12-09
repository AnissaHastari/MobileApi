package com.example.halamanlogin.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.R
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var productImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        // Initialize views
        tvTitle = findViewById(R.id.tvTitle)
        tvItemName = findViewById(R.id.tvItemName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        productImageView = findViewById(R.id.productImageView)

        // Get product details from Intent
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val productDescription = intent.getStringExtra("product_description")
        val productImage = intent.getStringExtra("product_image")

        // Set the title and product details
        tvTitle.text = "Detail Produk"
        tvItemName.text = productName
        tvPrice.text = productPrice
        tvDescription.text = productDescription

        // Load the image using Picasso
        Picasso.get().load("http://192.168.1.11:8000$productImage").into(productImageView)
    }
}
