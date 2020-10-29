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
            //Toast.makeText(this,sessionManager.fetchAuthToken(),Toast.LENGTH_SHORT).show()
            var token:String?
            token= sessionManager.fetchAuthToken()

            //Toast.makeText(this,sessionManager.fetchAuthToken(),Toast.LENGTH_SHORT).show()

            //val params = HashMap<String, Double>()
            //params.put("longitude", 37.8)
            //params.put("latitude", 66.3)
            val call = retrofitInterface.findspot("Bearer $token",38.7,8.94)
            //val call = retrofitInterface.findspot(params)
            call.enqueue(object: Callback<List<Location>>{
                override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                    Toast.makeText(this@Nearby, "t.message", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<Location>>,
                    response: Response<List<Location>>
                ) {
                    /*for(i in response.body()!!.listIterator()){
                        Toast.makeText(requireContext(), "${response.body()}",Toast.LENGTH_LONG).show()
                    }

                     */
                    if (response.code()==200 && response.body()!=null) {

                        Toast.makeText(this@Nearby,"connected ${response.body()}", Toast.LENGTH_LONG).show()
                        //toasted the token to check if its working.

                        val intent = Intent(this@Nearby, HomeActivity::class.java)
                        startActivity(intent)

                    } else if (response.code() == 400) {

                        Toast.makeText(this@Nearby, "client error", Toast.LENGTH_LONG)
                            .show()
                    }

                }

            })


        }

        }
    }






