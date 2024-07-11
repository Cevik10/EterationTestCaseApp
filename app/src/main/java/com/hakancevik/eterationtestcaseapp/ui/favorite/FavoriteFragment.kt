package com.hakancevik.eterationtestcaseapp.ui.favorite

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
import com.hakancevik.eterationtestcaseapp.databinding.FragmentFavoriteBinding
import com.hakancevik.eterationtestcaseapp.extension.hide
import com.hakancevik.eterationtestcaseapp.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val viewModel: FavoriteViewModel by viewModels()
    private val binding get() = _binding!!

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recyclerViewProductsInCart: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
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
                                recyclerViewProductsInCart.hide()
                                binding.noFavoriteLayout.show()
                            }
                        } else {
                            binding.apply {
                                binding.noFavoriteLayout.hide()
                                recyclerViewProductsInCart.show()
                            }
                            favoriteAdapter.updateCartItems(cartItems)
                        }
                    }
                }
            }
        }
    }


    private fun setupRecyclerView() {

        recyclerViewProductsInCart = binding.recyclerViewFavoriteProducts

        favoriteAdapter = favoriteAdapter.apply {
            onRemoveButtonClick = { cartItem ->
                viewModel.updateFavoriteStatus(cartItem)
            }
        }

        recyclerViewProductsInCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}