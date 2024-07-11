package com.hakancevik.eterationtestcaseapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.eterationtestcaseapp.MainActivity
import com.hakancevik.eterationtestcaseapp.databinding.FragmentCartBinding
import com.hakancevik.eterationtestcaseapp.extension.customToast
import com.hakancevik.eterationtestcaseapp.extension.gone
import com.hakancevik.eterationtestcaseapp.extension.hide
import com.hakancevik.eterationtestcaseapp.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val viewModel: CartViewModel by viewModels()
    private val binding get() = _binding!!

    @Inject
    lateinit var cartAdapter: CartAdapter
    private lateinit var recyclerViewProductsInCart: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViews()
        observeProductData()

    }

    private fun observeProductData() {
        viewModel.fetchProductFromLocal()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collectLatest { resource ->
                    resource.data?.let { cartItems ->
                        if (cartItems.isEmpty()) {
                            binding.apply {
                                noProductLayout.show()
                                recyclerViewProductsInCart.hide()
                                totalPriceLayout.hide()
                                removeCartBadge()
                            }
                        } else {
                            binding.apply {
                                noProductLayout.gone()
                                recyclerViewProductsInCart.show()
                                totalPriceLayout.show()
                            }
                            cartAdapter.updateCartItems(cartItems)
                            calculateTotalPrice(cartItems)
                            updateCartBadgeCount(cartItems.size)

                        }
                    }
                }
            }
        }
    }


    private fun setupViews() {
        binding.completeButton.setOnClickListener {
            requireActivity().customToast("Your order has been received")
        }
    }

    private fun setupRecyclerView() {

        recyclerViewProductsInCart = binding.recyclerViewProductsInCart

        cartAdapter = CartAdapter().apply {
            onIncreaseClick = { cartItem ->
                viewModel.increaseProductCount(cartItem)
            }
            onDecreaseClick = { cartItem ->
                viewModel.decreaseProductCount(cartItem)
            }
        }

        recyclerViewProductsInCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun calculateTotalPrice(cartItems: List<CartItem>) {
        var totalPrice = 0.0
        for (cartItem in cartItems) {
            val price = cartItem.productData.price.toDouble()
            val count = cartItem.productEntity.count
            totalPrice += price * count
        }

        binding.priceAmount.text = String.format(Locale.getDefault(), "%.2f ₺", totalPrice)
    }

    private fun updateCartBadgeCount(count: Int) {
        (activity as? MainActivity)?.updateBadgeCount(count)
    }

    private fun removeCartBadge() {
        (activity as? MainActivity)?.removeBadge()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}