package com.sshevtsov.pictureoftheday.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sshevtsov.pictureoftheday.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}