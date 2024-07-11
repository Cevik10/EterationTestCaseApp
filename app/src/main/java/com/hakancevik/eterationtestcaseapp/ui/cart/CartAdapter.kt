package com.hakancevik.eterationtestcaseapp.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.databinding.CartSingleRowBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData


data class CartItem(
    val productData: ProductData,
    val productEntity: ProductEntity
)

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartItems = mutableListOf<CartItem>()

    var onIncreaseClick: ((CartItem) -> Unit)? = null
    var onDecreaseClick: ((CartItem) -> Unit)? = null

    inner class CartViewHolder(private val binding: CartSingleRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.productName.text = cartItem.productData.name
            binding.productPrice.text = cartItem.productData.price
            binding.productCountDisplay.text = cartItem.productEntity.count.toString()

            binding.buttonMinus.setOnClickListener {
                onDecreaseClick?.invoke(cartItem)
            }

            binding.buttonPlus.setOnClickListener {
                onIncreaseClick?.invoke(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
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