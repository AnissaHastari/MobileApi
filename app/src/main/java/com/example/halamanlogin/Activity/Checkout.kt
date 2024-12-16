package com.example.halamanlogin.Activity

import android.content.Context
import android.content.SharedPreferences
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
import com.example.halamanlogin.Model.itemStatusResponse
import com.example.halamanlogin.Model.walletResponse
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
//        val productPrice = intent.getStringExtra("product_price")
        val days = intent.getIntExtra("days", 1)
        val totalPrice = intent.getStringExtra("total_price")?.toDoubleOrNull() ?: 0.0
        val penggunaId = intent.getStringExtra("pengguna_id")
        val imagepath = intent.getStringExtra("product_image")?: "errr"
        val ownerid = intent.getStringExtra("ownerid")?: "000"
        val returnDate = calculateReturnDate(days)
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val walletStr = sharedPreferences.getString("wallet", "0")
        val wallet = walletStr?.toDoubleOrNull() ?: 0.0

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
            confirmRent(penggunaId, productId, days, totalPrice, imagepath, ownerid, productName, returnDate, wallet)
            updatestatusitem0(productId.toString())
            updateWallet(penggunaId, sharedPreferences, totalPrice)

        }

        // Fetch rental history
        fetchRentalHistory(penggunaId)
    }

    private fun updateWallet(penggunaId:String ,sharedPreferences: SharedPreferences, totalPrice: Double) {
        // Get current wallet balance
        val walletStr = sharedPreferences.getString("wallet", "0")
        val currentWallet = walletStr?.toDoubleOrNull() ?: 0.0

        // Subtract totalPrice from wallet balance
        val updatedWallet = currentWallet - totalPrice

        // Update shared preferences with the new wallet balance
        sharedPreferences.edit().putString("wallet", updatedWallet.toString()).apply()

        updatewalletpengguna(penggunaId, updatedWallet)

        // Optionally log or perform actions with the updated wallet balance
        Log.d("WalletUpdate", "Wallet updated: Old Balance = $currentWallet, New Balance = $updatedWallet")
    }



    private fun calculateReturnDate(days: Int): String {
        val calendar = Calendar.getInstance()

        // Tambahkan jumlah hari ke tanggal saat ini
        calendar.add(Calendar.DAY_OF_MONTH, days)

        // Format tanggal menjadi "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun confirmRent(penggunaId: String, productId: Int, days: Int, totalPrice: Double, imagepath: String, ownerid:String, productName:String, returnDate:String, wallet:Double) {
        val rentRequest = RentRequest(
            renter_id = penggunaId.toInt(),
            item_id = productId,
            durasi = days,
            harga_total = totalPrice,
            image_path = imagepath,
            owner_id = ownerid.toInt(),
            nama_produk = productName,
            tgl_pengembalian = returnDate,
            status = 0
        )
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

                        val newRental = RentalHistoryItem(
                            rent_id = productId, // Atau ID lain yang sesuai
                            nama_produk = productName,
                            harga_total = totalPrice,
                            durasi = days,
                            tgl_pengembalian = returnDate,
                            image_path = imagepath,
                            status = 0)
                        rentalHistoryList.add(newRental)

                        rentalHistoryAdapter.notifyItemInserted(rentalHistoryList.size - 1)
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
                Log.d(TAG, "imagepath: $imagepath")
                Log.d(TAG, "ownerid: $ownerid")
                Log.d(TAG, "productName: $productName")
                Log.d(TAG, "returnDate: $returnDate")
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun updatewalletpengguna(penggunaId: String, wallets: Double) {

        RetrofitInstance.apiService.updatewallet(penggunaId = penggunaId,
            wallet = wallets).enqueue(object : Callback<walletResponse> {
            override fun onResponse(call: Call<walletResponse>, response: Response<walletResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val statResponse = response.body()!!
                    if (statResponse.status == "true") {
                        Toast.makeText(this@CheckoutActivity, "Status wallet berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        // Tambahkan aksi jika perlu, seperti refresh data
                    } else {
                        // Tampilkan pesan error dari server
                        Toast.makeText(this@CheckoutActivity, statResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileActivity", "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<walletResponse>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatestatusitem0(productId: String) {

        RetrofitInstance.apiService.updateitemstatus(item_id = productId,
            status = "0").enqueue(object : Callback<itemStatusResponse> {
            override fun onResponse(call: Call<itemStatusResponse>, response: Response<itemStatusResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val statResponse = response.body()!!
                    if (statResponse.status == "true") {
                        Toast.makeText(this@CheckoutActivity, "Status item berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        // Tambahkan aksi jika perlu, seperti refresh data
                    } else {
                        // Tampilkan pesan error dari server
                        Toast.makeText(this@CheckoutActivity, statResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "productId: $productId")
                    Toast.makeText(this@CheckoutActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileActivity", "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<itemStatusResponse>, t: Throwable) {
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
