package com.gebeya.parkingspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_po_slot_list.*
import kotlinx.android.synthetic.main.activity_slot_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.HashMap

class PoSlotListActivity : AppCompatActivity(), PoSlotAdapter.ClickedItem {
    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    private var resp = ArrayList<Slot>()
    private var freeresp = ArrayList<Slot>()
    var listOfslot = mutableListOf<String>()
    lateinit var parkingLotId: String
    lateinit var platenumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_po_slot_list)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)

        var recyclerView: RecyclerView = rcPoParkSlot
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        val intent = intent
        parkingLotId = intent.getStringExtra("parkingLotId")//from PoStackListActivity
        platenumber = intent.getStringExtra("plate_number")

        val map = HashMap<String, String>()
        map.put("parkingLotId", parkingLotId)
        var call = retrofitInterface!!.getslots("Bearer ${sessionManager.fetchAuthToken()}", map)

        call.enqueue(object : Callback<ArrayList<Slot>> {
            override fun onFailure(call: Call<ArrayList<Slot>>, t: Throwable) {
                Toast.makeText(this@PoSlotListActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(
                call: Call<ArrayList<Slot>>,
                response: Response<ArrayList<Slot>>
            ) {

                resp = response.body()!!

                for (i in 0..resp.size - 1) {
                    listOfslot.add(resp[i]._id.filter { resp[i].status.statusName[0] == "FREE" })
                }
                recyclerView.adapter = PoSlotAdapter(resp, this@PoSlotListActivity)

            }
        })
    }

    override fun clickedSpot(slot: Slot) {
        var slotID = slot._id
        val intent = intent


        val map = HashMap<String, String>()
        map.put("parkingLotId", parkingLotId)
        map.put("parkingSlotId", slotID)
        map.put("plate_number", platenumber)
        //Toast.makeText(this,"$parkingLotId $slotID,$platenumber", Toast.LENGTH_LONG).show()
        val call = retrofitInterface!!.poPark("Bearer ${sessionManager.fetchAuthToken()}", map)

        call.enqueue(object : Callback<Park> {
            override fun onFailure(call: Call<Park>, t: Throwable) {
                Toast.makeText(this@PoSlotListActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<Park>, response: Response<Park>) {
                if (response.code() == 200) {
                    var resp1 = response.body()!!
                    sessionManager.saveTicket(response.body()!!._id)
                    Toast.makeText(
                        this@PoSlotListActivity,
                        " slot reserved to ${resp1.plate_number} successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (response.code() == 400) {
                    Toast.makeText(this@PoSlotListActivity, "Client Error ", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

    }
}