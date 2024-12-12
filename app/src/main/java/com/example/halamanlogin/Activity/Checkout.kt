package com.example.halamanlogin.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.Model.HistoryResponse
import com.example.halamanlogin.Model.RentResponse
import com.example.halamanlogin.Network.RentRequest
import com.example.halamanlogin.Network.RetrofitInstance
import com.example.halamanlogin.R
import com.example.halamanlogin.adapters.RentalHistoryAdapter
import com.example.halamanlogin.Model.RentalHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var tvProductName: TextView
    private lateinit var tvDays: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnConfirmRent: Button
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var rentalHistoryAdapter: RentalHistoryAdapter
    private val rentalHistoryList = mutableListOf<RentalHistoryItem>()

    companion object {
        private const val TAG = "CheckoutActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_page)

        // Initialize views
        tvProductName = findViewById(R.id.tvProductName)
        tvDays = findViewById(R.id.tvDays)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnConfirmRent = findViewById(R.id.btnConfirmRent)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)

        // Setup RecyclerView
        rentalHistoryAdapter = RentalHistoryAdapter(this, rentalHistoryList, RetrofitInstance.apiService)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = rentalHistoryAdapter

        // Get data from Intent
        val productId = intent.getIntExtra("product_id", -1)
        val productName = intent.getStringExtra("product_name")?: ""
        val productPrice = intent.getStringExtra("product_price")
        val days = intent.getIntExtra("days", 1)
        val totalPrice = intent.getStringExtra("total_price")?.toDoubleOrNull() ?: 0.0
        val penggunaId = intent.getStringExtra("pengguna_id")
        val image_path = intent.getStringExtra("product_image")?: "errr"
        val owner_id = intent.getStringExtra("ownerid")?: "000"
        val returnDate = calculateReturnDate(days)

        if (productId == -1 || penggunaId.isNullOrEmpty()) {
            Toast.makeText(this, "Data penyewaan tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set data to views
        tvProductName.text = "Produk: $productName"
        tvDays.text = "Jumlah Hari: $days"
        tvTotalPrice.text = "Total Harga: Rp. ${String.format("%,.0f", totalPrice)}"

        // Handle konfirmasi penyewaan
        btnConfirmRent.setOnClickListener {
            confirmRent(penggunaId, productId, days, totalPrice, image_path, owner_id, productName, returnDate)
        }

        // Fetch rental history
        fetchRentalHistory(penggunaId)
    }


    fun calculateReturnDate(days: Int): String {
        val calendar = Calendar.getInstance()

        // Tambahkan jumlah hari ke tanggal saat ini
        calendar.add(Calendar.DAY_OF_MONTH, days)

        // Format tanggal menjadi "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun confirmRent(penggunaId: String, productId: Int, days: Int, totalPrice: Double, image_path: String, owner_id:String, productName:String, returnDate:String) {
        val rentRequest = RentRequest(
            renter_id = penggunaId.toInt(),
            item_id = productId,
            durasi = days,
            harga_total = totalPrice,
            image_path = image_path,
            owner_id = owner_id.toInt(),
            nama_produk = productName,
            tgl_pengembalian = returnDate,
            status = 0
        )
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val walletStr = sharedPreferences.getString("wallet", "0")
        val wallet = walletStr?.toDoubleOrNull() ?: 0.0

        if (wallet < totalPrice) {
            Toast.makeText(this, "Saldo wallet tidak mencukupi. Silakan isi saldo terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }
        RetrofitInstance.apiService.rentItem(rentRequest).enqueue(object : Callback<RentResponse> {
            override fun onResponse(call: Call<RentResponse>, response: Response<RentResponse>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val rentResponse = response.body()!!
                    if (rentResponse.status == "true") {
                        Toast.makeText(this@CheckoutActivity, "Penyewaan berhasil", Toast.LENGTH_SHORT).show()
                        // Panggil fetchRentalHistory untuk refresh
                    } else {
                        // Tampilkan pesan error dari server
                        Toast.makeText(this@CheckoutActivity, rentResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Log.d(TAG, "penggunaId: $penggunaId")
                Log.d(TAG, "productId: $productId")
                Log.d(TAG, "days: $days")
                Log.d(TAG, "totalPrice: $totalPrice")
                Log.d(TAG, "image_path: $image_path")
                Log.d(TAG, "owner_id: $owner_id")
                Log.d(TAG, "productName: $productName")
                Log.d(TAG, "returnDate: $returnDate")
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun fetchRentalHistory(penggunaId: String) {
        RetrofitInstance.apiService.getRentalHistory(penggunaId).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val historyResponse = response.body()!!
                    if (historyResponse.status == "true") {
                        rentalHistoryList.clear()
                        rentalHistoryList.addAll(historyResponse.data)
                        rentalHistoryAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@CheckoutActivity, "Tidak ada riwayat penyewaan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "Gagal mengambil riwayat: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Kegagalan jaringan: ${t.message}", t)
            }
        })
    }
}
