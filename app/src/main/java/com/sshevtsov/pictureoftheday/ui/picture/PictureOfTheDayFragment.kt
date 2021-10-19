package com.sshevtsov.pictureoftheday.ui.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sshevtsov.pictureoftheday.databinding.MainFragmentBinding

class PictureOfTheDayFragment : Fragment() {

    companion object {
        fun newInstance(): PictureOfTheDayFragment = PictureOfTheDayFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }
}