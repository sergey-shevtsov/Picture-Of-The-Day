package com.sshevtsov.pictureoftheday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sshevtsov.pictureoftheday.R
import com.sshevtsov.pictureoftheday.ui.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
    }
}