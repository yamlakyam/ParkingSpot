
package com.gebeya.parkingspot.Retrofit


import com.gebeya.parkingspot.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyService {

    @POST("auth/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResponse>

    @POST("auth/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void>

}