package com.example.betapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.rate_chartAdapter
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.api.game
import com.example.betapp.model.Game
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RateChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RateChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var game_chart:RecyclerView
    private lateinit var progress_bar:ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_rate_chart, container, false)
        game_chart=view.findViewById(R.id.rate_chart)
        progress_bar=view.findViewById(R.id.progress_circular);
        game_chart.layoutManager=LinearLayoutManager(activity)
        val apiCall:ApiCall= ApiCall()
  apiCall.showgame(object :game{
      override fun onSuccess(games: List<Game>) {
          if(games!=null) {
              val rateChartadapter: rate_chartAdapter = rate_chartAdapter(
                  activity as Context,
                  games
              )
              progress_bar.visibility=View.GONE
              game_chart.visibility=View.VISIBLE
              game_chart.adapter=rateChartadapter
              rateChartadapter.notifyDataSetChanged()
          }
          else{
              progress_bar.visibility=View.VISIBLE
              game_chart.visibility=View.GONE
              Toast.makeText(activity,"Failed",Toast.LENGTH_SHORT).show()
          }

      }

      override fun onFailure(failure: String) {
          progress_bar.visibility=View.VISIBLE
          game_chart.visibility=View.GONE
         Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
      }

  })

        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RateChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RateChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}