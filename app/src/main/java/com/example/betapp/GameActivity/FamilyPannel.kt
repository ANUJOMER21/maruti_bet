package com.example.betapp.GameActivity

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.misc.CustomDialogListener
import com.example.betapp.misc.GameData
import com.example.betapp.misc.customDialog
import com.example.betapp.misc.dialogdata
import com.example.betapp.model.BetItem
import com.example.betapp.model.GameDatas
import com.example.betapp.model.WebsiteSettings
import com.example.betapp.model.user
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.Locale

class FamilyPannel : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refresh: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var currentDate: TextView
    private lateinit var openRadioButton: RadioButton
    private lateinit var closeRadioButton: RadioButton
    private lateinit var digitsSpinner: Spinner
    private lateinit var pointsEditText: EditText
    private lateinit var betPattyTextView: TextView
    private lateinit var submitButton: MaterialButton
    private  var wallet:Double=0.0
    private var marketid:String=""
    private var opentime:String=""
    private var closetimw:String=""
    private fun isTimeBetween(currentTime: String, openTime: String, closeTime: String): Boolean {
        try {
            val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTimeDate = parser.parse(currentTime)
            val openTimeDate = parser.parse(openTime)
            val closeTimeDate = parser.parse(closeTime)
            Log.d("time","$openTime , $closeTime ,$currentTime")
            return currentTimeDate in openTimeDate..closeTimeDate
        }
        catch (e:Exception){
            return false
        }

    }
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }

    private var sessionType:String="";
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private val rotationDuration = 500L
    private lateinit var user: user
    lateinit var valueList:MutableList<Int>
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_pannel)
        initview()
        commonSharedPrefernces= CommonSharedPrefernces(this)
        user=commonSharedPrefernces.getuser()!!
        marketid= intent.getStringExtra("marketId").toString()
        sessionType=intent.getStringExtra("session").toString()
        opentime=intent.getStringExtra("starttime").toString()
        closetimw=intent.getStringExtra("endtime").toString()
        ApiCall().apiconfig(object :ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_bet=wh.min_bet.toInt()
                max_bet=wh.max_bet.toInt()
            }

            override fun onFailure(error: String) {
                TODO("Not yet implemented")
            }

        })

        openRadioButton.isChecked=true
        if(sessionType.equals("close")){
            closeRadioButton.isChecked=true;
            openRadioButton.visibility=View.GONE;
        }
        setwallet()
        setupRotateAnimation()
        backBtn.setOnClickListener {
            finish()
        }
        refresh.setOnClickListener {
            if (rotateAnimator?.isRunning == true) {
                stopRotateAnimation()
            } else {
                startRotateAnimation()
            }
            setwallet()
        }
        val cd=getCurrentDate()
        currentDate.setText(cd)
        setupspinner()
        submitButton.setOnClickListener {
            submitdata()
        }
    }
    var total_amt=0
    private lateinit var list:MutableList<BetItem>
    private fun submitdata() {
        if (( sessionType.equals("open")) || (closeRadioButton.isChecked && sessionType.equals(
                "close"
            ))
        ) {

            list = ArrayList()
            if (pointsEditText.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter points", Toast.LENGTH_SHORT).show()
            } else if (valueList.isEmpty()) {
                Toast.makeText(this, "Please select bet", Toast.LENGTH_SHORT).show()

            } else if(!isTimeBetween(getCurrentTime(),opentime,closetimw)){
                Toast.makeText(applicationContext,"Game is closed",Toast.LENGTH_SHORT).show()
            }
            else {
                total_amt = valueList.size * (pointsEditText.text.toString().toInt())
                for (value in valueList) {
                    list.add(BetItem(pointsEditText.text.toString().toInt(), value.toString()))

                }
                val balance_after = wallet - total_amt;
                val dialogdata = dialogdata(
                    "Family Pannel",
                    "$total_amt",
                    "$wallet",
                    "$balance_after"
                )
                val customDialog = customDialog(this, dialogdata, object : CustomDialogListener {
                    override fun onCancelClicked() {
                        total_amt = 0
                    }

                    override fun onConfirmClicked() {
                        if (balance_after < 0) {
                            Toast.makeText(
                                this@FamilyPannel,
                                "Insufficient Balance",
                                Toast.LENGTH_SHORT
                            ).show()
                        }   else if(pointsEditText.text.toString().toInt()>=max_bet){
                            Toast.makeText(applicationContext,"Maximum Bet amount is $max_bet",Toast.LENGTH_SHORT).show()

                        }
                        else if(pointsEditText.text.toString().toInt()<=min_bet){
                            Toast.makeText(applicationContext,"Minimum Bet amount is $min_bet",Toast.LENGTH_SHORT).show()

                        }
                        else {
                            callapi(total_amt)
                        }
                        total_amt = 0
                    }

                })
                customDialog.show()

            }
        }
        else
        {
            val message=if(openRadioButton.isChecked) "Game run in close session" else "Game run in open session"
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }




    }

    private fun callapi(total_amt: Int) {
        val gameDatas= GameDatas(
            marketId =marketid.toInt(),
            gameId = 29,
            userId = user.id!!.toInt(),
            gameData = convertListToJson(list),
            totalAmount = total_amt.toDouble(),
            transactionType = "debit",
            transactionNarration = "Game Played"
            ,
            session = if(openRadioButton.isChecked) "open" else if(closeRadioButton.isChecked)"close" else ""


        )
        Log.d("gameData",gameDatas.gameData)
        val apiCall= ApiCall()
        apiCall.submitgame(
            gameDatas,
            object : ApiResponse {
                override fun onSuccess(jsonObject: JsonObject) {
                    if(!jsonObject.isJsonNull){
                        if(jsonObject.get("status").toString().equals("\"success\""))
                        {
                            Toast.makeText(this@FamilyPannel,"Bet submitted", Toast.LENGTH_SHORT).show()
                            pointsEditText.setText("")
                        setwallet()}
                        else{
                            Toast.makeText(this@FamilyPannel,jsonObject.get("message").toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@FamilyPannel,"Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(failure: String) {
                    Toast.makeText(this@FamilyPannel,failure, Toast.LENGTH_SHORT).show()
                }

            }
        )

    }

    fun convertListToJson(betItems: List<BetItem>): String {
        val gson = Gson()
        return gson.toJson(betItems)
    }

    private fun setupspinner() {
        val GameData= GameData()
        valueList=ArrayList()
        val keyList=GameData.familyPannelitem().sorted()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, keyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        digitsSpinner.adapter=adapter
        digitsSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = keyList[position]
                valueList= GameData.familyPannelList(selectedItem.toString()).toMutableList()
                valueList.sortedBy { it }
                var patty:String=""
                for (item in valueList){
                    patty += "${String.format("%03d", item)}, "
                }
                betPattyTextView.text=patty
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                betPattyTextView.text=""
            }

        }

    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
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

    private fun initview() {
        backBtn = findViewById(R.id.back_btn)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        walletIcon = findViewById(R.id.wallet_icon)
        refresh = findViewById(R.id.refresh)
        walletBalanceToolbar = findViewById(R.id.wallet_balanceToolbar)
        currentDate = findViewById(R.id.current_date)
        openRadioButton = findViewById(R.id.open_rb)
        closeRadioButton = findViewById(R.id.close_rb)
        digitsSpinner = findViewById(R.id.digits)
        pointsEditText = findViewById(R.id.points)
        betPattyTextView = findViewById(R.id.bet_patty)
        submitButton = findViewById(R.id.submit)

    }

}