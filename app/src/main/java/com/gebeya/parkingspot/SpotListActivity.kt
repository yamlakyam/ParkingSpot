package com.gebeya.parkingspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_spot_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SpotListActivity : AppCompatActivity(), NearestAdapter.ClickedItem {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    var spotData: ArrayList<Nearest1> = ArrayList()
    private var resp = ArrayList<Slot>()
    private var arrayListslot =ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_list)

        val params = HashMap<String, Double>()
        params.put("longitude", 38.7)
        params.put("latitude", 8.9)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)
        var recyclerView: RecyclerView = rcSpots
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        var call = retrofitInterface!!.findspot1("Bearer ${sessionManager.fetchAuthToken()}", params)

        call.enqueue(object : Callback<ArrayList<Nearest1>> {
            override fun onFailure(call: Call<ArrayList<Nearest1>>, t: Throwable) {
                Toast.makeText(this@SpotListActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ArrayList<Nearest1>>,
                response: Response<ArrayList<Nearest1>>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    var resp = response.body()!!
                    recyclerView.adapter = NearestAdapter(resp,this@SpotListActivity)

                    // Toast.makeText(requireContext(), resp[0].floor, Toast.LENGTH_SHORT).show()
/*
                    for(i in 0..resp.size-1){
                        arrayListLoc.add(resp[i].location.coordinates)
                    }*/

                } else if (response.code() == 400) {

                    Toast.makeText(this@SpotListActivity, "client error", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    override fun clickedSpot(nearest: Nearest1) {
        var intent= Intent(this, BookActivity::class.java)
        intent.putExtra("data",nearest)
        var parkingId=nearest._id
        Toast.makeText(this, parkingId,Toast.LENGTH_LONG).show()

        val map = HashMap<String, String>()
        map.put("parkingLotId", parkingId)
        var call = retrofitInterface!!.getslots("Bearer ${sessionManager.fetchAuthToken()}", map)
        call.enqueue(object : Callback<ArrayList<Slot>> {
            override fun onFailure(call: Call<ArrayList<Slot>>, t: Throwable) {
                Toast.makeText(this@SpotListActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<Slot>>, response: Response<ArrayList<Slot>>) {
                if (response.code()==200 && response.body()!=null) {
                    resp = response.body()!!

                    for(i in 0..resp.size-1){
                        arrayListslot.add(resp[i]._id)
                    }
                    Toast.makeText(this@SpotListActivity,"connected $arrayListslot", Toast.LENGTH_LONG).show()

                    //toasted the token to check if its working.
                } else if (response.code() == 400) {

                    Toast.makeText(this@SpotListActivity, "client error", Toast.LENGTH_LONG).show()
                }

            }

        })


        startActivity(intent)
    }
}