package com.example.betapp.Adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.GameActivity.Gridfragment.BetItemListener
import com.example.betapp.GameActivity.Gridfragment.ViewmodelGrid1
import com.example.betapp.R
import com.example.betapp.model.BetItem

class GridAdapter(
    val betList: List<BetItem>,
    val context: Context,
    private val listener: BetItemListener,
    private val recyclerView: RecyclerView
):RecyclerView.Adapter<GridAdapter.vh>() {
    private var isScrolling = false

    init {
        // Attach a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Set scrolling flag to true while scrolling
                isScrolling = true
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Set scrolling flag to false when scrolling stops
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrolling = false
                }
            }
        })
    }

   inner class vh(itemView: View) :RecyclerView.ViewHolder(itemView){
       val textBet:TextView=itemView.findViewById(R.id.textBet);

       val amtbet:EditText=itemView.findViewById(R.id.textAmount);


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAdapter.vh {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.griditem, parent, false)
        return vh(itemView)
    }
    private val DELAY: Long = 500 // milliseconds
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    override fun getItemViewType(position: Int)=position
    override fun getItemId(position: Int)=position.toLong()
    override fun onBindViewHolder( holder: vh, position: Int) {
        holder.setIsRecyclable(false)
        val betItem=betList.get(position)
         var et=holder.amtbet;
      /*  if (isScrolling) {
            et.clearFocus()
            et.isFocusable = false
        } else {
            et.isFocusable = true
        }*/
        val number = betItem.number.toInt()
        holder.textBet.text = String.format("%02d", number)

        et.setText(if(betItem.amount.toString().equals("0") || betItem.amount.toString().isEmpty() ) "" else betItem.amount.toString())
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

        et.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the BetItem with the new amount



                // Schedule a new callback after the delay

                    // Update the BetItem with the new amount
                    var newAmt = 0
                    if (s.toString().isNotEmpty()) {
                        newAmt = s.toString().toIntOrNull() ?: 0
                        val updatedItem = BetItem(newAmt, betItem.number)
                        listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
                    }
                else{
                        val updatedItem = BetItem(newAmt, betItem.number)
                    listener.onUpdateBetItem(betItem.number.toInt(),updatedItem)
                    }




            }
        })

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

    override fun getItemCount(): Int {
     return betList.size
    }
}


