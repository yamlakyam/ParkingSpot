package com.gebeya.parkingspot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_timer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class TimerActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        retrofitInterface = retrofit!!.create(MyService::class.java)
        sessionManager = SessionManager(this)
        var stoptime: Long = 0
        var chronometer: Chronometer

        chronometer = findViewById(R.id.chronom1)
        startBtn.setOnClickListener(object : View.OnClickListener {

            var isWorking = false

            override fun onClick(v: View) {
                if (!isWorking) {
                    chronometer.start()
                    isWorking = true
                } else {
                    chronometer.stop()
                    isWorking = false
                }

                startBtn.setText(if (isWorking) "Stop Timer" else "Show Timer")

                Toast.makeText(
                    this@TimerActivity, getString(
                        if (isWorking)
                            R.string.working
                        else
                            R.string.stopped
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        cancellBtn.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("ticketId", sessionManager.fetchTicket()!!)

            var call = retrofitInterface!!.exit("Bearer ${sessionManager.fetchAuthToken()}", map)
            call.enqueue(object : Callback<Exit> {
                override fun onFailure(call: Call<Exit>, t: Throwable) {
                    Toast.makeText(this@TimerActivity, t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<Exit>, response: Response<Exit>
                ) {
                    if(response.code()==200){
                        var respo=response.body()!!
                        Toast.makeText(this@TimerActivity, "Reservation Cancelled", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@TimerActivity,BookActivity::class.java))
                        finish()

                    }
                    else if(response.code()==400){
                        Toast.makeText(this@TimerActivity ,"Client Error ", Toast.LENGTH_LONG).show()

                    }

                }
            })
        }

        val handler = Handler()
        handler.postDelayed({ cancellBtn.isEnabled=false}, 5000)

    }


}