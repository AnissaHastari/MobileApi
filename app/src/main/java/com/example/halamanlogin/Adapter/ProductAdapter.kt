package com.example.halamanlogin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.R
import com.example.halamanlogin.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(private val onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Inflate the layout using ViewBinding
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    // Function to update the data in the adapter
    fun submitList(products: List<Product>) {
        val startPosition = productList.size
        productList.addAll(products)
        notifyItemRangeInserted(startPosition, products.size)
    }

    // ViewHolder that uses ViewBinding to bind the views
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            // Bind data to the views using the ViewBinding object
            binding.productNameTextView.text = product.nama_produk
            val rp = "Rp. " + product.harga
            binding.productPriceTextView.text = rp

            // Load the image using Picasso or Glide
            Picasso.get().load("http://192.168.147.128:8000${product.image_path}")
                .placeholder(R.drawable.placeholder) // Placeholder saat gambar sedang dimuat
                .resize(300, 300) // Ubah ukuran gambar untuk menghemat memori
                .centerCrop() // Crop gambar agar sesuai dengan dimensi tampilan
                .into(binding.productImageView)

            // Set the item click listener
            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}
