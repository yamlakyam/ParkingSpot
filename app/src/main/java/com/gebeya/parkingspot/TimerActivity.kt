package com.gebeya.parkingspot

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
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

                startBtn.setText(if (isWorking) R.string.start else R.string.stop)

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

    }


}