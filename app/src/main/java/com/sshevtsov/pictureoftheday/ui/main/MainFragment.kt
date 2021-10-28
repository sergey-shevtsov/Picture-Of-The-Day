package com.sshevtsov.pictureoftheday.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.MainFragmentBinding
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var switchImageInProcess = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChipsListener()
        setBottomSheetBehavior(binding.bottomSheetInclude.bottomSheetContainer)

        viewModel.getData(System.currentTimeMillis())
            .observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun setChipsListener() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->

            switchImageInProcess = true

            when (checkedId) {
                R.id.chip_today -> {
                    viewModel.getData(System.currentTimeMillis())
                }
                R.id.chip_yesterday -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DATE, -1)
                    viewModel.getData(calendar.timeInMillis)
                }
                R.id.chip_before_yesterday -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DATE, -2)
                    viewModel.getData(calendar.timeInMillis)
                }
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                binding.apply {
                    imageView.load(data.serverResponseData.url) {
                        lifecycle(this@MainFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    bottomSheetInclude.bottomSheetHeader.text =
                        data.serverResponseData.title
                    bottomSheetInclude.bottomSheetDescription.text =
                        data.serverResponseData.explanation
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    switchImageInProcess = false
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.imageView.load(R.drawable.ic_no_photo_vector)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            is PictureOfTheDayData.Error -> {
                binding.imageView.load(R.drawable.ic_load_error_vector)
            }
        }
    }
}