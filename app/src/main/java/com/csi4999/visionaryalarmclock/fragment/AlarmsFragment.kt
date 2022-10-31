package com.csi4999.visionaryalarmclock.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.activity.AddNewReminder
//import com.qrolic.reminderapp.activity.SearchActivity
import com.csi4999.visionaryalarmclock.adapter.ViewPagerAdapter
import com.csi4999.visionaryalarmclock.databinding.FragmentAlarmsBinding

class AlarmsFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentAlarmsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarms, container, false)
        initAll()
        return fragmentHomeBinding.root
    }

    private fun initAll() {
        setHasOptionsMenu(true)
        with(fragmentHomeBinding) {
            fabAddReminder.setOnClickListener {
                startAddRemiderActivity()
            }
        }


        val tabArray = arrayOf("---------------------------------")
        fragmentHomeBinding.tlMain.addTab(fragmentHomeBinding.tlMain.newTab().setText("Alarms"))
       // fragmentHomeBinding.tlMain.addTab(fragmentHomeBinding.tlMain.newTab().setText("Favorites"))
        fragmentHomeBinding.tlMain.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerAdapter(
            lifecycle,
            childFragmentManager,
            fragmentHomeBinding.tlMain.tabCount
        )
        fragmentHomeBinding.vpMain.adapter = adapter


//        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        TabLayoutMediator(fragmentHomeBinding.tlMain,fragmentHomeBinding.vpMain){tab,position ->
            tab.text=tabArray[position]
        }.attach()

        /*tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //onTabUnselected() method
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //onTabReselected() method
            }
        })*/

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_search, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun startAddRemiderActivity() {
        val intent = Intent(context?.applicationContext, AddNewReminder::class.java);
        startActivity(intent)
    }
}