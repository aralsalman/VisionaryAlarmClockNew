package com.csi4999.visionaryalarmclock.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.databinding.ActivityMapsBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.android.core.permissions.PermissionsManager;

import android.widget.Toast

import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.location.Location
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap

import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference



class MapsActivityV2 : AppCompatActivity() {

    var mapView: MapView? = null
    private lateinit var binding: ActivityMapsBinding

    private lateinit var map: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
    }





}