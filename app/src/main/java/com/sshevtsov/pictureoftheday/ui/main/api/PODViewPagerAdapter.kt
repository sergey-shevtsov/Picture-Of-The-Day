package com.sshevtsov.pictureoftheday.ui.main.api

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class PODViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    companion object {
        private const val TODAY_FRAGMENT = 0
        private const val YESTERDAY_FRAGMENT = 1
        private const val BEFORE_YESTERDAY_FRAGMENT = 2
    }

    private val fragments = getFragmentsArray()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[TODAY_FRAGMENT]
            1 -> fragments[YESTERDAY_FRAGMENT]
            2 -> fragments[BEFORE_YESTERDAY_FRAGMENT]
            else -> fragments[TODAY_FRAGMENT]
        }
    }

    private fun getFragmentsArray(): Array<PODApiFragment> {
        // Получаем даты за сегодня, вчера и позавчера
        val todayDate = System.currentTimeMillis()
        val yesterdayDate = Calendar.getInstance().also { it.add(Calendar.DATE, -1) }
        val beforeYesterdayDate = Calendar.getInstance().also { it.add(Calendar.DATE, -2) }

        // Создаем бандлы, куда помещаем эти даты в формате Long
        val todayBundle = Bundle().also { it.putLong(PODApiFragment.DATE_EXTRA_KEY, todayDate) }
        val yesterdayBundle =
            Bundle().also { it.putLong(PODApiFragment.DATE_EXTRA_KEY, yesterdayDate.timeInMillis) }
        val beforeYesterdayBundle =
            Bundle().also {
                it.putLong(
                    PODApiFragment.DATE_EXTRA_KEY,
                    beforeYesterdayDate.timeInMillis
                )
            }

        // Возвращаем массив с POD фрагментами, куда передаем полученные даты
        return arrayOf(
            PODApiFragment.newInstance(todayBundle),
            PODApiFragment.newInstance(yesterdayBundle),
            PODApiFragment.newInstance(beforeYesterdayBundle)
        )
    }
}