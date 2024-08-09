package com.example.betapp.GameActivity

import android.animation.ObjectAnimator
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.Adapter.BetAdapter2
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.misc.CustomDialogListener
import com.example.betapp.misc.customDialog
import com.example.betapp.misc.dialogdata
import com.example.betapp.misc.getCurrentTimeFromInternet
import com.example.betapp.model.BetItem
import com.example.betapp.model.GameDatas
import com.example.betapp.model.WebsiteSettings
import com.example.betapp.model.user
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class SingleDigitActivity : AppCompatActivity() {

    private lateinit var backBtn: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var refresh: ImageView
    private lateinit var walletBalanceToolbar: TextView
    private lateinit var currentDate: TextView
    private lateinit var openRadioButton: RadioButton
    private lateinit var closeRadioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var digitsEditText: EditText
    private lateinit var pointsEditText: EditText
    private lateinit var addBetButton: MaterialButton
    private lateinit var betRecyclerView: RecyclerView
    private lateinit var submitButton: MaterialButton
    private lateinit var user: user
    private  var wallet:Double=0.0
    private var opentime:String=""
    private var closetimw:String=""
    private fun isTimeBetween( openTime: String, closeTime: String): Boolean {
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
    private var min_bet:Int=Int.MIN_VALUE
    private var max_bet:Int=Int.MAX_VALUE
    private suspend fun getCurrentTime(): String {
       /* var currentTime: String = ""
        // Assuming getCurrentTimeFromInternet is a suspend function
        getCurrentTimeFromInternet { currentTime=it }

        Log.d("current_time",currentTime)
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(currentTime)*/
        return withContext(Dispatchers.IO){
            try {
                var currentTime: String = ""
               getCurrentTimeFromInternet { currentTime=it }
                val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                 dateFormat.format(currentTime)
            }
            catch (e:Exception){
                ""
            }
        }
    }


    private var marketid:String=""
    private var sessionType:String="";
    private lateinit var commonSharedPrefernces: CommonSharedPrefernces
    private var rotateAnimator: ObjectAnimator? = null
    private lateinit var betAdapter: BetAdapter2
    private val rotationDuration = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_digit)
        initview()
        ApiCall().apiconfig(object :ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_bet=wh.min_bet.toInt()
                max_bet=wh.max_bet.toInt()
            }

            override fun onFailure(error: String) {
//
            }

        })


        commonSharedPrefernces= CommonSharedPrefernces(this)
        user=commonSharedPrefernces.getuser()!!
        marketid= intent.getStringExtra("marketId").toString()
        sessionType=intent.getStringExtra("session").toString()
        opentime=intent.getStringExtra("starttime").toString()
        closetimw=intent.getStringExtra("endtime").toString()
        openRadioButton.isChecked=true;
        if(sessionType.equals("close")){
            closeRadioButton.isChecked=true;
            openRadioButton.visibility= View.GONE;
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
        betAdapter= BetAdapter2(mutableListOf(),this)
        betRecyclerView.adapter=betAdapter
        addBetButton.setOnClickListener {
            addBet()
        }
        submitButton.setOnClickListener {
            submitButton.visibility=View.GONE
            submitdata()

        }
    }
    var total_amt=0
    private lateinit var list:MutableList<BetItem>

    private fun submitdata() {

        if((sessionType.equals("open"))||(closeRadioButton.isChecked&&sessionType.equals("close")))
        {


            list = betAdapter.betList;
            if (list.isEmpty()) {
                submitButton.visibility=View.VISIBLE
                Toast.makeText(this, "Please make some bet", Toast.LENGTH_SHORT).show()
            }   else {
                list.forEach { betItem ->
                    total_amt = total_amt + betItem.amount as Int
                }
                val balance_after = wallet - total_amt;
                val dialogdata = dialogdata(
                    "Single Digit",
                    "$total_amt",
                    "$wallet",
                    "$balance_after"
                )
                val customDialog = customDialog(this, dialogdata, object : CustomDialogListener {
                    override fun onCancelClicked() {
                        total_amt = 0
                        submitButton.visibility=View.VISIBLE
                    }

                    override fun onConfirmClicked() {
                        GlobalScope.launch(Dispatchers.Main) {

                            // Use currentTime as needed

                            if (balance_after < 0) {
                                Toast.makeText(
                                    this@SingleDigitActivity,
                                    "Insufficient Balance",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (!isTimeBetween( opentime, closetimw)) {
                                Toast.makeText(
                                    applicationContext,
                                    "Game is closed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                callapi(total_amt)
                            }
                            total_amt = 0
                           visblesubmitbtn()
                        }
                    }

                })
                customDialog.show()

            }
        }
        else
        {
            submitButton.visibility=View.VISIBLE
            val message=if(openRadioButton.isChecked) "Game run in close session" else "Game run in open session"
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }

    }
    private fun visblesubmitbtn() {
        Handler(Looper.getMainLooper()).postDelayed({
            // Show the button after 10 seconds
            submitButton.visibility=View.VISIBLE
        }, 5000)


    }
    fun convertListToJson(betItems: List<BetItem>): String {
        val gson = Gson()
        return gson.toJson(betItems)
    }
    private fun callapi(total_amt: Int) {
        val gameDatas=GameDatas(
            marketId =marketid.toInt(),
            gameId = 37,
            userId = user.id.toInt(),
            gameData = convertListToJson(list),
            totalAmount = total_amt.toDouble(),
            transactionType = "debit",
            transactionNarration = "Game Played",
            session = if(openRadioButton.isChecked) "open" else if(closeRadioButton.isChecked)"close" else ""

        )
        val apiCall=ApiCall()
        apiCall.submitgame(
            gameDatas,
            object :ApiResponse{
                override fun onSuccess(jsonObject: JsonObject) {
                    if(!jsonObject.isJsonNull){
                        if(jsonObject.get("status").toString().equals("\"success\""))
                        {Toast.makeText(this@SingleDigitActivity,"Bet submitted",Toast.LENGTH_SHORT).show()
                           list.clear()
                            digitsEditText.setText("");
                            setwallet()
                            pointsEditText.setText("")
                          betAdapter.notifyDataSetChanged()}
                        else{
                            Toast.makeText(this@SingleDigitActivity,jsonObject.get("message").toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@SingleDigitActivity,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(failure: String) {
                  Toast.makeText(this@SingleDigitActivity,failure,Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
    private fun addBet() {
        val bet = digitsEditText.text.toString()
        val amount = pointsEditText.text.toString().toIntOrNull()
        if (bet.isNotEmpty() && amount != null) {
            // Check if the bet is not already present
             if(pointsEditText.text.toString().toInt()>=max_bet){
                Toast.makeText(applicationContext,"Maximum Bet amount is $max_bet",Toast.LENGTH_SHORT).show()

            }
            else if(pointsEditText.text.toString().toInt()<=min_bet){
                Toast.makeText(applicationContext,"Minimum Bet amount is $min_bet",Toast.LENGTH_SHORT).show()

            }  else if (!betAdapter.betList.any { it.number == bet }) {
                betAdapter.betList.add(BetItem(amount, bet))
                betAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"Bet is already Present",Toast.LENGTH_SHORT).show()
            }
        } else {
           Toast.makeText(this,"Please Fill all field",Toast.LENGTH_SHORT).show()
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
        radioGroup = findViewById(R.id.radiogroup)
        digitsEditText = findViewById(R.id.digits)
        pointsEditText = findViewById(R.id.points)
        addBetButton = findViewById(R.id.add_bet)
        betRecyclerView = findViewById(R.id.betrv)
        submitButton = findViewById(R.id.submit)
    }
}