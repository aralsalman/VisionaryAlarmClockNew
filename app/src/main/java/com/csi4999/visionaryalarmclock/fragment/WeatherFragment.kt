package com.csi4999.visionaryalarmclock.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    lateinit var fragmentWeatherBinding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_weather, container, false)

        return fragment
    }


}