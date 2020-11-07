package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_slot_list.*
import kotlinx.android.synthetic.main.activity_spot_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.HashMap

class slotListActivity : AppCompatActivity(), slotListAdapter.ClickedItem {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    private var resp = ArrayList<Slot>()
    var listOfslot = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot_list)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)

        var recyclerView: RecyclerView = rcPo
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        val intent = intent
        var parkingId=intent.getStringExtra("id")

        val map = HashMap<String, String>()
        map.put("parkingLotId", parkingId)
        var call = retrofitInterface!!.getslots("Bearer ${sessionManager.fetchAuthToken()}", map)

        call.enqueue(object : Callback<ArrayList<Slot>> {
            override fun onFailure(call: Call<ArrayList<Slot>>, t: Throwable) {
                Toast.makeText(this@slotListActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<Slot>>, response: Response<ArrayList<Slot>>) {
                resp = response.body()!!

                for(i in 0..resp.size-1){
                    listOfslot.add(resp[i]._id)
                }
                recyclerView.adapter = slotListAdapter(resp,this@slotListActivity)
            }
        })
    }

    override fun clickedSpot(slots: Slot) {
        TODO("Not yet implemented")
    }
}