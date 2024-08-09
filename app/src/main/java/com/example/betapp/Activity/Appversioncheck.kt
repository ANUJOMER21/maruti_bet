package com.example.betapp.Activity

import RetrofitInstance
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.betapp.R

import com.google.android.material.button.MaterialButton


class Appversioncheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appversioncheck)
        val download:MaterialButton=findViewById(R.id.download)
        download.setOnClickListener {
            val url=RetrofitInstance.Base_url+"marutibets.apk"
            Log.d("url",url)
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)

        }
    }
}