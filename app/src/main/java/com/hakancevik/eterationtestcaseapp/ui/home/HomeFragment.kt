package com.hakancevik.eterationtestcaseapp.ui.home

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.eterationtestcaseapp.R
import com.hakancevik.eterationtestcaseapp.data.model.ProductEntity
import com.hakancevik.eterationtestcaseapp.databinding.FragmentHomeBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import com.hakancevik.eterationtestcaseapp.extension.Status
import com.hakancevik.eterationtestcaseapp.extension.createCustomProgressDialog
import com.hakancevik.eterationtestcaseapp.extension.customToast
import com.hakancevik.eterationtestcaseapp.ui.home.filter.FilterCriteria
import com.hakancevik.eterationtestcaseapp.ui.home.filter.FilterDialogFragment
import com.hakancevik.eterationtestcaseapp.ui.home.filter.FilterDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), FilterDialogListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val localProductList: ArrayList<ProductEntity> = arrayListOf()
    private var productListData: ArrayList<ProductData> = arrayListOf()

    @Inject
    lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewProducts: RecyclerView

    private lateinit var progressDialog: AlertDialog


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

                requireActivity().customToast("Product added to cart")

            }
        )


        val textColor = ContextCompat.getColor(requireContext(), R.color.gray)
        val searchViewHintText = binding.productSearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchViewHintText.setTextColor(textColor)

        val textFont = ResourcesCompat.getFont(requireContext(), R.font.exo_medium)
        searchViewHintText.typeface = textFont

        searchViewHintText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

        val searchIcon = binding.productSearch.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        searchViewHintText.hint = "Search"
        val hintColor = ContextCompat.getColor(requireContext(), R.color.gray)
        searchViewHintText.setHintTextColor(hintColor)



        binding.productSearch.setOnClickListener {
            binding.productSearch.isIconified = false
        }


        binding.productSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = productListData.filter {
                    it.name.lowercase().contains(newText.orEmpty().lowercase())
                }
                productAdapter.submitList(filteredList)
                return true
            }

        })


        binding.selectFilter.setOnClickListener {
            val dialog = FilterDialogFragment()
            dialog.listener = this
            dialog.show(parentFragmentManager, "filterDialog")
        }
    }

    private fun observeData() {
        viewModel.products.onEach { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    lifecycleScope.launch {
                        delay(600)
                        progressDialog.dismiss()
                    }

                    resource.data?.let { productList ->
                        productListData.clear()
                        productListData.addAll(productList)
                        productAdapter.submitList(productListData)

                    }
                }

                Status.ERROR -> {
                    progressDialog.dismiss()
                    requireActivity().customToast(getString(R.string.check_your_internet_connection))
                }

                Status.LOADING -> {
                    progressDialog = requireActivity().createCustomProgressDialog(getString(R.string.loading))
                    progressDialog.show()
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

    override fun onFilterApply(filterCriteria: FilterCriteria) {
        val filteredData: ArrayList<ProductData> = arrayListOf()
        if (filterCriteria.isInCart) {
            filteredData.addAll(
                filterProductData(
                    productListData,
                    localProductList
                )
            )
        }
        if (filterCriteria.isFavorite) {
            filteredData.addAll(filterIsFavoriteProductData(productListData, localProductList))
        }
        filteredData.addAll(productListData.filter {
            it.price.toFloat() < filterCriteria.priceRange
        })
        productAdapter.submitList(filteredData)
    }

}

fun filterProductData(
    productListData: List<ProductData>,
    localProductList: List<ProductEntity>
): List<ProductData> {
    val validIds = localProductList.filter { it.count > 0 }.map { it.id }.toSet()
    return productListData.filter { it.id in validIds }
}

fun filterIsFavoriteProductData(
    productListData: List<ProductData>,
    localProductList: List<ProductEntity>
): List<ProductData> {
    val validIds = localProductList.filter { it.isFavorite }.map { it.id }.toSet()
    return productListData.filter { it.id in validIds }
}


