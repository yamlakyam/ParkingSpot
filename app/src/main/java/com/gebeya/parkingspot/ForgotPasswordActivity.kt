package com.gebeya.parkingspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_main.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        send_link_btn.setOnClickListener {
            if (email_send_link.text.toString().isEmpty()) {
                email_send_link.error = "Email Required"
                email_send_link.requestFocus()
                return@setOnClickListener
            }
        }
    }
}