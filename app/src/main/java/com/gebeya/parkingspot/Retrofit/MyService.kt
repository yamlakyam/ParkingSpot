
package com.gebeya.parkingspot.Retrofit

import com.gebeya.parkingspot.*
import retrofit2.Call
import retrofit2.http.*

interface MyService {

    @POST("auth/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResponse>

    @POST("auth/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void>


    @POST("main/getnearest")
    fun findspot(@Header("Authorization") token: String,
                 //@Query("longitude") longitude:Double,
                 //@Query("latitude") latitude:Double): Call<ArrayList<Nearest>
                 //@QueryMap params:Map<String, Double> ):Call<ArrayList<Nearest>>
                 @Body map:HashMap<String, Double> ):Call<ArrayList<Nearest>>

    @POST("main/getnearest")
    fun findspot1(@Header("Authorization") token: String,
                 //@Query("longitude") longitude:Double,
                 //@Query("latitude") latitude:Double): Call<ArrayList<Nearest>
                 //@QueryMap params:Map<String, Double> ):Call<ArrayList<Nearest>>
                 @Body map:HashMap<String, Double> ):Call<ArrayList<Nearest1>>

    @POST("main/vehicle/getavailable")
    fun getslots(@Header("Authorization") token: String,
                        @Body map:HashMap<String, String>):Call<ArrayList<Slot>>



}