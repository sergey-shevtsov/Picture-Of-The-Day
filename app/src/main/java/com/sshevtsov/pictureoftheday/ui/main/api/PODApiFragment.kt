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
import com.sshevtsov.pictureoftheday.util.POD_DESCRIPTION_MODE_KEY
import com.sshevtsov.pictureoftheday.util.POD_HD_MODE_KEY
import com.sshevtsov.pictureoftheday.util.getBooleanSettingFromSharedPref

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

    private var showDescriptionSetting: Boolean = true
    private var hdQualitySetting: Boolean = false

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

        showDescriptionSetting =
            requireActivity().getBooleanSettingFromSharedPref(POD_DESCRIPTION_MODE_KEY)
        hdQualitySetting =
            requireActivity().getBooleanSettingFromSharedPref(POD_HD_MODE_KEY)

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
                    binding.loadingInclude.loadingLayout.visibility = View.GONE

                    val url = if (hdQualitySetting) {
                        data.serverResponseData.hdurl
                    } else {
                        data.serverResponseData.url
                    }

                    imageView.load(url) {
                        lifecycle(this@PODApiFragment)
                        error(R.drawable.ic_load_error_vector)
                    }

                    if (showDescriptionSetting) {
                        bottomSheetInclude.bottomSheetHeader.text =
                            data.serverResponseData.title
                        bottomSheetInclude.bottomSheetDescription.text =
                            data.serverResponseData.explanation
                        bottomSheetDialog.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.loadingInclude.loadingLayout.visibility = View.VISIBLE
                bottomSheetDialog.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is PictureOfTheDayData.Error -> {
                binding.loadingInclude.loadingLayout.visibility = View.GONE
                binding.imageView.load(R.drawable.ic_load_error_vector)
            }
        }
    }

}