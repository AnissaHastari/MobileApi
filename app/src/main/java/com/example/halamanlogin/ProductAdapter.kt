package com.yourpackage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.halamanlogin.R
import Product

class ProductAdapter(
    private val context: Context,
    private var productList: List<Product>,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(product: Product) {
            productName.text = product.name
            Glide.with(context).load(product.imageUrl).into(productImage)

            itemView.setOnClickListener {
                onClick(product)
            }
        }
    }
}
