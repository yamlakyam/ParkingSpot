package com.gebeya.parkingspot

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SessionManager(context: Context) {
    val pref: SharedPreferences= context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
    //val pref= getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
        const val USER_ROLE = "user_role"
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
        return pref.getString(USER_TOKEN, " ")
    }

    fun saveEmail(email:String){
        val editor=pref.edit()
        editor.putString(USER_EMAIL,email)
        editor.apply()
    }

    fun fetchEmail():String?{
        return pref.getString(USER_EMAIL,null)
    }

    fun savePassword(password:String){
        val editor=pref.edit()
        editor.putString(USER_PASSWORD,password)
        editor.apply()
    }

    fun fetchPassword():String?{
        return pref.getString(USER_PASSWORD,null)
    }

    fun saveRoles(roles:String){
        val editor=pref.edit()
        editor.putString(USER_ROLE,roles)
        editor.apply()

    }
    fun fetchRole():String?{
        return pref.getString(USER_ROLE,null)
    }

}