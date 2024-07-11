package com.hakancevik.eterationtestcaseapp.ui.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.hakancevik.eterationtestcaseapp.MainActivity
import com.hakancevik.eterationtestcaseapp.R
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.databinding.FragmentProductDetailBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.extension.Status
import com.hakancevik.eterationtestcaseapp.extension.customToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()
    private var productId = ""
    private var localProductCount = 0
    private var isFavorite = false

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        productId = args.productId
        productId.let {
            viewModel.fetchLocalProductData()
            viewModel.fetchProductDetail(it)
        }

        observeViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBackIcon(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBackIcon(false)
    }

    private fun observeViewModel() {
        viewModel.productDetail.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { product ->
                        setupUI(product)
                        updateToolbarTitle(product.name)
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

        viewModel.localProductDetail.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { product ->
                        localProductCount = product.firstOrNull {
                            it.id == productId
                        }?.count ?: 0
                        isFavorite = product.firstOrNull {
                            it.id == productId
                        }?.isFavorite ?: false
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

    private fun setupUI(product: ProductData) {
        binding.apply {
            glide.load(product.image).into(productImage)
            productTitle.text = product.name
            productDescription.text = product.description
            val priceFormat = getString(R.string.price_format)
            priceAmount.text = String.format(priceFormat, product.price)
            addToCartButton.setOnClickListener {
                localProductCount++
                viewModel.insertLocalProduct(ProductEntity(product.id, localProductCount, isFavorite))
                requireActivity().customToast("Product added to cart.")
            }
            if (isFavorite) {
                productStarIcon.setImageResource(R.drawable.ic_star_24)
            } else {
                productStarIcon.setImageResource(R.drawable.ic_outline_star_24)
            }

            productStarIcon.setOnClickListener {
                isFavorite = !isFavorite
                viewModel.insertLocalProduct(ProductEntity(productId, isFavorite = isFavorite, count = localProductCount))
                if (isFavorite) {
                    productStarIcon.setImageResource(R.drawable.ic_star_24)
                } else {
                    productStarIcon.setImageResource(R.drawable.ic_outline_star_24)
                }
            }
        }
    }

    private fun updateToolbarTitle(title: String) {
        (activity as? MainActivity)?.updateToolbarTitle(title)
    }
}