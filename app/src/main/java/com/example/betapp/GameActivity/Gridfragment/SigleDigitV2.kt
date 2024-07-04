package com.example.betapp.GameActivity.Gridfragment

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.GridAdapter
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.misc.CustomDialogListener
import com.example.betapp.misc.customDialog
import com.example.betapp.misc.dialogdata
import com.example.betapp.model.BetItem
import com.example.betapp.model.GameDatas
import com.example.betapp.model.WebsiteSettings
import com.example.betapp.model.user
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SigleDigitV2.newInstance] factory method to
 * create an instance of this fragment.
 */
class SigleDigitV2 : Fragment(),BetItemListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        viewModel = ViewModelProvider(this).get(ViewmodelSingledigit::class.java)

    }
    private fun isTimeBetween(currentTime: String, openTime: String, closeTime: String): Boolean {
        try {

            val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTime = Date()

            // Format the current time using the SimpleDateFormat object

            // Format the current time using the SimpleDateFormat object
            val formattedTime = parser.format(currentTime)
            val currentTimeDate = parser.parse(formattedTime)
            Log.d("currentTimeDate",currentTimeDate.toString())
            val openTimeDate = parser.parse(openTime)
            val closeTimeDate = parser.parse(closeTime)
            Log.d("time","$openTime , $closeTime ,$currentTimeDate")
            return currentTimeDate in openTimeDate..closeTimeDate
        }
        catch (e:Exception){
            return false
        }

    }
    private fun getCurrentTime(): String {
        return ""
    }
    lateinit var adapter: GridAdapter
    private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refresh: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var submitButton: MaterialButton
    private lateinit var openRadioButton: RadioButton
    private lateinit var closeRadioButton: RadioButton
    private  var wallet:Double=0.0
    private var opentime:String=""
    private var closetimw:String=""
    private var marketid:String=""
    private var sessionType:String="";
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private val rotationDuration = 500L
    private lateinit var user: user
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE
    private lateinit var viewModel: ViewmodelSingledigit
    private lateinit var view:View;
    private var currenttab=0
    private var startX: Float = 0f
    private var currentSectionPosition: Int = 0
    private val totalSections: Int = 3 // Example total number of sections
    private val SWIPE_THRESHOLD: Float = 50f
    private fun initview() {

        backBtn = view.findViewById(R.id.back_btn)
        toolbarTitle = view.findViewById(R.id.toolbarTitle)
        walletIcon = view.findViewById(R.id.wallet_icon)
        refresh = view.findViewById(R.id.refresh)
        walletBalanceToolbar = view.findViewById(R.id.wallet_balanceToolbar)
        openRadioButton = view.findViewById(R.id.open_rb)
        closeRadioButton = view.findViewById(R.id.close_rb)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sigle_digit_v2, container, false)
        val rv=view.findViewById<RecyclerView>(R.id.rv)
        initview()
        rv.layoutManager=(GridLayoutManager(requireContext(),2))
           val list=viewModel.betList.value

        adapter= GridAdapter(list!!.toMutableList(),requireActivity(),this,rv)
        rv.adapter=adapter
        adapter.single(true)
        adapter.notifyDataSetChanged()
        commonSharedPrefernces= CommonSharedPrefernces(requireActivity())
        user=commonSharedPrefernces.getuser()!!
        marketid= requireActivity().intent.getStringExtra("marketId").toString()
        sessionType=requireActivity().intent.getStringExtra("session").toString()
        openRadioButton.isChecked=true;
        opentime=requireActivity().intent.getStringExtra("starttime").toString()
        closetimw=requireActivity().intent.getStringExtra("endtime").toString()
        ApiCall().apiconfig(object : ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_bet=wh.min_bet.toInt()
                max_bet=wh.max_bet.toInt()
            }

            override fun onFailure(error: String) {

            }

        })
        if(sessionType.equals("close")){
            closeRadioButton.isChecked=true;
            openRadioButton.visibility=View.GONE;
        }
        setwallet()
        setupRotateAnimation()
        backBtn.setOnClickListener {
            requireActivity(). finish()
        }
        refresh.setOnClickListener {
            if (rotateAnimator?.isRunning == true) {
                stopRotateAnimation()
            } else {
                startRotateAnimation()
            }
            setwallet()
        }
        submitButton=view.findViewById<MaterialButton>(R.id.submit)
        submitButton.setOnClickListener {


            submitButton.visibility=View.GONE
            submitdata()
        }
        return view;
    }
    var total_amt=0
    private lateinit var list:ArrayList<BetItem>
    private fun submitdata() {
        //  Toast.makeText(requireActivity(),"$sessionType",Toast.LENGTH_SHORT).show()
        list= viewModel.betList.value!!
        var checkmin=viewModel.checkmin(min_bet)
        var checkmax=viewModel.checkmax(max_bet)

        if ((sessionType.equals("open")) || (closeRadioButton.isChecked && sessionType.equals("close"))) {

            var check = true
            list.forEach { if (it.amount != 0) check = false }
            if (check) {
                submitButton.visibility=View.VISIBLE
                Toast.makeText(requireActivity(), "Please make some bet", Toast.LENGTH_SHORT)
                    .show()
            } else if (checkmin.isNotEmpty()) {
                submitButton.visibility=View.VISIBLE
                Toast.makeText(requireActivity(), "$checkmin", Toast.LENGTH_SHORT).show()

            } else if (checkmax.isNotEmpty()) {
                submitButton.visibility=View.VISIBLE
                Toast.makeText(requireActivity(), "$checkmax", Toast.LENGTH_SHORT).show()

            } else {
                list.forEach { betItem ->
                    total_amt = total_amt + betItem.amount as Int
                    if(betItem.amount!=0) {
                        Log.d("value_total", "number ${betItem.number} amt ${betItem.amount}")
                    }
                }
                val balance_after = wallet - total_amt;
                val dialogdata = dialogdata(
                    "Single Digit v2",
                    "$total_amt",
                    "$wallet",
                    "$balance_after"
                )
                val customDialog =
                    customDialog(requireActivity(), dialogdata, object : CustomDialogListener {
                        override fun onCancelClicked() {
                            total_amt = 0
                            submitButton.visibility=View.VISIBLE

                        }

                        override fun onConfirmClicked() {
                            if (balance_after < 0) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Insufficient Balance",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (!isTimeBetween(getCurrentTime(), opentime, closetimw)) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Game is closed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                callapi(total_amt)
                            }
                            total_amt = 0
                            visblesubmitbtn()
                        }

                    })
                customDialog.show()

            }
        } else {
            submitButton.visibility=View.VISIBLE
            val message =
                if (openRadioButton.isChecked) "Game run in close session" else "Game run in open session"
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }


    }
    private fun visblesubmitbtn() {
        Handler(Looper.getMainLooper()).postDelayed({
            // Show the button after 10 seconds
            submitButton.visibility=View.VISIBLE
        }, 5000)


    }
    private fun callapi(total_amt: Int) {
        val gameDatas= GameDatas(
            marketId =marketid.toInt(),
            gameId = 37,
            userId = user.id!!.toInt(),
            gameData = convertListToJson(list),
            totalAmount = total_amt.toDouble(),
            transactionType = "debit",
            transactionNarration = "Game Played",
            session = if(openRadioButton.isChecked) "open" else if(closeRadioButton.isChecked)"close" else ""



        )
        val apiCall= ApiCall()
        apiCall.submitgame(
            gameDatas,
            object : ApiResponse {
                override fun onSuccess(jsonObject: JsonObject) {
                    if(!jsonObject.isJsonNull){
                        if(jsonObject.get("status").toString().equals("\"success\""))
                        {
                            Toast.makeText(requireActivity(),"Bet submitted", Toast.LENGTH_SHORT).show()
                            viewModel.populateBetList()
                            adapter.datareset()
                            //  pointsEditText.setText("")
                            setwallet()

                        }
                        else{
                            Toast.makeText(requireActivity(),jsonObject.get("message").toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(requireActivity(),"Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(failure: String) {
                    Toast.makeText(requireActivity(),failure, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
    fun convertListToJson(betItems: List<BetItem>): String {

        val gson = Gson()
        val list2:ArrayList<BetItem> =ArrayList()
        for(item in betItems){
            if(!item.amount.equals(0)){
                list2.add(item)
            }
        }
        Log.d("size,","${betItems.size} | ${list2.size}")
        return gson.toJson(list2)
    }
    private fun setupRotateAnimation() {
        // Create an ObjectAnimator to rotate the ImageView
        rotateAnimator = ObjectAnimator.ofFloat(refresh, "rotation", 0f, 360f)
        rotateAnimator?.duration = rotationDuration
        rotateAnimator?.interpolator = LinearInterpolator()

        // Start the animation
        startRotateAnimation()
    }
    private fun setwallet(){
        val ApiCall= ApiCall()
        ApiCall.walletApi(user!!.id,object : ApiResponse {
            override fun onSuccess(jsonObject: JsonObject) {
                if(jsonObject.get("status").toString().equals("\"success\"")){
                    val b=jsonObject.get("walletBalance").toString().removeSurrounding("\"")
                    walletBalanceToolbar.setText(b)
                    wallet=b.toDouble()
                }
                else
                    walletBalanceToolbar.setText("0")
            }

            override fun onFailure(failure: String) {
                walletBalanceToolbar.setText("0")
            }

        })
    }
    private fun startRotateAnimation() {
        rotateAnimator?.start()
    }

    private fun stopRotateAnimation() {
        rotateAnimator?.cancel()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SigleDigitV2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SigleDigitV2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onUpdateBetItem(position: Int, newValue: BetItem) {
        viewModel.updateBetItemAtPosition(position, newValue)
    }

}