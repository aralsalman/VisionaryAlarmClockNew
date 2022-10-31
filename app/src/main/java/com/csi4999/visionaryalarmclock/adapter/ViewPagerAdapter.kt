package com.csi4999.visionaryalarmclock.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.csi4999.visionaryalarmclock.fragment.AllReminderFragment

class ViewPagerAdapter(
    lifecycle: Lifecycle,
    fm: FragmentManager?,
    private var totalTabs: Int
) : FragmentStateAdapter(fm!!, lifecycle) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllReminderFragment()
            }
            1 -> {
                AllReminderFragment()
            }
            else -> AllReminderFragment()
        }
    }
}