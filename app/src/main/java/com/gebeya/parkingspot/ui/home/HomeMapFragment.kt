package com.gebeya.parkingspot.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gebeya.parkingspot.*
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import kotlinx.android.synthetic.main.fragment_home_map.*
import java.lang.ref.WeakReference

import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HomeMapFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mapboxMap: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationEngine: LocationEngine
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private var callback: HomeMapFragmentLocationCallback = HomeMapFragmentLocationCallback(this)

    private val  SOURCE_ID = "SOURCE_ID"
    private val  ICON_ID = "ICON_ID"
    private val  LAYER_ID = "LAYER_ID"

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    private var resp = ArrayList<Nearest>()
    private var arrayListLoc =ArrayList<List<Double>>()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        val root = inflater.inflate(R.layout.fragment_home_map, container, false)

        retrofitInterface = retrofit!!.create(MyService::class.java)

        //emailAddress.text=sessionManager.fetchEmail()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap =mapboxMap

        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)
        }

    }

    private fun initAddMarker(map: MapboxMap) {
        val symbolLayers = ArrayList<Feature>()
       // symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(38.0, 9.9)))
       // symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(38.4, 8.0)))


       /* val myArrayList = ArrayList<List<Double>>()
        myArrayList.add(listOf(39.0,8.6))
        myArrayList.add(listOf(38.7,9.6))

        */

        /*for(i in myArrayList)
            symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(i[0],i[1])))
        */
        Toast.makeText(requireContext(),"$arrayListLoc[0]",Toast.LENGTH_LONG).show()

        for(i in arrayListLoc)
            symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(i[0],i[1])))

        map.setStyle(
            Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(ICON_ID, BitmapUtils
                    .getBitmapFromDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.mapbox_marker_icon_default))!!)
                .withSource(GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(symbolLayers)))
                .withLayer(
                    SymbolLayer(LAYER_ID, SOURCE_ID)
                    .withProperties(iconImage(ICON_ID), iconSize(1.0f), iconAllowOverlap(true), iconIgnorePlacement(true)))
        )
    }
    @SuppressWarnings("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponent = mapboxMap.locationComponent
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                .useDefaultLocationEngine(false)
                .build()
            locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true                       // Enable to make component visible
                cameraMode = CameraMode.TRACKING                        // Set the component's camera mode
                renderMode = RenderMode.COMPASS                         // Set the component's render mode
            }
            initLocationEngine()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(requireActivity())
        }
    }
    @SuppressWarnings( "MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine.getLastLocation(callback)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(requireContext(), "Permission not granted!!", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap.getStyle {
                enableLocationComponent(it)
            }
        } else {
            Toast.makeText(requireContext(), "Permission not granted!! app will be EXIT", Toast.LENGTH_LONG).show()

        }
    }

    inner class HomeMapFragmentLocationCallback internal constructor(fragment: HomeMapFragment) :
        LocationEngineCallback<LocationEngineResult> {
        private val fragmentWeakReference: WeakReference<HomeMapFragment> = WeakReference(fragment)
        var latitude:Double = 0.0
        var longitude:Double = 0.0


        override fun onSuccess(result: LocationEngineResult?) {
            val fragment: HomeMapFragment? = fragmentWeakReference.get()
            if (fragment != null) {
                val location = result?.lastLocation ?: return
                 latitude = result.lastLocation?.latitude!!
                 longitude= result.lastLocation?.longitude!!
                val latLng = LatLng(latitude, longitude)


                Toast.makeText(requireContext(), "Location update : $latLng", Toast.LENGTH_SHORT).show()

                val params = HashMap<String, Double>()
                params.put("longitude", longitude)
                params.put("latitude", latitude)

                sessionManager= SessionManager(requireContext())
                val call = retrofitInterface!!.findspot("Bearer ${sessionManager.fetchAuthToken()}",params)

                call.enqueue(object: Callback<ArrayList<Nearest>>{
                    override fun onFailure(call: Call<ArrayList<Nearest>>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Nearest>>,
                        response: Response<ArrayList<Nearest>>
                    ) {
                        if (response.code()==200 && response.body()!=null) {
                            resp = response.body()!!

                            for(i in 0..resp.size-1){
                                arrayListLoc.add(resp[i].location.coordinates)
                            }

                            Toast.makeText(requireContext(),"connected $arrayListLoc", Toast.LENGTH_LONG).show()

                            initAddMarker(mapboxMap)
                            //toasted the token to check if its working.
                        } else if (response.code() == 400) {

                            Toast.makeText(requireContext(), "client error", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })









                if (fragment.mapboxMap != null && result.lastLocation != null) {
                    fragment.mapboxMap.getLocationComponent()
                        .forceLocationUpdate(result.lastLocation)
                }
            }
        }

        override fun onFailure(exception: java.lang.Exception) {
            Log.d("LocationChangeActivity", exception.localizedMessage)
            val fragment: HomeMapFragment? = fragmentWeakReference.get()
            if (fragment != null) {
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }


    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}