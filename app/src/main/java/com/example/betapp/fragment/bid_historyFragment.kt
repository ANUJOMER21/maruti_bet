package com.example.betapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.bidHistoryAdapter
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.bid
import com.example.betapp.misc.CommonSharedPrefernces

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [bid_historyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class bid_historyFragment : Fragment() {
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
private lateinit var recyclerView: RecyclerView
private lateinit var notrans:ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_bid_history, container, false)
        recyclerView=view.findViewById(R.id.bidHistoryRecycler)
        notrans=view.findViewById(R.id.txt)
        recyclerView.layoutManager=LinearLayoutManager(activity)
        val commonsharedPref:CommonSharedPrefernces= CommonSharedPrefernces(activity as Context)
        val apiCall=ApiCall()
        val uid=commonsharedPref.getuser()!!.id
        apiCall.bidHist(uid,object :bid{
            override fun onSuccess(games: List<com.example.betapp.model.UserGameSubmission>) {
                 notrans.visibility=View.GONE
                recyclerView.visibility=View.VISIBLE
                val bidHistoryAdapter=bidHistoryAdapter(activity as Context, games)
                recyclerView.adapter=bidHistoryAdapter
                bidHistoryAdapter.notifyDataSetChanged()
            }

            override fun onFailure(failure: String) {
             notrans.visibility=View.VISIBLE
                recyclerView.visibility=View.GONE
            }

        })
        return view
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment bid_historyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            bid_historyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}