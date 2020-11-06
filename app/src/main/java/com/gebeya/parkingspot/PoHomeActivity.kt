package com.gebeya.parkingspot

import android.R
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_po_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class PoHomeActivity : AppCompatActivity(),PoAdapter.ClickedItem {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager
    private var resp = ArrayList<PoResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.gebeya.parkingspot.R.layout.activity_po_home)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)
        var recyclerView: RecyclerView = rcPo
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL)
        )

        var call = retrofitInterface!!.po("Bearer ${sessionManager.fetchAuthToken()}")

        call.enqueue(object : Callback<ArrayList<PoResponse>> {
            override fun onFailure(call: Call<ArrayList<PoResponse>>, t: Throwable) {
                Toast.makeText(this@PoHomeActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ArrayList<PoResponse>>, response: Response<ArrayList<PoResponse>>) {
                if (response.code() == 200 && response.body() != null) {
                    var resp = response.body()!!
                    recyclerView.adapter = PoAdapter(resp,this@PoHomeActivity)


                } else if (response.code() == 400) {

                    Toast.makeText(this@PoHomeActivity, "client error", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }


    override fun clickedSpot(poResponse: PoResponse) {
        var callback:Callback<ArrayList<PoResponse>>

    }
}