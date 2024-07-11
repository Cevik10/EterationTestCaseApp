package com.hakancevik.eterationtestcaseapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.hakancevik.eterationtestcaseapp.databinding.FragmentHomeBinding
import com.hakancevik.eterationtestcaseapp.domain.entity.ProductData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

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

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}


