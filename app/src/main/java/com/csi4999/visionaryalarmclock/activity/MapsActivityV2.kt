package com.csi4999.visionaryalarmclock.activity
import android.app.PendingIntent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.csi4999.visionaryalarmclock.R
import com.csi4999.visionaryalarmclock.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.card.MaterialCardView
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.layers.generated.LocationIndicatorLayer
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.locationcomponent.location


class MapsActivityV2 : AppCompatActivity(), OnMapReadyCallback, LocationEngine, PermissionsListener{

    var mapView: MapView? = null

    //can remove below vars
    private lateinit var map: MapboxMap
    lateinit var permissionsManager: PermissionsManager
    lateinit var locationEngine: LocationEngine
    lateinit var locationIndicatorLayer: LocationIndicatorLayer
    lateinit var originLocation: Location
    //remove below var
    private val navigationLocationProvider = NavigationLocationProvider()
    //remove below class
    class NavigationLocationProvider {

    }


    lateinit var mapSearchBtn: Button
    lateinit var mapLayout: LinearLayout
    lateinit var mapSearchLay: LinearLayout
    lateinit var matcard: MaterialCardView



    private lateinit var binding: ActivityMapsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //can remove next 3 lines
       // Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        mapView = findViewById(com.csi4999.visionaryalarmclock.R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)

        //can remove chunk below
        mapView?.location?.apply {
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@MapsActivityV2,
                    R.drawable.appicon
                )
            )
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }

        //mapSearchBtn=findViewById(R.id.MapSearchButton)
        //mapLayout=findViewById(R.id.mapLinearLayout)
        //mapSearchLay=findViewById(R.id.mapSearchLinLay)
        //matcard=findViewById(R.id.materialCardVieww)


        val matcard = findViewById<MaterialCardView>(R.id.materialCardVieww)
        val mapSearchLay = findViewById<LinearLayout>(R.id.mapSearchLinLay)
        val mapSearchBtn = findViewById<Button>(R.id.MapSearchButton)
        val mapLayout = findViewById<LinearLayout>(R.id.mapLinearLayout)

        matcard.setOnClickListener(clickListener)
        mapSearchLay.setOnClickListener(clickListener)
        mapSearchBtn.setOnClickListener(clickListener)
        mapLayout.setOnClickListener(clickListener)
        Log.d("TAG", "Clicked!!")
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            //R.id.materialCardVieww -> firstFun()
            //R.id.mapSearchLinLay -> firstFun()
            //R.id.MapSearchButton -> firstFun()
            //R.id.mapLinearLayout -> firstFun()
        }
    }

    private fun firstFun(){
        Log.d("TAG", "Clicked!!")
        //matcard.visibility = View.GONE
        //mapSearchLay.visibility = View.VISIBLE
        //mapSearchBtn.visibility = View.VISIBLE
        //mapLayout.visibility = View.VISIBLE
    }

    //can remove below function
    private fun setLocationProvider(locationProvider: MapsActivityV2.NavigationLocationProvider) {
        TODO("Not yet implemented. Need to learn the API.")
    }


    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented. Need to figure out " +
                "how to do this with mapbox and not google maps")
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun getLastLocation(callback: LocationEngineCallback<LocationEngineResult>) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun requestLocationUpdates(
        request: LocationEngineRequest,
        callback: LocationEngineCallback<LocationEngineResult>,
        looper: Looper?
    ) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun requestLocationUpdates(
        request: LocationEngineRequest,
        pendingIntent: PendingIntent?
    ) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun removeLocationUpdates(callback: LocationEngineCallback<LocationEngineResult>) {
        TODO("Not yet implemented. Need to learn the API.")
    }

    override fun removeLocationUpdates(pendingIntent: PendingIntent?) {
        TODO("Not yet implemented. Need to learn the API.")
    }




}