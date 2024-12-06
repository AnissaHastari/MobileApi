package com.example.halamanlogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.R
import com.yourpackage.adapter.ProductAdapter
import com.example.halamanlogin.P
import com.example.halamanlogin.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Ganti dengan base URL API kamu
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.productsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        productAdapter = ProductAdapter(this, emptyList()) { product ->
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }

        recyclerView.adapter = productAdapter

        // Load Products
        loadProducts()
    }

    private fun loadProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val products = apiService.getProducts()  // Mengambil data produk dari API
            withContext(Dispatchers.Main) {
                productAdapter.updateList(products)  // Menampilkan produk di RecyclerView
            }
        }
    }
}
