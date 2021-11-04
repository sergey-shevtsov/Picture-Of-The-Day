package com.sshevtsov.pictureoftheday.ui.mars

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.clear
import coil.api.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.FragmentMarsBinding
import com.sshevtsov.pictureoftheday.ui.mars.api.MarsRoverPhotosData
import com.sshevtsov.pictureoftheday.ui.mars.api.MarsRoverPhotosViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

private const val ROVER_LANDING_YEAR = 2012
private const val ROVER_LANDING_MONTH = 7
private const val ROVER_LANDING_DATE = 6

class MarsFragment : Fragment() {

    companion object {
        fun newInstance() = MarsFragment()
    }

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this).get(MarsRoverPhotosViewModel::class.java)
    }

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    private val roverLandingDate: Calendar by lazy {
        Calendar.getInstance().also {
            it.set(ROVER_LANDING_YEAR, ROVER_LANDING_MONTH, ROVER_LANDING_DATE)
        }
    }
    private lateinit var constraintBuilder: CalendarConstraints
    private lateinit var datePicker: MaterialDatePicker<Long>
    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initChooseButton()
        initRandomButton()
    }

    private fun initViewModel() {
        selectRandomDate()

        viewModel.getData(selectedDate)
            .observe(viewLifecycleOwner) { data ->
                when (data) {
                    is MarsRoverPhotosData.Loading -> {
                        binding.loadingInclude.loadingLayout.visibility = View.VISIBLE
                        binding.imageView.clear()
                    }
                    is MarsRoverPhotosData.Success -> {
                        binding.loadingInclude.loadingLayout.visibility = View.GONE

                        var url = data.serverResponseData.photos?.get(0)?.imageSrc

                        if (url!![4] != 's') {
                            url = url.replaceRange(0, 4, "https")
                        }

                        binding.imageView.load(url) {
                            lifecycle(this@MarsFragment)
                            error(R.drawable.ic_load_error_vector)
                            listener(
                                onSuccess = { _, _ ->
                                    ObjectAnimator
                                        .ofFloat(binding.imageView, "alpha", 0f, 1f)
                                        .setDuration(500)
                                        .start()
                                }
                            )
                        }
                    }
                    is MarsRoverPhotosData.Error -> {
                        binding.loadingInclude.loadingLayout.visibility = View.GONE
                        binding.imageView.load(R.drawable.ic_load_error_vector) {
                            listener(
                                onSuccess = { _, _ ->
                                    ObjectAnimator
                                        .ofFloat(binding.imageView, "alpha", 0f, 1f)
                                        .setDuration(250)
                                        .start()
                                }
                            )
                        }
                        Toast.makeText(context, data.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun initChooseButton() {

        binding.buttonChooseDate.setOnClickListener {

            constraintBuilder = CalendarConstraints.Builder()
                .setStart(roverLandingDate.timeInMillis)
                .setEnd(System.currentTimeMillis())
                .setOpenAt(selectedDate)
                .build()

            datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintBuilder)
                .setSelection(selectedDate)
                .setTitleText(getString(R.string.select_date))
                .build()

            datePicker.addOnPositiveButtonClickListener {
                datePicker.selection?.let { selection ->
                    selectedDate = selection
                }
                viewModel.getData(selectedDate)
                renderSelectedDate()
            }

            datePicker.show(parentFragmentManager, null)
        }
    }

    private fun initRandomButton() {
        binding.buttonRandomDate.setOnClickListener {
            selectRandomDate()
            viewModel.getData(selectedDate)
        }
    }

    private fun selectRandomDate() {
        selectedDate = Random.nextLong(roverLandingDate.timeInMillis, System.currentTimeMillis())
        renderSelectedDate()
    }

    private fun renderSelectedDate() {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        binding.textViewChosenDate.text =
            String.format(
                Locale.getDefault(), "%s %s",
                getString(R.string.mars_rover_selected_date), sdf.format(selectedDate)
            )
    }
}