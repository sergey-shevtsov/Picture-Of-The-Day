package com.sshevtsov.pictureoftheday.ui.main.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.PodApiFragmentBinding

class PODApiFragment : Fragment() {

    companion object {
        const val DATE_EXTRA_KEY = "DATE_EXTRA"
        fun newInstance(bundle: Bundle): PODApiFragment {
            return PODApiFragment().also {
                it.arguments = bundle
            }
        }
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: PodApiFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetDialog: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PodApiFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.bottomSheetInclude.bottomSheetContainer)

        val date = arguments?.getLong(DATE_EXTRA_KEY)
        viewModel.getData(date!!).observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetDialog = BottomSheetBehavior.from(bottomSheet)
        bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.apply {
                    imageView.load(data.serverResponseData.url) {
                        lifecycle(this@PODApiFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottomSheetInclude.bottomSheetHeader.text =
                        data.serverResponseData.title
                    bottomSheetInclude.bottomSheetDescription.text =
                        data.serverResponseData.explanation
                    bottomSheetDialog.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.imageView.load(R.drawable.ic_no_photo_vector)
                bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is PictureOfTheDayData.Error -> {
                binding.imageView.load(R.drawable.ic_load_error_vector)
            }
        }
    }

}