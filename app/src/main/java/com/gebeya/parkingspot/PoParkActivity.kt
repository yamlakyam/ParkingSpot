package com.gebeya.parkingspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_po_park.*
import kotlinx.android.synthetic.main.activity_timer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.HashMap
import kotlin.math.absoluteValue

class PoParkActivity : AppCompatActivity() {
    var arrayListStack = ArrayList<String>()
    var arrayListSlot = ArrayList<String>()
    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_po_park)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)

        var call = retrofitInterface!!.getstack("Bearer ${sessionManager.fetchAuthToken()}")

        call.enqueue(object : Callback<ArrayList<Stack>> {
            override fun onFailure(call: Call<ArrayList<Stack>>, t: Throwable) {
                Toast.makeText(this@PoParkActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ArrayList<Stack>>,
                response: Response<ArrayList<Stack>>
            ) {

                if (response.code() == 200) {
                    var resp = response.body()!!
                    for (i in 0..resp.size - 1) {
                        arrayListStack.add(resp[i]._id)
                    }


                    var adapter1 = ArrayAdapter<String>(
                        this@PoParkActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        arrayListStack
                    )
                    stackSpinner.adapter = adapter1
                    stackSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                var parkingLotId = parent!!.getItemAtPosition(position).toString()

                                val map = HashMap<String, String>()
                                map.put("parkingLotId", parkingLotId)
                                var call = retrofitInterface!!.getslots(
                                    "Bearer ${sessionManager.fetchAuthToken()}",
                                    map
                                )

                                call.enqueue(object : Callback<ArrayList<Slot>> {
                                    override fun onFailure(
                                        call: Call<ArrayList<Slot>>,
                                        t: Throwable
                                    ) {
                                        Toast.makeText(
                                            this@PoParkActivity,
                                            t.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    override fun onResponse(
                                        call: Call<ArrayList<Slot>>,
                                        response: Response<ArrayList<Slot>>
                                    ) {

                                        var resp1 = response.body()!!

                                        for (i in 0..(resp1.size - 1).absoluteValue) {
                                            arrayListSlot.add(resp[i-1]._id)
                                        }


                                        var adapter1 = ArrayAdapter<String>(
                                            this@PoParkActivity,
                                            R.layout.support_simple_spinner_dropdown_item,
                                            arrayListSlot
                                        )
                                        slotSpinner.adapter = adapter1
                                        slotSpinner.onItemSelectedListener =
                                            object : AdapterView.OnItemSelectedListener {
                                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                                }

                                                override fun onItemSelected(
                                                    parent: AdapterView<*>?,
                                                    view: View?,
                                                    position: Int,
                                                    id: Long
                                                ) {
                                                    var parkingSlotId =
                                                        parent!!.getItemAtPosition(position)
                                                            .toString()
                                                    parkPoBtn.setOnClickListener {
                                                        val map1 = HashMap<String, String>()
                                                        map1.put("parkingLotId", parkingLotId)
                                                        map1.put("parkingSlotId", parkingSlotId)

                                                        val call = retrofitInterface!!.park(
                                                            "Bearer ${sessionManager.fetchAuthToken()}",
                                                            map
                                                        )

                                                        call.enqueue(object : Callback<Park> {
                                                            override fun onFailure(
                                                                call: Call<Park>,
                                                                t: Throwable
                                                            ) {
                                                                Toast.makeText(
                                                                    this@PoParkActivity,
                                                                    t.message,
                                                                    Toast.LENGTH_LONG
                                                                ).show()

                                                            }

                                                            override fun onResponse(
                                                                call: Call<Park>,
                                                                response: Response<Park>
                                                            ) {
                                                                if (response.code() == 200) {
                                                                    Toast.makeText(
                                                                        this@PoParkActivity,
                                                                        "Parking Successful",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                } else if (response.code() == 400) {
                                                                    Toast.makeText(
                                                                        this@PoParkActivity,
                                                                        "Client Error",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                }
                                                            }

                                                        })
                                                    }


                                                }

                                            }

                                    }
                                })


                            }
                        }


                } else if (response.code() == 400) {

                }

            }

        })


    }
}