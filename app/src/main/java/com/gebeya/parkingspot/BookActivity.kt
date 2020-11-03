package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val intent = intent

            var near = intent.getSerializableExtra("data") as Nearest1
            val id: String = near._id
            val company: String = near.company
            val status: String = near.full_status.toString()
            price.text=near.price.toString()+" birr per minute"

    }
}