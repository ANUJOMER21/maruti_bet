package com.example.betapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.biddingListAdapter
import com.example.betapp.R
import com.example.betapp.model.BetItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class bettingdata : AppCompatActivity() {
    private lateinit var RecyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bettingdata)
        RecyclerView=findViewById(R.id.rv_bid)
        RecyclerView.layoutManager=LinearLayoutManager(this)
        val receivedListOfMaps = intent.getStringExtra("listOfMaps")
        val gamename=intent.getStringExtra("gamename")
        var single=false
        var full=false
        var  half=false

        val gson=Gson()
        val data: List<BetItem> = gson.fromJson(receivedListOfMaps, object : TypeToken<List<BetItem>>() {}.type)
if(gamename.equals("Single Digit",true)){
    single=true
}
        if(gamename.equals("Full Sangam",true)){
            full=true
        }
        if(gamename.equals("Half Sangam",true)){
            half=true
        }
        val adapter=biddingListAdapter(this,data!!,single,full,half)
        RecyclerView.adapter=adapter
        adapter.notifyDataSetChanged()

    }
}