package com.example.betapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.betapp.Adapter.MarketAdapter
import com.example.betapp.R
import com.example.betapp.api.ApiCall

import com.example.betapp.misc.ToolbarChangeListener

import com.example.betapp.misc.getCurrentTimeFromInternet

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smarteist.autoimageslider.SliderView
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var toolbarChangeListener: ToolbarChangeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarChangeListener) {
            toolbarChangeListener = context
        } else {
            throw ClassCastException("$context must implement ToolbarChangeListener")
        }
    }

    override fun onResume() {
        super.onResume()
      //  toolbarChangeListener?.onToolbarTitleChange("Home")
    }
private  lateinit var ApiCall:ApiCall
    private lateinit var rv:RecyclerView

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
         ApiCall =ApiCall()
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        rv =view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(activity)
        val sliderView:SliderView=view.findViewById(R.id.slider)
       progressBar=view.findViewById(R.id.progress)
      rv.visibility=View.GONE
        progressBar.visibility=View.VISIBLE
        ApiCall.Sliderlist(object : ApiCall.SLiderCallback {
            override fun onSlierReceived(sliders: List<String>) {
                val sliderImage: List<String> =sliders
                if (sliderImage.size > 0) {
                    val adapter = com.example.betapp.Adapter.SliderAdapter(sliderImage)

                    // below method is used to set auto cycle direction in left to
                    // right direction you can change according to requirement.
                    sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR)

                    // below method is used to
                    // setadapter to sliderview.
                    sliderView.setSliderAdapter(adapter)

                    // below method is use to set
                    // scroll time in seconds.
                    sliderView.setScrollTimeInSec(5)

                    // to set it scrollable automatically
                    // we use below method.
                    sliderView.setAutoCycle(true)

                    // to start autocycle below method is used.
                    sliderView.startAutoCycle()
                }
            }

            override fun onFailure(error: String) {
                if (error != null) {
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
                }
            }

        })


       /* topimage.setOnClickListener {
            val intent=Intent(activity as Context, GameGrid::class.java)
            intent.putExtra("marketId",12)

            intent.putExtra("markerName",1)
            startActivity( intent)
        }*/
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv.layoutManager = layoutManager
        getmarket()
        val Whatsapp:FloatingActionButton=view.findViewById(R.id.whatsapp_number)
        var whats=""
        val youtube:FloatingActionButton=view.findViewById(R.id.youtube)
        var you=""

        ApiCall.websiteSetting(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                  whats=wh;
            }

            override fun onFailure(error: String) {
                Toast.makeText(activity,error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
        Whatsapp.setOnClickListener {
            sendMessageToWhatsApp("91"+whats, "Hello")

        }
        ApiCall.youtubesetting(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
             if(wh.isNullOrEmpty())
             {
                 youtube.visibility=View.GONE
             }
                else{
                    youtube.visibility=View.VISIBLE
                 you=wh
             }
            }

            override fun onFailure(error: String) {
                youtube.visibility=View.GONE
            }

        })
        Whatsapp.setOnClickListener {
            sendMessageToWhatsApp("91"+whats, "Hello")

        }
        youtube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(you))
            startActivity(intent)
        }
        val telegram:FloatingActionButton=view.findViewById(R.id.telegram_number)
        var tele=""
        ApiCall.telegram(object :ApiCall.telegramCallback{
            override fun telegram(number: String) {
                if(number.isNotEmpty()){
                    tele=number
                    telegram.visibility=View.VISIBLE
                }
                else{
                    telegram.visibility=View.GONE
                }
            }

            override fun failure(fail: String) {
                   telegram.visibility=View.GONE
            }

        })
        telegram.setOnClickListener {
            try {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                telegramIntent.setData(Uri.parse("http://telegram.me/$tele"))
                startActivity(telegramIntent)
            } catch (e: java.lang.Exception) {
                // show error message
            }
        }


// Begin a FragmentTransaction


        val Deposit:TextView=view.findViewById(R.id.deposit)
        val withdraw:TextView=view.findViewById(R.id.withdraw)
        var auto:Boolean=false;
ApiCall.autodeposit(object :ApiCall.WebseiteSetting{
    override fun onWebsiteSettingReceived(wh: String) {
        if(wh.equals("working"))
            auto=true;
        else
            auto=false;
    }

    override fun onFailure(error: String) {

    }

})
Deposit.setOnClickListener {
    if(auto) {
        replaceFragment(com.example.betapp.fragment.walletFragment2())
    }
    else{
        sendMessageToWhatsApp(whats,"I want to deposit/withdraw manually")
    }
}
        withdraw.setOnClickListener {
            if(auto) {
                replaceFragment(WithdrawRequestFragment())
            }
            else{
                sendMessageToWhatsApp(whats,"I want to deposit/withdraw manually")
            }
        }
val swipeRefreshLayout:SwipeRefreshLayout=view.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            getmarket()
            swipeRefreshLayout.isRefreshing=false
        }
        return view;
    }
    fun parseTimeFromJson(json: String?): String {
        if (json.isNullOrEmpty()) return "Unknown"

        val jsonObject = JSONObject(json)
        return jsonObject.getString("datetime")
    }

/*    private fun getmarket(){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("http://worldtimeapi.org/api/timezone/Asia/Kolkata")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    println("Unexpected code $response")
                    return
                }

                val body = response.body?.string()
                val currentTime = parseTimeFromJson(body)
                ApiCall.getMarkets( object :ApiCall.MarketCallback{

                    override fun onMarketsReceived(markets: List<market>) {
                        val mainMarkerAdapter= MarketAdapter(currentTime,activity!!,markets)
                        rv.adapter=mainMarkerAdapter
                        mainMarkerAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(error: Throwable) {
                        Toast.makeText(activity,error.toString(),Toast.LENGTH_SHORT).show()
                    }

                })
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        // For simplicity, return a placeholder time


    }*/
private fun getmarket() {
getCurrentTimeFromInternet { time->
    ApiCall.getMarkets(object : ApiCall.MarketCallback {
        override fun onMarketsReceived(markets: List<com.example.betapp.model.market>) {
            rv.visibility=View.VISIBLE
            progressBar.visibility=View.GONE

            Log.d("time_ntp", time)
            val mainMarkerAdapter = MarketAdapter( time, requireActivity()!!, markets)
            rv.adapter = mainMarkerAdapter
            mainMarkerAdapter.notifyDataSetChanged()
        }

        override fun onFailure(error: Throwable) {
            rv.visibility=View.GONE
            progressBar.visibility=View.VISIBLE
            Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}


}

/*    private fun getmarket() {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("http://worldtimeapi.org/api/timezone/Asia/Kolkata")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                println("Unexpected code $response")
                return
            }

            val body = response.body
            if (body != null) {
                val responseBody = body.string()
                val currentTime = parseTimeFromJson(responseBody)
                ApiCall.getMarkets(object : ApiCall.MarketCallback {
                    override fun onMarketsReceived(markets: List<market>) {
                        val mainMarkerAdapter = MarketAdapter(currentTime, activity!!, markets)
                        rv.adapter = mainMarkerAdapter
                        mainMarkerAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(error: Throwable) {
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Handle the case when response body is null
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }
    })
}*/

    private fun sendMessageToWhatsApp(phoneNumber: String, message: String?) {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            val url =
                "https://api.whatsapp.com/send?phone=" + "$phoneNumber" + "&text=$message"
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        // Replace the existing fragment with the new fragment
        transaction.replace(R.id.fragment_container, fragment)

        // Optional: Add the transaction to the back stack
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}