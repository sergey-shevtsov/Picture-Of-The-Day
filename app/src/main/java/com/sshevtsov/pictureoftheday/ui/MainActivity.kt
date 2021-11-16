package com.sshevtsov.pictureoftheday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.databinding.ActivityMainBinding
import com.sshevtsov.pictureoftheday.ui.main.MainFragment
import com.sshevtsov.pictureoftheday.ui.mars.MarsFragment
import com.sshevtsov.pictureoftheday.ui.settings.SettingsFragment
import com.sshevtsov.pictureoftheday.util.DARK_MODE_KEY
import com.sshevtsov.pictureoftheday.util.getBooleanSettingFromSharedPref

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setSavedTheme()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBottomNavigation()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.isChecked && supportFragmentManager.backStackEntryCount == 0) {
                return@setOnItemSelectedListener false
            }
            clearBackStack()

            when (item.itemId) {
                R.id.main -> {
                    replaceFragment(MainFragment.newInstance())
                    true
                }
                R.id.mars -> {
                    replaceFragment(MarsFragment.newInstance())
                    true
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun clearBackStack() {
        supportFragmentManager.apply {
            for (i in 0 until backStackEntryCount) {
                popBackStack()
            }
        }
    }

    private fun setSavedTheme() {
        if (getBooleanSettingFromSharedPref(DARK_MODE_KEY)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}