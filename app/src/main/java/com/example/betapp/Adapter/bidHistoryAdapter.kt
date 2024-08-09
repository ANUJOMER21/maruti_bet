package com.example.betapp.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Activity.bettingdata
import com.example.betapp.R
import com.example.betapp.model.BetItem
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class bidHistoryAdapter(private val context: Context,private  val bidList: List<com.example.betapp.model.UserGameSubmission>): RecyclerView.Adapter<bidHistoryAdapter.Vh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bidHistoryAdapter.Vh {
        val view =
            LayoutInflater.from(context).inflate(R.layout.biddinghistory_view, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: bidHistoryAdapter.Vh, position: Int) {
        val userGameSubmission=bidList.get(position)
        holder.amountTextView.setText(userGameSubmission.totalAmount)
        holder.gameNameTextView.setText("${userGameSubmission.gameName} Session: ${userGameSubmission.session}")
        holder.marketNameTextView.setText(userGameSubmission.marketname)
        var list_hm=parseJsonToBetList(userGameSubmission.gameData)
        val gamename=userGameSubmission.gameName
        Log.d("list_hm",list_hm.toString())
       /* holder.bidCardView.setOnClickListener {
            val intent=Intent(context,bettingdata::class.java)
            val Gson =Gson()
            val data=Gson.toJson(list_hm)
            intent.putExtra("gamename",gamename)
            intent.putExtra("listOfMaps", data)
            context.startActivity(intent)
        }*/
        holder.rv.layoutManager=LinearLayoutManager(context)
        var single=false
        var full=false
        var  half=false
        if(gamename.equals("Single Digit",true)){
            single=true
        }
        if(gamename.equals("Full Sangam",true)){
            full=true
        }
        if(gamename.equals("Half Sangam",true)){
            half=true
        }
        val adapter=biddingListAdapter(context,list_hm,single,full,half)
        holder.rv.adapter=adapter
        adapter.notifyDataSetChanged()

    }

    fun parseJsonToBetList(jsonString: String): List<BetItem> {
        val gson = Gson()
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val betDataList: List<Map<String, Any>> = gson.fromJson(jsonString, type)

        return betDataList.map {
            BetItem(
                amount = it["amount"] as Double,  // Assuming "amount" is always present and has a valid Double value
                number = String.format("%02d", it["number"]) // Assuming "number" is always present and has a valid String value
            )
        }
    }
    override fun getItemCount(): Int {
        return bidList.size
    }
    class Vh(itemView: View):RecyclerView.ViewHolder(itemView){
        val bidCardView: MaterialCardView = itemView.findViewById(R.id.bidcv)
        val marketNameTextView: TextView = itemView.findViewById(R.id.market_name)
        val gameNameTextView: TextView = itemView.findViewById(R.id.game_name)
        val amountTextView: TextView = itemView.findViewById(R.id.amount)
        val rv:RecyclerView=itemView.findViewById(R.id.rv_amt);

         init{



         }
    }
}