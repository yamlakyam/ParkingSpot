
package com.gebeya.parkingspot.Retrofit

import com.gebeya.parkingspot.Location
import com.gebeya.parkingspot.LoginResponse
import com.gebeya.parkingspot.NearestResponse
import retrofit2.Call
import retrofit2.http.*

interface MyService {

    @POST("auth/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResponse>

    @POST("auth/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void>


    @GET("main/getnearest")
    fun findspot(@Header("Authorization") token: String,
                 @Query("longitude") longitude:Double,
                 @Query("latitude") latitude:Double): Call<List<Location>>
                 //@QueryMap params:Map<String, Double> ):Call<List<Location>>


    /*

    @GET("main/getnearest")
    fun listSpots((@Header("Authorization") token: String, @Query())


     */


}