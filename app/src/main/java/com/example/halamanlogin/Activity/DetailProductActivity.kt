package com.example.halamanlogin.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvItemName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var productImage: ImageView
    private lateinit var btnOrder: Button
    private lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)

        // Menyambungkan elemen UI
        tvTitle = findViewById(R.id.tvTitle)
        tvItemName = findViewById(R.id.tvItemName)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        productImage = findViewById(R.id.productImage)
        btnOrder = findViewById(R.id.btnOrder)   // Tombol Sewa Barang

        // Mengambil ID produk dari intent
        val productId = intent.getStringExtra("product_id")

        // Ambil data produk berdasarkan ID
        fetchProductDetails(productId)

        // Tombol Sewa Barang
        btnOrder.setOnClickListener {
            // Arahkan ke halaman Checkout saat tombol Sewa Barang ditekan
            val intent = Intent(this@ProductDetailActivity, CheckoutActivity::class.java)
            intent.putExtra("product_id", productId) // Kirimkan data produk jika diperlukan
            startActivity(intent)
        }

        // Tombol Checkout
        btnCheckout.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProductDetails(productId: String?) {
        if (productId != null) {
            val retrofit = RetrofitInstance.apiService
            retrofit.getProductDetails(productId).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        if (product != null) {
                            // Menampilkan data produk di UI
                            tvItemName.text = product.nama_produk
                            tvPrice.text = "Rp ${product.harga}"
                            tvDescription.text = product.deskripsi

                            // Menampilkan gambar produk menggunakan Picasso
                            Picasso.get().load("http://192.168.12.128:8000${product.image_path}").into(productImage)
                        }
                    } else {
                        Toast.makeText(this@ProductDetailActivity, "Failed to fetch product details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(this@ProductDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
