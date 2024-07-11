package com.hakancevik.eterationtestcaseapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.databinding.FragmentHomeBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.extension.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val localProductList: ArrayList<ProductEntity> = arrayListOf()
    private var productListData: ArrayList<ProductData> = arrayListOf()

    @Inject
    lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewProducts: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeData()
        viewModel.fetchAllProducts()
        viewModel.fetchLocalProductData()
    }

    private fun setupUI() {
        recyclerViewProducts = binding.recyclerViewProducts

        recyclerViewProducts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }



        productAdapter.setOnItemClickListener(
            listener = { product ->
                val action = HomeFragmentDirections.actionNavigationHomeToProductDetailFragment(product.id)
                findNavController().navigate(action)
            },
            onStarClickListener = { id, count, isFavorite ->
                viewModel.insertLocalProduct(
                    ProductEntity(
                        id = id, count = count, isFavorite = isFavorite
                    )
                )
            },
            onCartClickListener = { id, count, isFavorite ->
                viewModel.insertLocalProduct(
                    ProductEntity(
                        id = id, count = count, isFavorite = isFavorite
                    )
                )


            }
        )




    }

    private fun observeData() {
        viewModel.products.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { productList ->
                        productListData.clear()
                        productListData.addAll(productList)
                        productAdapter.submitList(productListData)

                    }
                }

                Status.ERROR -> {
                }

                Status.LOADING -> {
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.localProductDetail.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { product ->
                        localProductList.clear()
                        localProductList.addAll(product)
                        productAdapter.updateLocalData(product)


                    }
                }

                Status.ERROR -> {
                    // Handle error
                }

                Status.LOADING -> {
                    // Handle loading
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}


