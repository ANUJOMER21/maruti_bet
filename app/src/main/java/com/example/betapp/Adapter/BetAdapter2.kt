package com.example.betapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.R
import com.example.betapp.model.BetItem

class  BetAdapter2(val betList: MutableList<BetItem>, val context: Context) :

    RecyclerView.Adapter<BetAdapter2.BetViewHolder>() {
    fun get_list():MutableList<BetItem>{
        return betList
    }

    inner class BetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textBet: TextView = itemView.findViewById(R.id.textBet)
        val textAmount: TextView = itemView.findViewById(R.id.textAmount)
        val imageDelete: LinearLayout = itemView.findViewById(R.id.imageDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bet, parent, false)
        return BetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BetViewHolder, position: Int) {
        val currentItem = betList[position]

            holder.textBet.text=currentItem.number

        holder.textAmount.text = currentItem.amount.toString()

        holder.itemView.setOnClickListener {

            showConfirmationDialog(position)
        }
    }
    private fun showConfirmationDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Remove Bet")
        alertDialogBuilder.setMessage("Are you sure you want to remove this bet?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Remove the bet
            betList.removeAt(position)
            notifyDataSetChanged()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // Do nothing if the user clicks "No"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    override fun getItemCount() = betList.size
}