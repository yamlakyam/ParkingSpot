package com.gebeya.parkingspot.Retrofit

import com.gebeya.parkingspot.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private var retrofit: Retrofit? = null
    private  var sessionManage: SessionManager?=null

    //val BASE_URL = "http://192.168.1.5:3000"
    val BASE_URL = "https://parking-spot-finder-api.herokuapp.com"

    fun getInstance(): Retrofit {
        return if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(OkHttpClient.Builder().addInterceptor{chain ->
                //  val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${sessionManage?.fetchAuthToken()}").build()
                  //  chain.proceed((request))
                //}.build())
                .build()
            retrofit!!
        } else {
            retrofit!!
        }


    }



}