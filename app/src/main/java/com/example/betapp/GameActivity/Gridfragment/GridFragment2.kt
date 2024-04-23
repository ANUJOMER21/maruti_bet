package com.example.betapp.GameActivity.Gridfragment

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [GridFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridFragment2 : Fragment() , BetItemListener{
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)

        }
        viewModel = ViewModelProvider(this).get(ViewmodelGrid2::class.java)
    }
    lateinit var adapter: GridAdapter
   private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refresh: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var openRadioButton: RadioButton
    private lateinit var closeRadioButton: RadioButton
    private lateinit var submitButton: MaterialButton
    private  var wallet:Double=0.0
    private var opentime:String=""
    private var closetimw:String=""
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

    private var marketid:String=""
    private var sessionType:String="";
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null

    private val rotationDuration = 500L
    private lateinit var user: user
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE
    private lateinit var viewModel: ViewmodelGrid2
    private lateinit var view:View;
    private var currenttab=0
    private var startX: Float = 0f
    private var currentSectionPosition: Int = 0
    private val totalSections: Int = 3 // Example total number of sections
    private val SWIPE_THRESHOLD: Float = 50f
    private fun handleSwipe(startX: Float, endX: Float) {
        val deltaX = endX - startX
        if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
            if (deltaX > 0) {
                // Right swipe
              //  Toast.makeText(requireActivity(),"$currenttab",Toast.LENGTH_SHORT).show()
                if(currenttab<=9&&currenttab>0){
                    currenttab--
                    tabLayout.getTabAt(currenttab)?.select()

                }
            } else {
                // Left swipe
             //   Toast.makeText(requireActivity(),"$currenttab",Toast.LENGTH_SHORT).show()
                if(currenttab>=0&&currenttab<9) {
                    currenttab++
                    tabLayout.getTabAt(currenttab)?.select()

                }
            }
        }
    }
    lateinit var tabLayout:TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_grid2, container, false)
        val rv=view.findViewById<RecyclerView>(R.id.rv)
        initview()
        rv.layoutManager=(GridLayoutManager(requireContext(),2))
       tabLayout = view.findViewById<TabLayout>(R.id.tablayout)
        viewModel.updateFrom_to(0,9)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Update ViewModel from/to values based on the selected tab
                /*    when(tab!!.id){
                        R.id.zero->viewModel.updateFrom_to(0,9)
                        R.id.one->viewModel.updateFrom_to(10,19)
                        R.id.two->viewModel.updateFrom_to(20,29)
                        R.id.three->viewModel.updateFrom_to(30,39)
                        R.id.four->viewModel.updateFrom_to(40,49)
                        R.id.five->viewModel.updateFrom_to(50,59)
                        R.id.six->viewModel.updateFrom_to(60,69)
                        R.id.seven->viewModel.updateFrom_to(70,79)
                        R.id.eight->viewModel.updateFrom_to(80,89)
                        R.id.nine->viewModel.updateFrom_to(90,99)
                    }*/
                Log.d("position",tab!!.position.toString())
                val t=tab.text.toString().toInt()
                currenttab=t;
                viewModel.updateFrom_to(t , (tab.position + 1) * 10 - 1)


            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        viewModel.fromtopair.observe(viewLifecycleOwner, Observer {
                pair ->
            // Update UI with the new value of from
            //val betList = viewModel.getBetListInRange()
            val list=viewModel.betList.value
            val to= pair.second
            val from=pair.first
            Log.d("fromvalue_fr","$from || $to")
            val betList:ArrayList<BetItem> =ArrayList()
    /*        for(num in list!!){
                val f=num.number.get(0).toInt()-'0'.toInt();

                Log.d("first_num",f.toString())
                if(f.equals(from)){
                    betList.add(num)
                }
            }*/
            for ( i in 0..list!!.size-1){
                val f=list[i]
                val num=sumOfDigits(f.number.toInt())%10
                if(num==from){
                    betList.add(f)
                }
            }
            Log.d("fromvalue_fr",betList.size.toString())
            adapter=GridAdapter(betList, requireActivity(), this,rv)
            rv.adapter=adapter
            adapter.notifyDataSetChanged()
        })
        rv.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    false
                }
                MotionEvent.ACTION_UP -> {
                    val endX = event.x
                    handleSwipe(startX, endX)
                    false
                }
                else -> false
            }
        }
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
     return  view
    }
    private fun getCurrentTime(): String {
        return ""
    }
    var total_amt=0
    private fun sumOfDigits(i: Int): Int {
        var num = i
        var sum = 0
        while (num != 0) {
            sum += num % 10
            num /= 10
        }
        return sum
    }
    private lateinit var list:ArrayList<BetItem>
    private fun submitdata() {
        //  Toast.makeText(requireActivity(),"$sessionType",Toast.LENGTH_SHORT).show()

            list = viewModel.betList.value!!
            if ((sessionType.equals("open")) || (closeRadioButton.isChecked && sessionType.equals("close"))) {

                var check = true
                var checkmin = viewModel.checkmin(min_bet)
                var checkmax = viewModel.checkmax(max_bet)
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
                    }
                    val balance_after = wallet - total_amt;
                    val dialogdata = dialogdata(
                        "Double Patti v2",
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
                                submitButton.visibility=View.VISIBLE
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
    private fun callapi(total_amt: Int) {
        val gameDatas= GameDatas(
            marketId =marketid.toInt(),
            gameId = 21,
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
                            adapter.notifyDataSetChanged()
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
    override fun onUpdateBetItem(position: Int, newValue: BetItem) {
        viewModel.updateBetItemAtPosition(position, newValue)
    }
    private fun initview() {

       backBtn = view.findViewById(R.id.back_btn)
        toolbarTitle = view.findViewById(R.id.toolbarTitle)
        walletIcon = view.findViewById(R.id.wallet_icon)
        refresh = view.findViewById(R.id.refresh)
        walletBalanceToolbar = view.findViewById(R.id.wallet_balanceToolbar)
        openRadioButton = view.findViewById(R.id.open_rb)
        closeRadioButton = view.findViewById(R.id.close_rb)


    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GridFragment2.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): GridFragment2 {
            val fragment = GridFragment2()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}