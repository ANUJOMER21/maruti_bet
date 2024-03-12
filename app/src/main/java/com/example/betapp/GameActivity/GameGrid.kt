package com.example.betapp.GameActivity

import android.animation.ObjectAnimator
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.misc.getCurrentTimeFromInternet
import com.example.betapp.model.user
import com.google.android.material.card.MaterialCardView
import com.google.gson.JsonObject
import java.util.Locale
import kotlin.random.Random

class GameGrid : AppCompatActivity() {

    private lateinit var singleDigitCardView: MaterialCardView
    private lateinit var doubleDigitCardView: MaterialCardView
    private lateinit var singlePattiCardView: MaterialCardView
    private lateinit var doublePattiCardView: MaterialCardView
    private lateinit var triplePattiCardView: MaterialCardView
    private lateinit var halfSangamCardView: MaterialCardView
    private lateinit var fullSangamCardView: MaterialCardView
    private lateinit var spCardView: MaterialCardView
    private lateinit var dpCardView: MaterialCardView
    private lateinit var familyJodiCardView: MaterialCardView
    private lateinit var redJodiCardView: MaterialCardView
    private lateinit var redFamilyJodiCardView: MaterialCardView
    private lateinit var cyclePattiCardView: MaterialCardView
    private lateinit var familyPanelCardView: MaterialCardView
    private lateinit var back:ImageView;
    private lateinit var wallet_tv:TextView
    private lateinit var reload:ImageView
    private lateinit var marketid:String
    private var rotateAnimator: ObjectAnimator? = null
    private val rotationDuration = 500L
    private var session:String=""
    private var opentime:String=""
    private var closetime:String=""
    private var currentTime:String=""
    private lateinit var user: user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_grid)
        val commonSharedPrefernces=CommonSharedPrefernces(this)
        initview();
        user= commonSharedPrefernces.getuser()!!
        val intentExtras = intent.extras
        marketid= if (intentExtras!!.get("marketId") != null) intentExtras!!.get("marketId").toString() else "0"
        val marketname=intentExtras!!.get("markerName")
        opentime= intentExtras!!.getString("start_time","").toString()
        closetime=intentExtras!!.getString("close_time","")
        session=intentExtras!!.getString("session","")
        Log.d("Session",session)
        gameclick()
        reload_wallet()
        getCurrentTimeFromInternet { time -> currentTime=time
        }

        val toolbartittle:TextView=findViewById(R.id.toolbarTitle)
        toolbartittle.text= (marketname).toString()

        setwallet()
        setupRotateAnimation()

        // Set up a handler to stop the rotation after 3 seconds
        Handler().postDelayed({
            stopRotateAnimation()
        }, rotationDuration)
        reload.setOnClickListener {
            if (rotateAnimator?.isRunning == true) {
                stopRotateAnimation()
            } else {
                startRotateAnimation()
            }
            setwallet()
        }




        back.setOnClickListener { finish() }



    }
    private fun setupRotateAnimation() {
        // Create an ObjectAnimator to rotate the ImageView
        rotateAnimator = ObjectAnimator.ofFloat(reload, "rotation", 0f, 360f)
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
                    wallet_tv.setText(b)
                }
                else
                    wallet_tv.setText("0")
            }

            override fun onFailure(failure: String) {
                wallet_tv.setText("0")
            }

        })
    }
    private fun startRotateAnimation() {
        rotateAnimator?.start()
    }

    private fun stopRotateAnimation() {
        rotateAnimator?.cancel()
    }
    private fun gameclick(){
        if(session.isNotEmpty()) {
            singleDigitCardView.setOnClickListener {
                startCardActivity("SingleDigitActivity")
            }

            doubleDigitCardView.setOnClickListener {
                if(session.equals("open"))
                   startCardActivity("DoubleDigit")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }
            }

            singlePattiCardView.setOnClickListener {
                startCardActivity("SinglePatti")
            }

            doublePattiCardView.setOnClickListener {
                startCardActivity("doublepatti")
            }

            triplePattiCardView.setOnClickListener {
                startCardActivity("TriplePatti")
            }

            halfSangamCardView.setOnClickListener {
                if(session.equals("open"))
                startCardActivity("HalfSangam")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }

            }

            fullSangamCardView.setOnClickListener {
                if(session.equals("open"))
                startCardActivity("FullSangam")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }
            }

            spCardView.setOnClickListener {
                startCardActivity("SPgame")
            }

            dpCardView.setOnClickListener {
                startCardActivity("DpGame")
            }

            familyJodiCardView.setOnClickListener {
                if(session.equals("open"))
                startCardActivity("FamilyJodi")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }
            }

            redJodiCardView.setOnClickListener {
                if(session.equals("open"))
                startCardActivity("RedJodi")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }
            }

            redFamilyJodiCardView.setOnClickListener {
                if(session.equals("open"))
                startCardActivity("redfamilyJodi")
                else{
                    Toast.makeText(this,"Game is Close.",Toast.LENGTH_SHORT).show()
                }
            }

            cyclePattiCardView.setOnClickListener {
                startCardActivity("Cyclepatti")
            }

            familyPanelCardView.setOnClickListener {
                startCardActivity("FamilyPannel")
            }
        }
        else{

            Toast.makeText(this@GameGrid,"Game is closed",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
setwallet()
    }
    private fun isTimeBetween(openTime: String, closeTime: String): Boolean {
        try {
            val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val parser1 = SimpleDateFormat("hh:mm a", Locale.getDefault())

            val currentTimeString = currentTime
            // val currentTimeString =getCurrentTimeFromInternet()
            val currentTimeDate = parser1.parse(currentTimeString)

            val openTimeDate = parser.parse(openTime)
            val closeTimeDate = parser.parse(closeTime)
            Log.d("time","$openTime | $closeTime | $currentTime")
            return currentTimeDate in openTimeDate..closeTimeDate
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Calendar.getInstance().time)
    }
    private fun startCardActivity(cardName: String) {
        try {
            if(marketid.equals("0")){
                showToast("Error")
            }
            else if(!isTimeBetween(opentime,closetime)){
                showToast("Game is closed")
            }
            else {
                val intent =
                    Intent(this, Class.forName("com.example.betapp.GameActivity.$cardName"))
                intent.putExtra("marketId", marketid)
                intent.putExtra("session",session)
                intent.putExtra("starttime",opentime)
                intent.putExtra("endtime",closetime)
                startActivity(intent)
            }
        } catch (e: ClassNotFoundException) {
            showToast("Activity not found for $cardName")
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun reload_wallet() {
        val r=Random.nextInt(1, 101)
        wallet_tv.setText(r.toString())
    }

    private fun initview() {
        singleDigitCardView = findViewById(R.id.single_digit_cv)
        doubleDigitCardView = findViewById(R.id.double_digit_cv)
        singlePattiCardView = findViewById(R.id.single_patti_cv)
        doublePattiCardView = findViewById(R.id.double_patti_cv)
        triplePattiCardView = findViewById(R.id.triplePatti_cv)
        halfSangamCardView = findViewById(R.id.half_sangam_cv)
        fullSangamCardView = findViewById(R.id.full_sangam_cv)
        spCardView = findViewById(R.id.sp_cv)
        dpCardView = findViewById(R.id.dp_cv)
        familyJodiCardView = findViewById(R.id.family_jodi_cv)
        redJodiCardView = findViewById(R.id.Red_jodi_cv)
        redFamilyJodiCardView = findViewById(R.id.Red_family_jodi_cv)
        cyclePattiCardView = findViewById(R.id.cycle_patti_cv)
        familyPanelCardView = findViewById(R.id.family_Panel_cv)
        back=findViewById(R.id.back_btn);
        wallet_tv=findViewById(R.id.wallet_balanceToolbar)
        reload=findViewById(R.id.refresh)
    }
}