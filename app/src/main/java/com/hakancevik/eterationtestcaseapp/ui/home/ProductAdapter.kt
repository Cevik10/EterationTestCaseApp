package com.hakancevik.eterationtestcaseapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hakancevik.eterationtestcaseapp.R
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.databinding.ProductSingleRowBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData

class ProductAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var onItemClickListener: ((ProductData) -> Unit)? = null
    private var onStarIconClickListener: ((String, Int, Boolean) -> Unit)? = null
    private var onAddToCartButtonClickListener: ((String, Int, Boolean) -> Unit)? = null
    private val localProductEntity: ArrayList<ProductEntity> = arrayListOf()

    fun setOnItemClickListener(
        listener: (ProductData) -> Unit,
        onStarClickListener: (String, Int, Boolean) -> Unit,
        onCartClickListener: (String, Int, Boolean) -> Unit,
    ) {
        onItemClickListener = listener
        onStarIconClickListener = onStarClickListener
        onAddToCartButtonClickListener = onCartClickListener
    }


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

    fun updateLocalData(localProductList: List<ProductEntity>) {
        localProductEntity.clear()
        localProductEntity.addAll(localProductList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        var isProductStarred = isProductStar(product.id)
        var itemCount = isProductCount(id = product.id)
        holder.apply {
            binding.apply {
                root.setOnClickListener {
                    onItemClickListener?.invoke(product)
                }
                productName.text = product.name
                productPrice.text = "${product.price} ₺"
                glide.load(product.image).into(productImage)
                if (isProductStarred) {
                    productStarIcon.setImageResource(R.drawable.ic_star_24)
                } else {
                    productStarIcon.setImageResource(R.drawable.ic_outline_star_24)
                }
                productStarIcon.apply {
                    setOnClickListener {
                        isProductStarred = !isProductStarred
                        onStarIconClickListener?.invoke(product.id, itemCount, isProductStarred)
                        if (isProductStarred) {
                            setImageResource(R.drawable.ic_star_24)
                        } else {
                            setImageResource(R.drawable.ic_outline_star_24)
                        }
                    }
                }
                addToCartButton.setOnClickListener {
                    itemCount++
                    onAddToCartButtonClickListener?.invoke(product.id, itemCount, isProductStarred)
                }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private fun isProductStar(id: String): Boolean {
        return localProductEntity.any {
            it.id == id && it.isFavorite
        }
    }

    private fun isProductCount(id: String): Int {
        return localProductEntity.firstOrNull() {
            it.id == id
        }?.count ?: 0
    }

    class ProductViewHolder(val binding: ProductSingleRowBinding) : RecyclerView.ViewHolder(binding.root)
}
