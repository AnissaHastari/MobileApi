package com.example.halamanlogin.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.Adapter.ProductAdapter
import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.Model.ApiResponse
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_pages)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.productsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter { product -> onProductClicked(product) }
        recyclerView.adapter = productAdapter

        // Fetch products from the API
        fetchProducts()

        // Set up Profile Button
        findViewById<View>(R.id.profileButton).setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProducts() {
        val retrofit = RetrofitInstance.apiService
        retrofit.getProducts().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status == "true") {
                        val products = apiResponse.data
                        // Mengonversi harga dengan membersihkan simbol dan titik
                        products.forEach { product ->
                            try {
                                val cleanPrice = product.harga.replace("Rp", "").replace(".", "").trim()
                                val price = cleanPrice.toInt() // Mengonversi ke angka
                                // Menampilkan harga dengan format
                                val formattedPrice = "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)}"
                                // Lakukan hal lain dengan data produk
                            } catch (e: NumberFormatException) {
                                Log.e("Error", "Invalid price format: ${product.harga}")
                            }
                        }
                        productAdapter.submitList(products) // Mengirim data produk ke adapter
                    } else {
                        Toast.makeText(this@HomeActivity, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun onProductClicked(product: Product) {
        val intent = Intent(this@HomeActivity, ProductDetailActivity::class.java)
        intent.putExtra("product_id", product.id)
        intent.putExtra("product_name", product.nama_produk)
        intent.putExtra("product_price", product.harga)
        intent.putExtra("product_description", product.deskripsi)
        intent.putExtra("product_image", product.image_path)
        startActivity(intent)
    }
}
