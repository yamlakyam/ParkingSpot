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
import kotlinx.android.synthetic.main.activity_po_stack_list.*
import kotlinx.android.synthetic.main.activity_spot_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PoStackListActivity : AppCompatActivity(),PoStackAdapter.ClickedItem {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_po_stack_list)


        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)
        var recyclerView: RecyclerView = rcPoParkstack
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        var call = retrofitInterface!!.getstack("Bearer ${sessionManager.fetchAuthToken()}")

        call.enqueue(object : Callback<ArrayList<Stack>> {
            override fun onFailure(call: Call<ArrayList<Stack>>, t: Throwable) {
                Toast.makeText(this@PoStackListActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<Stack>>, response: Response<ArrayList<Stack>>) {
                if(response.code()==200){
                    var resp = response.body()!!
                    recyclerView.adapter = PoStackAdapter(resp,this@PoStackListActivity)
                }
                else if(response.code()==400){
                    Toast.makeText(this@PoStackListActivity, "client error", Toast.LENGTH_LONG).show()
                }
            }
        }

            )



    }

    override fun clickedSpot(stackk: Stack) {
        var callback:Callback<ArrayList<Slot>>

        var intent= Intent(this, PoSlotListActivity::class.java)

        var parkingLotId=stackk._id

        intent.putExtra("parkingLotId",parkingLotId)//trying to send the id first then do the ntk call on the other activity

        startActivity(intent)
    }
}