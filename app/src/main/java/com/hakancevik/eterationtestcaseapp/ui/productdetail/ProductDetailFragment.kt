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
import com.hakancevik.eterationtestcaseapp.databinding.FragmentProductDetailBinding
import com.hakancevik.eterationtestcaseapp.extension.Status
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

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        productId = args.productId
        productId.let {
            viewModel.fetchProductDetail(it)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBackIcon(true)
    }

    private fun observeViewModel() {
        viewModel.productDetail.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { product ->

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
    }


    private fun updateToolbarTitle(title: String) {
        (activity as? MainActivity)?.updateToolbarTitle(title)
    }

}
