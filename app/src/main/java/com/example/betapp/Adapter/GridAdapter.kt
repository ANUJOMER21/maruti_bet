package com.example.betapp.Adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.GameActivity.Gridfragment.BetItemListener
import com.example.betapp.R
import com.example.betapp.model.BetItem


class GridAdapter(
    val betList: MutableList<BetItem>,
    val context: Context,
    private val listener: BetItemListener,
    private val recyclerView: RecyclerView
):RecyclerView.Adapter<GridAdapter.vh>() {
    private var isScrolling = false
public fun datareset(){
   for (i in 0..<betList.size){
       betList.get(i).amount=0
   }
    notifyDataSetChanged()
}
/*    inner class MyCustomEditTextListener : TextWatcher {
        private var position = 0
        val betItem=betList.get(position)
        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            // no op
        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            var newAmt = 0
            if (s.toString().isNotEmpty()) {
                newAmt = s.toString().toIntOrNull() ?: 0
                val updatedItem = BetItem(newAmt, betItem.number)
                Log.d("updateditem",updatedItem.toString())
                listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
            }
            else{
                val updatedItem = BetItem(newAmt, betItem.number)
                listener.onUpdateBetItem(betItem.number.toInt(),updatedItem)
            }
        }
    }*/



   inner class vh(itemView: View) :RecyclerView.ViewHolder(itemView){


       val textBet:TextView=itemView.findViewById(R.id.textBet);

       val amtbet:EditText=itemView.findViewById(R.id.textAmount);
        init {

           //     val betItem = betList.get(position)

            }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAdapter.vh {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.griditem, parent, false)
        return vh(itemView)
    }

    override fun getItemViewType(position: Int)=position
    override fun getItemId(position: Int)=position.toLong()
    override fun onBindViewHolder( holder: vh, position: Int) {
        holder.setIsRecyclable(false)
        val betItem=betList.get(position)
         var et=holder.amtbet;

        val number = betItem.number.toInt()
        holder.textBet.text = String.format("%02d", number)

        et.setText(if(betItem.amount.toString().equals("0") || betItem.amount.toString().isEmpty() ) "" else betItem.amount.toString())
        et.doAfterTextChanged { s ->
            Log.d("aftertextchanged", s.toString())
            var newAmt = 0
            if (s.toString().isNotEmpty()) {
                newAmt = s.toString().toIntOrNull() ?: 0
                val updatedItem = BetItem(newAmt, betItem.number)
                Log.d("updateditem", updatedItem.toString())
                listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
                updateinbetlist(updatedItem)
            } else {
                val updatedItem = BetItem(newAmt, betItem.number)
                listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
                updateinbetlist(updatedItem)
            }

        }
        /*runnable?.let { handler.removeCallbacks(it) }

        // Schedule a new callback after the delay
        runnable = Runnable {
            // Update the BetItem with the new amount
            var newAmt = 0
            if (holder.amtbet.toString().isNotEmpty()) {
                newAmt = holder.amtbet.toString().toIntOrNull() ?: 0
            }

            val updatedItem = BetItem(newAmt, betItem.number)
            listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
        }
        handler.postDelayed(runnable!!, DELAY)*/

   /*     et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the BetItem with the new amount



                // Schedule a new callback after the delay

                    // Update the BetItem with the new amount





            }
        })*/

     /*   var newAmt = 0
        if (et.text.toString().isNotEmpty()) {
            newAmt = et.text.toString().toIntOrNull() ?: 0
            val updatedItem = BetItem(newAmt, betItem.number)
            listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
        }
        else{
            val updatedItem = BetItem(newAmt, betItem.number)
            listener.onUpdateBetItem(betItem.number.toInt(),updatedItem)
        }*/


    }

    private fun updateinbetlist(betItem: BetItem) {
        var index = betList.indexOfFirst { it.number == betItem.number }
        if (index != -1) {

            betList[index]=betItem
       //     notifyItemChanged(index) // Notify RecyclerView about the change
        }
    }


    override fun getItemCount(): Int {
     return betList.size
    }
}


