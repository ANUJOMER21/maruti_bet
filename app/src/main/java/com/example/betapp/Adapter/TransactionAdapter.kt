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
import com.example.betapp.Activity.TransactionBetHistory
import com.google.gson.Gson
import java.util.Locale


class TransactionAdapter(val context: Context,val list:List<com.example.betapp.model.Transaction>):
    RecyclerView.Adapter<TransactionAdapter.Vh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.Vh {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_view, parent, false)
        return Vh(view)
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

            holder.ll1.setOnClickListener{
                if(Transaction.gameSubmission.marketId!=null){
                    val intent:Intent= Intent(context,TransactionBetHistory::class.java)
                    val gson:Gson=Gson()
                    val data=gson.toJson(Transaction)
                    intent.putExtra("data",data)
                    intent.putExtra("gname",gname)
                    context.startActivity(intent)
                }
            }
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
        }
    }

}