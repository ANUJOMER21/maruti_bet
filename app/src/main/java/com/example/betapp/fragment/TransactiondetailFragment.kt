package com.example.betapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.TransactionAdapter
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.transaction
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.Transaction

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactiondetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactiondetailFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_transactiondetail, container, false)
        val RecyclerView:RecyclerView=view.findViewById(R.id.bidHistoryRecycler);
        val textview:ProgressBar=view.findViewById(R.id.txt);
        RecyclerView.layoutManager=LinearLayoutManager(activity)
        val ApiCall =ApiCall()
        val commonSharedPrefernces=CommonSharedPrefernces(activity as Context)
        val userid=commonSharedPrefernces.getuser()!!.id
        ApiCall.transhist(userid,object :transaction{
            override fun onSuccess(games: List<Transaction>) {
                RecyclerView.visibility=View.VISIBLE
                textview.visibility=View.GONE
                val TransactionAdapter=TransactionAdapter(
                  requireActivity(),
                    games
                )
                RecyclerView.adapter=TransactionAdapter
                TransactionAdapter.notifyDataSetChanged()
            }

            override fun onFailure(failure: String) {
                textview.visibility=View.VISIBLE
                RecyclerView.visibility=View.GONE
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
         * @return A new instance of fragment TransactiondetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactiondetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}