package com.gebeya.parkingspot

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.regex.Matcher
import java.util.regex.Pattern


class CreateAccountActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private var retrofitInterface: MyService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        retrofitInterface = retrofit!!.create(MyService::class.java)

        create_account_btn.setOnClickListener {

            if (name_reg.text.toString().trim().isEmpty()) {
                name_reg.error = "Name Required"
                name_reg.requestFocus()
                return@setOnClickListener
            }
            if (phone_reg.text.toString().trim().isEmpty()) {
                phone_reg.error = "Phone Number Required"
                phone_reg.requestFocus()
                return@setOnClickListener
            }
            if(plate_number.text.toString().trim().isEmpty()){
            plate_number.error="Plate Number Required"
            plate_number.requestFocus()
            return@setOnClickListener
            }

            if(plate_number.text.toString().length!=5){
                plate_number.error="Plate Number must be five digits"
                plate_number.requestFocus()
                return@setOnClickListener
            }
            if (email_reg.text.toString().trim().isEmpty()) {
                email_reg.error = "Email Required"
                email_reg.requestFocus()
                return@setOnClickListener
            }
            if (password_reg.text.toString().trim().isEmpty()) {
                password_reg.error = "Password Required"
                password_reg.requestFocus()
                return@setOnClickListener
            }
            if (confirm_password_reg.text.toString().trim().isEmpty()) {
                confirm_password_reg.error = "Password Required"
                confirm_password_reg.requestFocus()
                return@setOnClickListener
            }

            if (password_reg.text.toString().trim().length <8 && !(isValidPassword(password_reg.text.toString().trim()))){
                password_reg.error="Password must be minuimum length of 8 charcters"
                password_reg.requestFocus()
                return@setOnClickListener
            }
            val map = HashMap<String, String>()

            map["name"] = name_reg.text.toString()
            map["email"] = email_reg.text.toString()
            map["password"] = password_reg.text.toString()
            map["phone_no"] = phone_reg.text.toString()
            map["plate_number"]=plate_number.text.toString()


            if (map["password"] == confirm_password_reg.text.toString()) {
                progressB_CA.isVisible=true
                val call: Call<Void> = retrofitInterface!!.executeSignup(map)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 200) {

                            Toast.makeText(this@CreateAccountActivity, "Signed Up successfully", Toast.LENGTH_LONG).show()

                            var intent= Intent(this@CreateAccountActivity,MainActivity::class.java)
                            startActivity(intent)
                        } else if (response.code() == 400) {

                            Toast.makeText(this@CreateAccountActivity, "Already Registered ", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(
                            this@CreateAccountActivity,"Network Failure",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            } else {
                Toast.makeText(this, "Password must match", Toast.LENGTH_LONG).show()
            }

        }
        have_account_link.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidPassword(pass: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(pass)

        return matcher.matches()
    }
}