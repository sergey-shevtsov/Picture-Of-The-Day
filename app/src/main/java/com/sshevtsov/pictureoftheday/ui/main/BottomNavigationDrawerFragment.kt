package com.sshevtsov.pictureoftheday.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.sshevtsov.pictureoftheday.R

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomNavigationDrawer"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_navigation_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_navigation_one -> {
                    Toast.makeText(context, "Page 1", Toast.LENGTH_SHORT).show()
                    dismiss()
                    true
                }
                R.id.bottom_navigation_two -> {
                    Toast.makeText(context, "Page 2", Toast.LENGTH_SHORT).show()
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }
}