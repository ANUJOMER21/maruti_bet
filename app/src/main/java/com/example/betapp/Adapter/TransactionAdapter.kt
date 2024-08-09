package com.example.betapp.Adapter

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.R

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betapp.Activity.TransactionBetHistory
import com.example.betapp.model.BetItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale


class TransactionAdapter(val context: Context,val list:List<com.example.betapp.model.Transaction>):
    RecyclerView.Adapter<TransactionAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.Vh {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_view, parent, false)
        return Vh(view)
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
    override fun onBindViewHolder(holder: TransactionAdapter.Vh, position: Int) {
              val Transaction=list.get(position)
        holder.status.setText(Transaction.status)
        holder.amount.setText(Transaction.amount.toDouble().toInt().toString())
        holder.narration.setText(Transaction.narration)
        holder.date.setText(formatDate(Transaction.time))
        when(Transaction.type){
            "debit"->holder.crdr.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.top_right_svgrepo_com))
            "credit"->holder.crdr.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.bottom_left_svgrepo_com))

        }
        holder.curret_bal.setText(Transaction.current_ba)
        if(Transaction.gameSubmission.marketId==null){
            holder.ll4.visibility=View.GONE
        }
        else{
            holder.ll4.visibility=View.VISIBLE
            holder.marketname.setText("${Transaction.gameSubmission.marketname} ")
            val gname:String?= if( Transaction.gameSubmission.gamename!=null)Transaction.gameSubmission.gamename else ""
            holder.gamename.setText(
              "$gname (${Transaction.gameSubmission.session})"
            )


           val listhm= parseJsonToBetList(
                Transaction.gameSubmission.gameData
            )
            holder.rv.layoutManager= LinearLayoutManager(context)
            var single=false
            var full=false
            var  half=false
            if(gname.equals("Single Digit",true)){
                single=true
            }
            if(gname.equals("Full Sangam",true)){
                full=true
            }
            if(gname.equals("Half Sangam",true)){
                half=true
            }
            val adapter=biddingListAdapter(context, listhm,single,full,half)
            holder.rv.adapter=adapter
            adapter.notifyDataSetChanged()
        }


    }
    fun formatDate(inputDate: String): String {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = dateFormat.parse(inputDate)
        return SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).format(date)
    }


    override fun getItemCount(): Int {
   return list.size
    }
    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {

          var ll1: RelativeLayout
          var status: TextView
          var date: TextView
          var rl2: RelativeLayout
          var tv: TextView
          var crdr: ImageView
          var amount: TextView
          var narration: TextView
          var  curret_bal:TextView
          var ll4:LinearLayout
          var gamename:TextView
          var marketname:TextView
          val rv:RecyclerView

        init {
            // Find views by ID
            ll4=itemView.findViewById(R.id.ll4)
            gamename=itemView.findViewById(R.id.gamename)
            marketname=itemView.findViewById(R.id.marketname)
            curret_bal=itemView.findViewById(R.id.current_bal)
            ll1 = itemView.findViewById(R.id.ll1)
            status = itemView.findViewById(R.id.status)
            date = itemView.findViewById(R.id.date)
            rl2 = itemView.findViewById(R.id.rl2)
            tv = itemView.findViewById(R.id.tv)
            crdr = itemView.findViewById(R.id.crdr)
            amount = itemView.findViewById(R.id.amount)
            narration = itemView.findViewById(R.id.narration)
            rv=itemView.findViewById(R.id.rv_amt)
        }
    }

}