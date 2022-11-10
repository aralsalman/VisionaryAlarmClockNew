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


class MapsFragment : Fragment(), OnMapReadyCallback {

    lateinit var map: GoogleMap
    lateinit var fragmentMapBinding: FragmentMapsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
    //    fragmentMapBinding =
    //        DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)

    //    return fragmentMapBinding.root


        val v = inflater.inflate(R.layout.fragment_maps, container, false)



        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager?.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        var city: LatLng = LatLng(42.670319, -83.222260)
        var option: MarkerOptions = MarkerOptions()
        option.position(city).title("CityName")
        map.addMarker(option)
        map.moveCamera(CameraUpdateFactory.newLatLng(city))

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






