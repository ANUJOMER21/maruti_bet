package com.example.betapp.Adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.example.betapp.GameActivity.Gridfragment.BetItemListener
import com.example.betapp.R
import com.example.betapp.model.BetItem

class GridAdapter2( context: Context, var betList: List<BetItem>, val listener: BetItemListener,):
ArrayAdapter<BetItem?>(context,0,betList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.griditem, parent, false)

        }
        val textBet: TextView =listitemView!!.findViewById(R.id.textBet);

        val amtbet: EditText =listitemView!!.findViewById(R.id.textAmount);
        val betItem=betList.get(position)
        val number = betItem.number.toInt()
        textBet.text = String.format("%02d", number)
        amtbet.setText(if(betItem.amount.toString().equals("0") || betItem.amount.toString().isEmpty() ) "" else betItem.amount.toString())
        amtbet.addTextChangedListener(object : TextWatcher {

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
                    Log.d("updateditem",updatedItem.toString())
                    listener.onUpdateBetItem(betItem.number.toInt(), updatedItem)
                }
                else{
                    val updatedItem = BetItem(newAmt, betItem.number)
                    listener.onUpdateBetItem(betItem.number.toInt(),updatedItem)
                }




            }
        })
        return listitemView;
    }
}