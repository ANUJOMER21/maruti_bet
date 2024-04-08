package com.example.betapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.R

class rate_chartAdapter(private val context: Context, private val game_amt: List<com.example.betapp.model.Game>):
RecyclerView.Adapter<rate_chartAdapter.VH>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rate_chartAdapter.VH {
        val view =
            LayoutInflater.from(context).inflate(R.layout.rate_view, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: rate_chartAdapter.VH, position: Int) {
val  game=game_amt.get(position)
        holder.name.setText("${game.gameName} 10: ")
        holder.multiply.setText("${game.multiplyAmount}")

       }

    override fun getItemCount(): Int {
      return game_amt.size
    }
    class VH(itemView: View):RecyclerView.ViewHolder(itemView){
        val name:TextView=itemView.findViewById(R.id.game_name)
        val multiply:TextView=itemView.findViewById(R.id.game_multiply)

    }


}