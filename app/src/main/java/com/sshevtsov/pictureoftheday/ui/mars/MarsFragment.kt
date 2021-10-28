package com.sshevtsov.pictureoftheday.ui.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sshevtsov.pictureoftheday.databinding.FragmentMarsBinding

class MarsFragment : Fragment() {

    companion object {
        fun newInstance() = MarsFragment()
    }

    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(layoutInflater)

        return binding.root
    }
}