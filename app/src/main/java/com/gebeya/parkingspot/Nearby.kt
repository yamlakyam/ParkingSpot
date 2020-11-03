package com.gebeya.parkingspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.gebeya.parkingspot.Retrofit.MyService
import com.gebeya.parkingspot.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_nearby.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Nearby : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager


    private var retrofit: Retrofit? = RetrofitClient.getInstance()
    private lateinit var retrofitInterface: MyService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)

        retrofitInterface = retrofit!!.create(MyService::class.java)




        button3.setOnClickListener {
            sessionManager= SessionManager(this)
            var token:String?
            token= sessionManager.fetchAuthToken()
            val map = HashMap<String, Double>()

            map["longitude"] = 37.8
            map["latitude"]=8.9
            val call = retrofitInterface.findspot("Bearer $token",map)


            call.enqueue(object: Callback<ArrayList<Nearest>>{
                override fun onFailure(call: Call<ArrayList<Nearest>>, t: Throwable) {
                    Toast.makeText(this@Nearby, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ArrayList<Nearest>>,
                    response: Response<ArrayList<Nearest>>
                ) { if (response.code()==200 && response.body()!=null) {
                    Toast.makeText(this@Nearby,"connected ", Toast.LENGTH_LONG).show()
                    //toasted the token to check if its working.
                } else if (response.code() == 400) {
                    Toast.makeText(this@Nearby, "client error", Toast.LENGTH_LONG)
                        .show()
                }
                }
            })
        }

    }
}