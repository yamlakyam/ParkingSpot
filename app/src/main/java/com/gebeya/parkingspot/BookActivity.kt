package com.gebeya.parkingspot

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_book.mapView
import kotlinx.android.synthetic.main.fragment_home_map.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList



class BookActivity : AppCompatActivity(), OnMapReadyCallback {
    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    private var resp = ArrayList<Slot>()
    var listOfslot = mutableListOf<String>()
    private lateinit var slotSlected:Any
    private lateinit var mapboxMap: MapboxMap

    private val  SOURCE_ID = "SOURCE_ID"
    private val  ICON_ID = "ICON_ID"
    private val  LAYER_ID = "LAYER_ID"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_book)
        mapView?.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)

        val intent = intent
        var parkingId=intent.getStringExtra("parkingLotId")
        var parkingSlotId=intent.getStringExtra("slotId")

        bookBtn.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("parkingLotId", parkingId)
            map.put("parkingSlotId",parkingSlotId)

            Toast.makeText(this,"$parkingId $parkingSlotId", Toast.LENGTH_LONG).show()

            val call = retrofitInterface!!.park("Bearer ${sessionManager.fetchAuthToken()}", map)
            call.enqueue(object :Callback<Park>{

                override fun onFailure(call: Call<Park>, t: Throwable) {

                    Toast.makeText(this@BookActivity, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<Park>, response: Response<Park>) {

                    if (response.code() == 200) {
                        var resp1=response.body()!!._id
                        sessionManager.saveTicket(response.body()!!._id)
                        Toast.makeText(this@BookActivity," slot $parkingSlotId reservation successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@BookActivity,TimerActivity::class.java))
                    }
                    else if (response.code() == 400) {
                        Toast.makeText(this@BookActivity ,"Client Error ", Toast.LENGTH_LONG).show()
                    }

                }
            })
        }

        cancelbtn.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("ticketId", sessionManager.fetchTicket()!!)

            var call = retrofitInterface!!.exit("Bearer ${sessionManager.fetchAuthToken()}", map)
            call.enqueue(object :Callback<Exit>{
                override fun onFailure(call: Call<Exit>, t: Throwable) {
                    Toast.makeText(this@BookActivity, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<Exit>, response: Response<Exit>
                ) {
                    var respo=response.body()!!
                    Toast.makeText(this@BookActivity, "Reservation Cancelled", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@BookActivity,slotListActivity::class.java))
                }
            })
        }

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val symbolLayers = ArrayList<Feature>()
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(38.0, 9.9)))

        mapboxMap.setStyle(
            Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(ICON_ID, BitmapUtils
                    .getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.mapbox_marker_icon_default))!!)
                .withSource(GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(symbolLayers)))
                .withLayer(
                    SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                            PropertyFactory.iconImage(ICON_ID),
                            PropertyFactory.iconSize(1.0f),
                            PropertyFactory.iconAllowOverlap(true),
                            PropertyFactory.iconIgnorePlacement(true)
                        ))
        )
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