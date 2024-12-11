package com.example.halamanlogin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.Network.ApiService
import com.example.halamanlogin.R
import com.example.halamanlogin.Model.RentalHistoryItem
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentalHistoryAdapter(
    private val context: Context,
    private val rentalList: List<RentalHistoryItem>,
    private val apiService: ApiService
) : RecyclerView.Adapter<RentalHistoryAdapter.RentalViewHolder>() {

    inner class RentalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val durasi: TextView = itemView.findViewById(R.id.durasi)
        val tglPengembalian: TextView = itemView.findViewById(R.id.tglPengembalian)
        val hargaTotal: TextView = itemView.findViewById(R.id.hargaTotal)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
        val actionButton: Button = itemView.findViewById(R.id.actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rental_history, parent, false)
        return RentalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RentalViewHolder, position: Int) {
        val rental = rentalList[position]
        holder.itemName.text = rental.product_name
        holder.durasi.text = "${rental.durasi} hari"
        holder.tglPengembalian.text = "Tanggal Pengembalian: ${rental.tgl_pengembalian}"
        holder.hargaTotal.text = "Rp. ${String.format("%,.0f", rental.harga_total)}"

        // Load image menggunakan Picasso
        Picasso.get().load("http://192.168.1.12:8000${rental.image_path}").into(holder.itemImage)

        // Set status text dan tombol aksi berdasarkan status
        when (rental.status) {
            0 -> {
                holder.statusText.text = "Status: Sedang Dikirim"
                holder.actionButton.visibility = View.GONE
            }
            1 -> {
                holder.statusText.text = "Status: Diterima"
                holder.actionButton.visibility = View.VISIBLE
                holder.actionButton.text = "Konfirmasi Barang Diterima"
                holder.actionButton.setOnClickListener {
                    updateRentalStatus(rental.rent_id, 2)
                }
            }
            2 -> {
                holder.statusText.text = "Status: Disewa"
                holder.actionButton.visibility = View.VISIBLE
                holder.actionButton.text = "Akhiri Menyewa"
                holder.actionButton.setOnClickListener {
                    updateRentalStatus(rental.rent_id, 3)
                }
            }
            3 -> {
                holder.statusText.text = "Status: Dikembalikan"
                holder.actionButton.visibility = View.GONE
            }
            4 -> {
                holder.statusText.text = "Status: Selesai"
                holder.actionButton.visibility = View.GONE
            }
            else -> {
                holder.statusText.text = "Status: Tidak Diketahui"
                holder.actionButton.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = rentalList.size

    private fun updateRentalStatus(rentId: Int, newStatus: Int) {
        val call = apiService.updateRentalStatus(rentId, newStatus)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Status penyewaan diperbarui", Toast.LENGTH_SHORT).show()
                    // Anda mungkin perlu memperbarui daftar penyewaan setelah perubahan
                    // Misalnya, dengan memanggil kembali data dari API
                } else {
                    Toast.makeText(context, "Gagal memperbarui status: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
