package com.hakancevik.eterationtestcaseapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.hakancevik.eterationtestcaseapp.databinding.FavoriteProductSingleRowBinding
import com.hakancevik.eterationtestcaseapp.ui.cart.CartItem


class FavoriteAdapter(
    private val glide: RequestManager
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()

    var onRemoveButtonClick: ((CartItem) -> Unit)? = null


    inner class FavoriteViewHolder(private val binding: FavoriteProductSingleRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.productName.text = cartItem.productData.name
            binding.productPrice.text = cartItem.productData.price

            glide.load(cartItem.productData.image).into(binding.productImage)

            binding.productRemoveButton.setOnClickListener {
                onRemoveButtonClick?.invoke(cartItem)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteProductSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        val diffCallback = CartItemsDiffCallback(cartItems, newCartItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        cartItems.clear()
        cartItems.addAll(newCartItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class CartItemsDiffCallback(
    private val oldList: List<CartItem>,
    private val newList: List<CartItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].productData.id == newList[newItemPosition].productData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}