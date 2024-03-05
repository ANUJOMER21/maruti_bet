package com.example.betapp.GameActivity

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.BetAdapter
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

class RedJodi : AppCompatActivity() {
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
    private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refresh: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var currentDate: TextView
    private lateinit var openRadioButton: RadioButton
    private lateinit var closeRadioButton: RadioButton
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var pointsEditText: EditText
    private lateinit var addBetButton: MaterialButton
    private lateinit var betRecyclerView: RecyclerView
    private lateinit var submitButton: MaterialButton
    private  var wallet:Double=0.0
    private var marketid:String=""
    private var sessionType:String="";
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private lateinit var betAdapter: BetAdapter
    private val rotationDuration = 500L
    private lateinit var user: user
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_jodi)
        initview()
        setdigits()
        ApiCall().apiconfig(object :ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_bet=wh.min_bet.toInt()
                max_bet=wh.max_bet.toInt()
            }

            override fun onFailure(error: String) {
                TODO("Not yet implemented")
            }

        })


        commonSharedPrefernces= CommonSharedPrefernces(this)
        user=commonSharedPrefernces.getuser()!!
        marketid= intent.getStringExtra("marketId").toString()
        sessionType=intent.getStringExtra("session").toString()
        opentime=intent.getStringExtra("starttime").toString()
        closetimw=intent.getStringExtra("endtime").toString()

        openRadioButton.isSelected=true;
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
        betRecyclerView.layoutManager = GridLayoutManager(this,2)
        betAdapter= BetAdapter(mutableListOf(),this)
        betRecyclerView.adapter=betAdapter
        addBetButton.setOnClickListener {
            addBet()
        }
        submitButton.setOnClickListener {
            submitdata()
        }
    }
    var total_amt=0
    private lateinit var list:MutableList<BetItem>
    private fun submitdata() {
        if((sessionType.equals("open"))||(closeRadioButton.isSelected&&sessionType.equals("close")))
        {

            list = betAdapter.betList;
        if (list.isEmpty()) {
            Toast.makeText(this, "Please make some bet", Toast.LENGTH_SHORT).show()
        }else if(!isTimeBetween(getCurrentTime(),opentime,closetimw)){
            Toast.makeText(applicationContext,"Game is closed",Toast.LENGTH_SHORT).show()
        } else {
            list.forEach { betItem ->
                total_amt = total_amt + betItem.amount as Int
            }
            val balance_after = wallet - total_amt;
            val dialogdata = dialogdata(
                "Red Jodi",
                "$total_amt",
                "$wallet",
                "$balance_after"
            )
            val customDialog = customDialog(this, dialogdata, object : CustomDialogListener {
                override fun onCancelClicked() {
                    total_amt=0
                }

                override fun onConfirmClicked() {
                    if (balance_after < 0) {
                        Toast.makeText(
                            this@RedJodi,
                            "Insufficient Balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        callapi(
                            total_amt)
                    }
                    total_amt=0
                }

            })
            customDialog.show()

        }
            }
        else
        {
            val message=if(openRadioButton.isSelected) "Game run in close session" else "Game run in open session"
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }

    }
    fun convertListToJson(betItems: List<BetItem>): String {
        val gson = Gson()
        return gson.toJson(betItems)
    }
    private fun callapi(total_amt: Int) {
        val gameDatas= GameDatas(
            marketId =marketid.toInt(),
            gameId = 26,
            userId = user.id!!.toInt(),
            gameData = convertListToJson(list),
            totalAmount = total_amt.toDouble(),
            transactionType = "debit",
            transactionNarration = "Game Played",
            session = if(openRadioButton.isSelected) "open" else if(closeRadioButton.isSelected)"close" else ""



        )
        val apiCall= ApiCall()
        apiCall.submitgame(
            gameDatas,
            object : ApiResponse {
                override fun onSuccess(jsonObject: JsonObject) {
                    if(!jsonObject.isJsonNull){
                        if(jsonObject.get("status").toString().equals("\"success\""))
                        {   list.clear()
                            autoCompleteTextView.setText("")
                            pointsEditText.setText("")
                            setwallet()
                            betAdapter.notifyDataSetChanged()
                            Toast.makeText(this@RedJodi,"Bet submitted", Toast.LENGTH_SHORT).show()
                           }
                        else{
                            Toast.makeText(this@RedJodi,jsonObject.get("message").toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@RedJodi,"Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(failure: String) {
                    Toast.makeText(this@RedJodi,failure, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
    private fun addBet() {
        val bet = autoCompleteTextView.text.toString()
        val amount = pointsEditText.text.toString().toIntOrNull()
        if (bet.isNotEmpty() && amount != null) {
            // Check if the bet is not already present
             if(pointsEditText.text.toString().toInt()>=max_bet){
                Toast.makeText(applicationContext,"Maximum Bet amount is $max_bet",Toast.LENGTH_SHORT).show()

            }
            else if(pointsEditText.text.toString().toInt()<=min_bet){
                Toast.makeText(applicationContext,"Minimum Bet amount is $min_bet",Toast.LENGTH_SHORT).show()

            }else if (!betAdapter.betList.any { it.number == bet }) {
                betAdapter.betList.add(BetItem(amount, bet))
                betAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"Bet is already Present", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this,"Please Fill all field", Toast.LENGTH_SHORT).show()
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

    private fun setdigits() {
        val gameData= GameData()
        val list=gameData.red_jodi().sorted()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 0
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedNumber = adapter.getItem(position).toString()
            autoCompleteTextView.setText(selectedNumber)
        }
        autoCompleteTextView.setOnDismissListener {
            val enteredText = autoCompleteTextView.text.toString()
            Log.d("entert",enteredText)
            if (!list.contains(enteredText)) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
                autoCompleteTextView.text.clear()
            }
        }
        autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // Check when focus is lost
                val enteredText = autoCompleteTextView.text.toString()
                if (!list.contains(enteredText)) {
                    Toast.makeText(this, "Invalid digit", Toast.LENGTH_SHORT).show()
                    autoCompleteTextView.text.clear()
                }
            }
        }
    }

    private fun initview() {
        autoCompleteTextView = findViewById(R.id.digits)
        backBtn = findViewById(R.id.back_btn)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        walletIcon = findViewById(R.id.wallet_icon)
        refresh = findViewById(R.id.refresh)
        walletBalanceToolbar = findViewById(R.id.wallet_balanceToolbar)
        currentDate = findViewById(R.id.current_date)
        openRadioButton = findViewById(R.id.open_rb)
        closeRadioButton = findViewById(R.id.close_rb)
        pointsEditText = findViewById(R.id.points)
        addBetButton = findViewById(R.id.add_bet)
        betRecyclerView = findViewById(R.id.betrv)
        submitButton = findViewById(R.id.submit)

    }
}