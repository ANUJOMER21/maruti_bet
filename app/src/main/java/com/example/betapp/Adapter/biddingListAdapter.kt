package com.example.betapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.R
import com.example.betapp.model.BetItem
import java.text.NumberFormat
import java.util.Locale

class biddingListAdapter(private val context: Context, private val list: List<BetItem> ,private val singledigit:Boolean,private val fullsangam:Boolean,private val HalfSangam:Boolean): RecyclerView.Adapter<biddingListAdapter.Vh>() {
    fun formatNumberWithZero(number: Int): String {
        val numberFormat = NumberFormat.getInstance(Locale.US)
        val formattedNumber = numberFormat.format(number)
        return formattedNumber.padStart(2, '0') // Assuming a maximum of 9 digits
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): biddingListAdapter.Vh {
        val view =
            LayoutInflater.from(context).inflate(R.layout.bid_detail_view, parent, false)
        return Vh(view)
    }

    override fun onBindViewHolder(holder: biddingListAdapter.Vh, position: Int) {


        val map=list.get(position)
if(fullsangam){
    holder.bid.text = (map.number)
}
        else if(singledigit){
    holder.bid.text = (map.number)
        }
        else if(HalfSangam){
    holder.bid.text = (map.number)
        }
        else{
    holder.bid.text = formatNumberWithZero(map.number.toInt())
        }

        holder.amount.text = (map.amount.toString())
    }

    override fun getItemCount(): Int {
     return list.size
    }
    class Vh(itemView: View):RecyclerView.ViewHolder(itemView){
        val amount:TextView=itemView.findViewById(R.id.amount)
        val bid:TextView=itemView.findViewById(R.id.bet_number)
    }
}