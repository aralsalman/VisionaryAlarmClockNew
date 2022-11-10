package com.csi4999.visionaryalarmclock.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.databinding.FragmentNewsBinding


class NewsFragment : Fragment() {

    lateinit var fragmentNewsBinding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentNewsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        return fragmentNewsBinding.root
    }


}