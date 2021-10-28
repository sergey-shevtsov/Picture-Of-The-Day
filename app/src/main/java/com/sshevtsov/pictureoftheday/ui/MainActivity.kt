package com.sshevtsov.pictureoftheday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
            when (item.itemId) {
                R.id.main -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit()
                    true
                }
                R.id.mars -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, MarsFragment.newInstance())
                        .commit()
                    true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .commit()
                    true
                }
                else -> false
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