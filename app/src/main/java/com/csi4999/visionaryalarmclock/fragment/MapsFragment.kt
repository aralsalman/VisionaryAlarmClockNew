package com.csi4999.visionaryalarmclock.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.databinding.FragmentMapsBinding


class MapsFragment : Fragment() {

    lateinit var fragmentMapBinding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)

        return fragmentMapBinding.root
    }


    }







