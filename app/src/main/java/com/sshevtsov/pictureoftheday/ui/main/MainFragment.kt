package com.sshevtsov.pictureoftheday.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.MainFragmentBinding
import com.sshevtsov.pictureoftheday.ui.main.api.PODViewPagerAdapter
import com.sshevtsov.pictureoftheday.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private val viewPagerAdapter: PODViewPagerAdapter by lazy {
        PODViewPagerAdapter(requireActivity())
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

        initWikiSearch()
        initAnimationListeners()
        initChips()
        initViewPager()
    }

    private fun initWikiSearch() {
        binding.textInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.textInputEditText.text.toString()}")
            })
        }
    }

    private fun initAnimationListeners() {
        binding.main.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                when (currentId) {
                    R.id.closed -> {
                        context?.hideKeyboard(binding.textInputEditText)
                        binding.textInputEditText.clearFocus()
                    }
                }
            }
        })
    }

    private fun initChips() {
        binding.chipFilterDescription.isChecked =
            requireActivity().getBooleanSettingFromSharedPref(POD_DESCRIPTION_MODE_KEY)
        binding.chipFilterHdQuality.isChecked =
            requireActivity().getBooleanSettingFromSharedPref(POD_HD_MODE_KEY)

        binding.chipFilterDescription.setOnCheckedChangeListener { _, isChecked ->
            requireActivity().saveBooleanSettingInSharedPref(POD_DESCRIPTION_MODE_KEY, isChecked)
            requireActivity().recreate()
        }
        binding.chipFilterHdQuality.setOnCheckedChangeListener { _, isChecked ->
            requireActivity().saveBooleanSettingInSharedPref(POD_HD_MODE_KEY, isChecked)
            requireActivity().recreate()
        }
    }

    private fun initViewPager() {
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.today)
                }
                1 -> {
                    tab.text = getString(R.string.yesterday)
                }
                2 -> {
                    tab.text = getString(R.string.before_yesterday)
                }
            }
        }.attach()
    }
}