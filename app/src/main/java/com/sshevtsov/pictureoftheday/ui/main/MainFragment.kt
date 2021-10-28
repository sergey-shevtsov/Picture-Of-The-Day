package com.sshevtsov.pictureoftheday.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.MainFragmentBinding
import com.sshevtsov.pictureoftheday.ui.main.api.PODViewPagerAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        binding.viewPager.adapter = PODViewPagerAdapter(requireActivity())
        binding.viewPager.currentItem = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.before_yesterday)
                }
                1 -> {
                    tab.text = getString(R.string.yesterday)
                }
                2 -> {
                    tab.text = getString(R.string.today)
                }
            }
        }.attach()
    }
}