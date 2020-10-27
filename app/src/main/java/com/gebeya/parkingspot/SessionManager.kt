package com.gebeya.parkingspot

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var pref:SharedPreferences= context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
        //const val USER_INFO =
    }

    //fun to save auth token
    fun saveAuthToken(token:String){
        val editor = pref.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()

    }
    //fun to fetch auth token

    fun fetchAuthToken():String?{
        return pref.getString(USER_TOKEN,null)
    }
    /*
    fun saveEmail(email:User){

    }

     */
}