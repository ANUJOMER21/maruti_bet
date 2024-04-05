package com.example.betapp.GameActivity

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.ImageView
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

class DoubleDigit : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refreshIcon: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var currentDate: TextView
    private lateinit var digitsEditText: EditText
    private lateinit var pointsEditText: EditText
    private lateinit var addBetButton: MaterialButton
    private lateinit var betRecyclerView: RecyclerView
    private lateinit var submitButton: MaterialButton

    private lateinit var user: user
    private  var wallet:Double=0.0
    private var marketid:String=""
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private lateinit var betAdapter: BetAdapter
    private val rotationDuration = 500L
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
    private fun initializeViews() {
        // Toolbar


        // Elements inside the Toolbar
        backBtn = findViewById(R.id.back_btn)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        walletIcon = findViewById(R.id.wallet_icon)
        refreshIcon = findViewById(R.id.refresh)
        walletBalanceToolbar = findViewById(R.id.wallet_balanceToolbar)

        // Other elements in the layout
        currentDate = findViewById(R.id.current_date)
        digitsEditText = findViewById(R.id.digits)
        pointsEditText = findViewById(R.id.points)
        addBetButton = findViewById(R.id.add_bet)
        betRecyclerView = findViewById(R.id.betrv)
        submitButton = findViewById(R.id.submit)
    }
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_digit)
        initializeViews()

        commonSharedPrefernces= CommonSharedPrefernces(this)
        user=commonSharedPrefernces.getuser()!!
        marketid= intent.getStringExtra("marketId").toString()
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

        setwallet()
        setupRotateAnimation()
        backBtn.setOnClickListener {
            finish()
        }
        refreshIcon.setOnClickListener {
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
        list = betAdapter.betList;
        if (list.isEmpty()) {
            Toast.makeText(this, "Please make some bet", Toast.LENGTH_SHORT).show()
        }

        else {
            list.forEach { betItem ->
                total_amt = total_amt + betItem.amount as Int
            }
            val balance_after = wallet - total_amt;
            val dialogdata = dialogdata(
                "Double Digit",
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
                            this@DoubleDigit,
                            "Insufficient Balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else if(!isTimeBetween(getCurrentTime(),opentime,closetimw)){
                        Toast.makeText(applicationContext,"Game is closed",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        callapi(total_amt)
                    }
                    total_amt=0
                }


            })
            customDialog.show()
        }
    }
    fun convertListToJson(betItems: List<BetItem>): String {
        val gson = Gson()

        return gson.toJson(betItems)
    }
    private fun callapi(total_amt: Int) {
        val gameDatas= GameDatas(
            marketId =marketid.toInt(),
            gameId = 38,
            userId = user.id!!.toInt(),
            gameData = convertListToJson(list),

            totalAmount = total_amt.toDouble(),
            transactionType = "debit",
            transactionNarration = "Game Played",
            session = "open"

        )
        Log.d("doubledigit",gameDatas.gameData)
        val apiCall= ApiCall()
        apiCall.submitgame(
            gameDatas,
            object : ApiResponse {
                override fun onSuccess(jsonObject: JsonObject) {
                    if(!jsonObject.isJsonNull){
                        if(jsonObject.get("status").toString().equals("\"success\""))
                        {
                            Toast.makeText(this@DoubleDigit,"Bet submitted", Toast.LENGTH_SHORT).show()
                            list.clear()
                               setwallet()
                            digitsEditText.setText("");
                            pointsEditText.setText("")
                            betAdapter.notifyDataSetChanged()

                        }
                        else{
                            Toast.makeText(this@DoubleDigit,jsonObject.get("message").toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@DoubleDigit,"Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(failure: String) {
                    Toast.makeText(this@DoubleDigit,failure, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
    private fun addBet() {
        val bet = String.format("%02d",digitsEditText.text.toString().toInt())
        val amount = pointsEditText.text.toString().toIntOrNull()
        if (bet.isNotEmpty() && amount != null) {
            // Check if the bet is not already present
             if(pointsEditText.text.toString().toInt()>=max_bet){
                Toast.makeText(applicationContext,"Maximum Bet amount is $max_bet",Toast.LENGTH_SHORT).show()

            }
            else if(digitsEditText.text.length==1){
                Toast.makeText(applicationContext,"Please type both digit ",Toast.LENGTH_SHORT).show()
                 digitsEditText.setText("")
             }
            else if(pointsEditText.text.toString().toInt()<=min_bet){
                Toast.makeText(applicationContext,"Minimum Bet amount is $min_bet",Toast.LENGTH_SHORT).show()

            }
           else if (!betAdapter.betList.any { it.number == bet }) {
                betAdapter.betList.add(BetItem(amount, bet))
                betAdapter.notifyDataSetChanged()
            }
            else {
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
        rotateAnimator = ObjectAnimator.ofFloat(refreshIcon, "rotation", 0f, 360f)
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
}