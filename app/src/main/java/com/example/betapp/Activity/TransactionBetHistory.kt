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

class TransactionBetHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_bet_history)
        val data=intent.getStringExtra("data")
        val gname=intent.getStringExtra("gname")
        val Transaction: com.example.betapp.model.Transaction =Gson().fromJson(data,
            com.example.betapp.model.Transaction::class.java)
        val list:List<BetItem> =parseJsonToBetList(
            Transaction.gameSubmission.gameData
        )
        val rv=findViewById<RecyclerView>(R.id.betrv)
        val full:Boolean=gname.equals("Full Sangam",true)
        val single:Boolean=gname.equals("Single Digit",true)
        val half:Boolean=gname.equals("Half Sangam",true)

        val adapter:biddingListAdapter= biddingListAdapter(this,list,single,full,half)
        rv.layoutManager=LinearLayoutManager(this)
        rv.adapter=adapter
        adapter.notifyDataSetChanged()


    }
    fun parseJsonToBetList(jsonString: String): List<BetItem> {
        val gson = Gson()
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val betDataList: List<Map<String, Any>> = gson.fromJson(jsonString, type)

        return betDataList.map {
            BetItem(
                amount = it["amount"] as Double,  // Assuming "amount" is always present and has a valid Double value
                number = it["number"].toString() // Assuming "number" is always present and has a valid String value
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}