package com.csi4999.visionaryalarmclock.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.activity.MapsActivity
import com.csi4999.visionaryalarmclock.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class   MapsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment = inflater.inflate(R.layout.fragment_maps, container, false)

        return fragment
    }




}

// public class MapsFragment extends Fragment {
  //  public MapsFragment() {
        //empty public constructor
  //  }

   // @override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
      //  Bundle savedInstanceState) {
    //    return inflater.inflate(R.layout.fragment_maps,container, false)
    //}
//}






