package com.sshevtsov.pictureoftheday.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.sshevtsov.pictureoftheday.databinding.SettingsFragmentBinding
import com.sshevtsov.pictureoftheday.util.DARK_MODE_KEY
import com.sshevtsov.pictureoftheday.util.getBooleanSettingFromSharedPref
import com.sshevtsov.pictureoftheday.util.saveBooleanSettingInSharedPref

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

        initDarkModeOption()
    }

    private fun initDarkModeOption() {
        binding.darkModeSwitch.isChecked = requireActivity().getBooleanSettingFromSharedPref(
            DARK_MODE_KEY
        )

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            requireActivity().saveBooleanSettingInSharedPref(DARK_MODE_KEY, isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}