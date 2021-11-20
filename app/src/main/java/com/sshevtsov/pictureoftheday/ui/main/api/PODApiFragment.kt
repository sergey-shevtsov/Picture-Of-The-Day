package com.sshevtsov.pictureoftheday.ui.main.api

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.FragmentPodApiBinding
import com.sshevtsov.pictureoftheday.ui.imagedetail.ImageDetailFragment
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

    private var _binding: FragmentPodApiBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetDialog: BottomSheetBehavior<ConstraintLayout>

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPodApiBinding.inflate(inflater, container, false)
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
                    if (data.serverResponseData.mediaType.equals("video")) {
                        data.serverResponseData.url?.let {
                            binding.loadingInclude.loadingLayout.visibility = View.GONE
                            renderVideoPreview(it)
                        }
                    } else {
                        val url = if (hdQualitySetting) {
                            data.serverResponseData.hdurl
                        } else {
                            data.serverResponseData.url
                        }

                        imageView.load(url) {
                            lifecycle(this@PODApiFragment)
                            error(R.drawable.ic_load_error_vector)
                            listener(
                                onSuccess = { _, _ ->
                                    binding.loadingInclude.loadingLayout.visibility = View.GONE
                                    imageView.animate()
                                        .setDuration(500)
                                        .setInterpolator(LinearOutSlowInInterpolator())
                                        .alpha(1f)
                                    heartSwitch.animate()
                                        .setDuration(500)
                                        .setInterpolator(LinearOutSlowInInterpolator())
                                        .translationX(0f)

                                    imageView.setOnClickListener {
                                        val bundle = Bundle().apply {
                                            putString(
                                                ImageDetailFragment.IMAGE_URL_EXTRA,
                                                data.serverResponseData.hdurl
                                            )
                                        }
                                        requireActivity().supportFragmentManager
                                            .beginTransaction()
                                            .add(
                                                R.id.container,
                                                ImageDetailFragment.newInstance(bundle)
                                            )
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                            .addToBackStack(null)
                                            .commit()
                                    }
                                }
                            )
                        }
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

    private fun renderVideoPreview(url: String) {
        binding.videoFrameLayout.visibility = View.VISIBLE
        binding.buttonShowVideo.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}