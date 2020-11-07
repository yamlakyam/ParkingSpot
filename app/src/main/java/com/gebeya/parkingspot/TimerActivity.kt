package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        var stoptime:Long=0
        startBtn.setOnClickListener {
            chronom1.base= SystemClock.elapsedRealtime()+stoptime
            chronom1.start()
        }



        done.setOnClickListener {
            stoptime=chronom1.base- SystemClock.elapsedRealtime()
            chronom1.stop()
        }
    }
}