// File: com/example/halamanlogin/Adapter/ProductAdapter.kt

package com.example.halamanlogin.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.halamanlogin.Model.Product
import com.example.halamanlogin.R
import com.example.halamanlogin.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(private val onClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList: List<Product> = listOf()

    fun submitList(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            // Menampilkan gambar produk
            Picasso.get()
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder) // Pastikan drawable ini ada
                .error(R.drawable.error) // Pastikan drawable ini ada
                .into(binding.productImageView)

            binding.productNameTextView.text = product.name
            binding.productPriceTextView.text = "Rp ${product.price}"

            // Menangani klik produk
            binding.root.setOnClickListener {
                onClick(product)
            }
        }
    }
}
