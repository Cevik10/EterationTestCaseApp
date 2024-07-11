package com.hakancevik.eterationtestcaseapp.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hakancevik.eterationtestcaseapp.databinding.FragmentFilterDialogBinding
import java.util.Locale

class FilterDialogFragment : DialogFragment() {

    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!
    var listener: FilterDialogListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.applyButton.setOnClickListener {
            val filterCriteria = getFilterCriteria()
            listener?.onFilterApply(filterCriteria)
            dismiss()
        }

        binding.priceMaxSlider.addOnChangeListener { _, fl, _ ->
            binding.maxPriceText.text = String.format(Locale.getDefault(), "Max Price: %.2f", fl)
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun getFilterCriteria(): FilterCriteria {
        return FilterCriteria(
            priceRange = binding.priceMaxSlider.value,
            isFavorite = binding.favoriteSwitch.isChecked,
            isInCart = binding.cartSwitch.isChecked
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class FilterCriteria(
    val priceRange: Float,
    val isFavorite: Boolean,
    val isInCart: Boolean
)
