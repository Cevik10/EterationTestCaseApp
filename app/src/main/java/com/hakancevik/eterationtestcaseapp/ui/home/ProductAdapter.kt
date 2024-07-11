package com.hakancevik.eterationtestcaseapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hakancevik.eterationtestcaseapp.databinding.ProductSingleRowBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData

class ProductAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductData, newItem: ProductData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<ProductData>) {
        differ.submitList(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.apply {
            binding.apply {

                productName.text = product.name
                productPrice.text = "${product.price} ₺"
                glide.load(product.image).into(productImage)

            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size


    class ProductViewHolder(val binding: ProductSingleRowBinding) : RecyclerView.ViewHolder(binding.root)
}
