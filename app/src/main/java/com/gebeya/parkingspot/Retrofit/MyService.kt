
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
                 @Body map:HashMap<String, Double> ):Call<ArrayList<Nearest>>

    @POST("main/getnearest")
    fun findspot1(@Header("Authorization") token: String,
                 @Body map:HashMap<String, Double> ):Call<ArrayList<Nearest1>>

    @POST("main/vehicle/getavailable")
    fun getslots(@Header("Authorization") token: String,
                        @Body map:HashMap<String, String>):Call<ArrayList<Slot>>

    @POST("main/vehicle/park")
    fun park(@Header("Authorization") token: String,
             @Body map:HashMap<String, String>):Call<Park>

    @POST("main/vehicle/exitByTicket")
    fun exit(@Header("Authorization") token: String,
             @Body map:HashMap<String, String>):Call<Exit>

    @POST("main/exitByPlate")
    fun poCancel(@Header("Authorization") token: String,
                 @Body map: HashMap<String, String>):Call<PoResponse>

    @POST("main/getstacks")
    fun getstack(@Header("Authorization") token: String ):Call<ArrayList<Stack>>

    @POST("main/getallslots")
    fun getslotsPo(@Header("Authorization") token: String,
                 @Body map:HashMap<String, String>):Call<ArrayList<Slot>>

    @POST("main/vehicle/park")
    fun poPark(@Header("Authorization") token: String,
               @Body map:HashMap<String, String>):Call<Park>

    @POST("main/clearStack")
    fun clearStack(@Header("Authorization") token: String,
               @Body map:HashMap<String, String>):Call<Stack>

    @POST("main/getactivetickets")
    fun getTicket(@Header("Authorization") token: String):Call<ArrayList<Ticket>>

}