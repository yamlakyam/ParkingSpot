package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_po_clear_stack.*
import kotlinx.android.synthetic.main.activity_po_stack_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PoClearStackActivity : AppCompatActivity(),PoStackAdapter.ClickedItem{
    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_po_clear_stack)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)
        var recyclerView: RecyclerView = rcPoClearstack
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        var call = retrofitInterface!!.getstack("Bearer ${sessionManager.fetchAuthToken()}")
        call.enqueue(object : Callback<ArrayList<Stack>> {
            override fun onFailure(call: Call<ArrayList<Stack>>, t: Throwable) {
                Toast.makeText(this@PoClearStackActivity, "Network Failure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<Stack>>, response: Response<ArrayList<Stack>>) {
                if(response.code()==200){
                    var resp = response.body()!!
                    recyclerView.adapter = PoStackAdapter(resp,this@PoClearStackActivity)
                }
                else if(response.code()==400){
                    Toast.makeText(this@PoClearStackActivity, "client error", Toast.LENGTH_LONG).show()
                }
            }
        }

        )
    }

    override fun clickedSpot(stackk: Stack) {
        var parkingLotId=stackk._id

        val map=HashMap<String,String>()
        map.put("parkingLotId",parkingLotId)

        var call = retrofitInterface!!.clearStack("Bearer ${sessionManager.fetchAuthToken()}",map)
        call.enqueue(object : Callback<Stack> {
            override fun onFailure(call: Call<Stack>, t: Throwable) {
                Toast.makeText(this@PoClearStackActivity, "Network Failure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Stack>, response: Response<Stack>) {
                if(response.code()==200){
                   Toast.makeText(this@PoClearStackActivity,"${response.body()!!.slots} slots are cleared", Toast.LENGTH_LONG).show()

                }
                else if(response.code()==400){
                    Toast.makeText(this@PoClearStackActivity, "client error", Toast.LENGTH_LONG).show()
                }
            }
        }

        )


    }
}