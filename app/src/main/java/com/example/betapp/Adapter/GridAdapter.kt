package com.example.betapp.Adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.GameActivity.Gridfragment.BetItemListener
import com.example.betapp.R
import com.example.betapp.model.BetItem

class GridAdapter(val betList: List<BetItem>, val context: Context , private val listener: BetItemListener):RecyclerView.Adapter<GridAdapter.vh>() {

    class vh(itemView: View) :RecyclerView.ViewHolder(itemView){
       val textBet:TextView=itemView.findViewById(R.id.textBet);
       val amtbet:EditText=itemView.findViewById(R.id.textAmount);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAdapter.vh {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.griditem, parent, false)
        return GridAdapter.vh(itemView)
    }

    override fun onBindViewHolder(holder: GridAdapter.vh, position: Int) {
        val betItem=betList.get(position)

        val number = betItem.number.toInt()
        holder.textBet.text = String.format("%02d", number)

        holder.amtbet.setText(if(betItem.amount.toString().equals("0") || betItem.amount.toString().isEmpty() ) "" else betItem.amount.toString())
        holder.amtbet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the BetItem with the new amount
                val newAmount = s.toString().toIntOrNull() ?: 0
                val updatedItem = BetItem(newAmount, betItem.number)
                listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
            }
        })
    }

    override fun getItemCount(): Int {
     return betList.size
    }
}


