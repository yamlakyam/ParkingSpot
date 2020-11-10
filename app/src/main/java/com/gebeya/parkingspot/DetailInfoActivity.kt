package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_info.*

class DetailInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info)

        val intent = intent
        slotDet.text=intent.getStringExtra("slotId")
        plateNoDet.text=intent.getStringExtra("plateNumber")
        startedDet.text=intent.getStringExtra("parkAt")
        exitTimeDet.text=intent.getStringExtra("exitAt")
        totalPriceDet.text= intent.getDoubleExtra("total", 0.0).toString()+" birr"

    }
}