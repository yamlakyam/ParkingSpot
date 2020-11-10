package com.gebeya.parkingspot

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_po_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.round
import kotlin.math.roundToLong


class PoHomeActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.gebeya.parkingspot.R.layout.activity_po_home)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)

        poName.text="Parking Officer: ${sessionManager.fetchName()}"

        exitBtn.setOnClickListener {
            if (plateNo.text.toString().trim().isEmpty()) {
                plateNo.error = "Plate Number Required"
                plateNo.requestFocus()
                return@setOnClickListener
            }
            if (plateNo.text.toString().trim().length <5){
                plateNo.error="Plate Number is a minimum of 5 characters"
                plateNo.requestFocus()
                return@setOnClickListener
            }

            val map = HashMap<String, String>()
            map["plate_no"] = plateNo.text.toString()

            var call = retrofitInterface!!.poCancel("Bearer ${sessionManager.fetchAuthToken()}",map)

            call.enqueue(object : Callback<PoResponse> {
                override fun onFailure(call: Call<PoResponse>, t: Throwable) {
                    Toast.makeText(this@PoHomeActivity, "Network Failure", Toast.LENGTH_LONG).show()

                }

                @SuppressLint("SimpleDateFormat")
                override fun onResponse(call: Call<PoResponse>, response: Response<PoResponse>) {
                    if (response.code() == 200 ) {
                        var resp = response.body()!!
                        Toast.makeText(this@PoHomeActivity,"cleared",Toast.LENGTH_LONG).show()
                        var intent= Intent(this@PoHomeActivity, DetailInfoActivity::class.java)
                        intent.putExtra("slotId",resp.slot_id)
                        intent.putExtra("plateNumber",resp.plate_number)
                        //intent.putExtra("parkAt",resp.park_at)
                        //intent.putExtra("exitAt",resp.exit_at)
                       // intent.putExtra("total",resp.total_price)
                        //round(this * multiplier) / multiplier
                      // intent.putExtra("total",round(resp.total_price * 10.0) / 10.0)
                       intent.putExtra("total",round(resp.total_price * 100) / 100)
                        //intent.putExtra("total",BigDecimal(resp.total_price).setScale(2, RoundingMode.HALF_EVEN))
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val date1 = dateFormat.parse(resp.park_at)
                        val date2 = dateFormat.parse(resp.exit_at)
                        val formatter = SimpleDateFormat("HH:mm:ss") //If you need time just put specific format for time like ‘HH:mm:ss’
                        val parkat = formatter.format(date1)
                        val exitat = formatter.format(date2)

                        intent.putExtra("parkAt",parkat)
                        intent.putExtra("exitAt",exitat)


                        startActivity(intent)


                    } else if (response.code() == 400) {

                        Toast.makeText(this@PoHomeActivity, "Wrong Value", Toast.LENGTH_LONG)
                            .show()
                    }
                    else if(response.code()==404){
                        Toast.makeText(this@PoHomeActivity, "No Ticket found for plate number ${plateNo.text.toString()}", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        parkBtn.setOnClickListener {
            if (plateNo.text.toString().trim().isEmpty()) {
                plateNo.error = "Plate Number Required"
                plateNo.requestFocus()
                return@setOnClickListener
            }
            if (plateNo.text.toString().trim().length <5){
                plateNo.error="Plate Number is a minimum of 5 characters"
                plateNo.requestFocus()
                return@setOnClickListener
            }

            val intent1=Intent(this, PoSlotListActivity::class.java)
            var plate_number=plateNo.text.toString()

            val intent = Intent(this, PoStackListActivity::class.java)
            intent.putExtra("plate_number",plate_number)
            startActivity(intent)

        }

        clearBtn.setOnClickListener {
            startActivity(Intent(this, PoClearStackActivity::class.java))
        }

        ticketBtn.setOnClickListener {
            startActivity(Intent(this, TicketListActivity::class.java))
        }

    }

}