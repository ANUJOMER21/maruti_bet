package com.example.betapp.misc

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.betapp.model.bankdetail
import com.example.betapp.model.user
import com.google.gson.Gson

class CommonSharedPrefernces(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val Gson=Gson()
    fun savebank(bankdetail: bankdetail){
        val json=Gson.toJson(bankdetail)
        with(sharedPreferences.edit()){
            putString("bank",json)
            apply()
        }
    }
    fun getbank():bankdetail?{
        val json=sharedPreferences.getString("bank","")
       return if (json.equals(""))
                  null
              else
                  Gson.fromJson(json,bankdetail::class.java)
    }
    fun saveuser(user: user){
        Log.d("login","user_saved")
        val json=Gson.toJson(user
        )
         with(sharedPreferences.edit()){
             putString("user",json)
             apply()
         }
    }
    fun getuser(): user? {
        val json=sharedPreferences.getString("user","")
        if(json.equals("")){
       return null
        }
        else  return Gson.fromJson(json,user::class.java)
    }
    fun logout() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}