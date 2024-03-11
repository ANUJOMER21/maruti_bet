/*
package com.example.betapp.Adapter

import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.betapp.GameActivity.GameGrid
import com.example.betapp.R
import com.example.betapp.model.market
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.URL
import java.util.Date
import java.util.Locale


class MarketAdapter(private val context: Context, private val marketList: List<market>) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.game_list_row, parent, false)
        return MarketViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = marketList[position]
        val result = market.marketTodayOpenNumber.split("-")
        // Set data to views
        holder.marketName.text = market.marketName
        holder.centermarketWinningNumber.text = "-${result.get(1)}-"
        holder.leftmarketWinningNumber.text = result.get(0)
        holder.rightmarketWinningNumber.text = result.get(2)
        holder.marketOpenTime.text = market.marketOpenTime
        holder.marketCloseTime.text = market.marketCloseTime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.bind(
                market,
                market.openSessionOpenTime,
                market.openSessionCloseTime,
                market.closeSessionOpenTime,
                market.closeSessionCloseTime
            )
        }

    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val marketName: TextView = itemView.findViewById(R.id.market_name)
        val centermarketWinningNumber: TextView =
            itemView.findViewById(R.id.center_market_winning_number)
        val leftmarketWinningNumber: TextView =
            itemView.findViewById(R.id.left_market_winning_number)
        val rightmarketWinningNumber: TextView =
            itemView.findViewById(R.id.right_market_winning_number)
        val marketBettingStatus: TextView = itemView.findViewById(R.id.market_betting_status_title)
        val marketOpenTime: TextView = itemView.findViewById(R.id.market_open_time)
        val marketCloseTime: TextView = itemView.findViewById(R.id.market_close_time)
        val play: TextView = itemView.findViewById(R.id.play)

        private val handler = Handler(Looper.getMainLooper())
        private val delay = 10000L // 1 second delay

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(
            item: market,
            openTime: String,
            closeTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {
            updateStatus(item, openTime, closeTime, closeSessionOpenTime, closeSessionCloseTime)

            // Schedule a periodic check to update the status
            handler.postDelayed(object : Runnable {
                override fun run() {
                    updateStatus(
                        item,
                        openTime,
                        closeTime,
                        closeSessionOpenTime,
                        closeSessionCloseTime
                    )
                    handler.postDelayed(this, delay)
                }
            }, delay)
            play.setOnClickListener {

                handleButtonClick(
                    item,
                    openTime,
                    closeTime,
                    closeSessionOpenTime,
                    closeSessionCloseTime
                )
            }


        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun handleButtonClick(
            item: market,
            openTime: String,
            closeTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {

            Log.d("time", "${isTimeBetween( openTime, closeTime)}")
            if (isTimeBetween( openTime, closeTime)) {

                val intent = Intent(itemView.context, GameGrid::class.java)
                intent.putExtra("marketId", item.marketId)
                intent.putExtra("markerName", item.marketName)
                intent.putExtra("start_time", openTime)
                intent.putExtra("close_time", closeTime)
                intent.putExtra("session", "open")
                itemView.context.startActivity(intent)

            } else if (isTimeBetween( closeSessionOpenTime, closeSessionCloseTime)) {
                val intent = Intent(itemView.context, GameGrid::class.java)
                intent.putExtra("marketId", item.marketId)
                intent.putExtra("markerName", item.marketName)
                intent.putExtra("session", "close")
                intent.putExtra("start_time", closeSessionOpenTime)
                intent.putExtra("close_time", closeSessionCloseTime)
                itemView.context.startActivity(intent)
            } else {
                // Betting is closed, vibrate the phone and show a toast
                vibratePhone()
                shakeButton()
                showToast("Market is closed for betting")
            }
        }

        private fun shakeButton() {
            val shakeAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
            shakeAnimation.duration = 500 // Shake for 500 milliseconds
            shakeAnimation.interpolator = CycleInterpolator(5f) // Repeat the animation 5 times
            play.startAnimation(shakeAnimation)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun vibratePhone() {
            val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }

        private fun updateStatus(
            item: market,
            openTime: String,
            closeTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {


            itemView.post {
                if (isTimeBetween( openTime, closeTime)) {
                    setStatus(item, "Betting open", R.color.green_color)
                } else
                    if (isTimeBetween( closeSessionOpenTime, closeSessionCloseTime)) {
                        setStatus(item, "Betting open in Close Time", R.color.green_color)
                    } else {
                        setStatus(item, "Market closed", android.R.color.holo_red_dark)
                    }
            }
        }

        private fun setStatus(item: market, message: String, colorResId: Int) {
            marketBettingStatus.text = message
            marketBettingStatus.setTextColor(
                marketBettingStatus.context.resources.getColor(
                    colorResId
                )
            )
            // You may want to update other views in your item layout based on the status.
        }



        private suspend fun isTimeBetween(
            openTime: String,
            closeTime: String
        ): Boolean {
            try {
                // Get the current Indian time from the internet
                val currentTimeIndian = getCurrentTimeFromInternet()
                Log.d("istimebetween","$openTime| $closeTime | $currentTimeIndian ")

                if (currentTimeIndian != null) {
                    val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    parser.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

                    // Parse the open and close times to Indian time zone
                    val openTimeDate = parser.parse(openTime)
                    val closeTimeDate = parser.parse(closeTime)

                    // Check if the current time is between the open and close times
                    return currentTimeIndian in openTimeDate..closeTimeDate
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }




        suspend fun getCurrentTimeFromInternet(): Date? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL("https://worldtimeapi.org/api/timezone/Asia/Kolkata")
                    val stream = url.openStream()
                    val response = Gson().fromJson(stream.reader(), ApiResponse::class.java)
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(response.datetime)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }

        data class ApiResponse(val datetime: String)


    }
}

*/
package com.example.betapp.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.GameActivity.GameGrid
import com.example.betapp.R
import com.example.betapp.model.market
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MarketAdapter(private val context: Context, private val marketList: List<market>) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.game_list_row, parent, false)
        return MarketViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = marketList[position]
        val result = market.marketTodayOpenNumber.split("-")
        holder.bind(
            market,
            result[0],
            result[1],
            result[2],
            market.marketOpenTime,
            market.marketCloseTime,
            market.openSessionOpenTime,
            market.openSessionCloseTime,
            market.closeSessionOpenTime,
            market.closeSessionCloseTime
        )
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val marketName: TextView = itemView.findViewById(R.id.market_name)
        private val centermarketWinningNumber: TextView =
            itemView.findViewById(R.id.center_market_winning_number)
        private val leftmarketWinningNumber: TextView =
            itemView.findViewById(R.id.left_market_winning_number)
        private val rightmarketWinningNumber: TextView =
            itemView.findViewById(R.id.right_market_winning_number)
        private val marketBettingStatus: TextView =
            itemView.findViewById(R.id.market_betting_status_title)
        private val marketOpenTime: TextView = itemView.findViewById(R.id.market_open_time)
        private val marketCloseTime: TextView = itemView.findViewById(R.id.market_close_time)
        private val play: TextView = itemView.findViewById(R.id.play)

        private val handler = Handler(Looper.getMainLooper())
        private val delay = 10000L // 10 seconds delay

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(
            item: market,
            leftNumber: String,
            centerNumber: String,
            rightNumber: String,
            openTime: String,
            closeTime: String,
            openSessionOpenTime: String,
            openSessionCloseTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {
            marketName.text = item.marketName
            centermarketWinningNumber.text = "-$centerNumber-"
            leftmarketWinningNumber.text = leftNumber
            rightmarketWinningNumber.text = rightNumber
            marketOpenTime.text = openTime
            marketCloseTime.text = closeTime

            play.setOnClickListener {
                handleButtonClick(
                    item,
                    openTime,
                    closeTime,
                    openSessionOpenTime,
                    openSessionCloseTime,
                    closeSessionOpenTime,
                    closeSessionCloseTime
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        updateStatus(item, openTime, closeTime, openSessionOpenTime, openSessionCloseTime, closeSessionOpenTime, closeSessionCloseTime)
                        handler.postDelayed(this, delay)
                    }
                }, delay)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun handleButtonClick(
            item: market,
            openTime: String,
            closeTime: String,
            openSessionOpenTime: String,
            openSessionCloseTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {
            if (isTimeBetween(openTime, closeTime)) {
                startGameActivity(item.marketId, item.marketName, openTime, closeTime, "open")
            } else if (isTimeBetween(openSessionOpenTime, openSessionCloseTime)) {
                startGameActivity(item.marketId, item.marketName, openSessionOpenTime, openSessionCloseTime, "open")
            } else if (isTimeBetween(closeSessionOpenTime, closeSessionCloseTime)) {
                startGameActivity(item.marketId, item.marketName, closeSessionOpenTime, closeSessionCloseTime, "close")
            } else {
                vibratePhone()
                shakeButton()
                showToast("Market is closed for betting")
            }
        }

        private fun startGameActivity(marketId: String, marketName: String, startTime: String, closeTime: String, session: String) {
            val intent = Intent(itemView.context, GameGrid::class.java)
            intent.putExtra("marketId", marketId)
            intent.putExtra("markerName", marketName)
            intent.putExtra("start_time", startTime)
            intent.putExtra("close_time", closeTime)
            intent.putExtra("session", session)
            itemView.context.startActivity(intent)
        }

        private fun shakeButton() {
            val shakeAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
            shakeAnimation.duration = 500 // Shake for 500 milliseconds
            shakeAnimation.interpolator = CycleInterpolator(5f) // Repeat the animation 5 times
            play.startAnimation(shakeAnimation)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun vibratePhone() {
            val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
        }

        private fun updateStatus(
            item: market,
            openTime: String,
            closeTime: String,
            openSessionOpenTime: String,
            openSessionCloseTime: String,
            closeSessionOpenTime: String,
            closeSessionCloseTime: String
        ) {
            if (isTimeBetween(openTime, closeTime)) {
                setStatus(item, "Betting open", R.color.green_color)
            } else if (isTimeBetween(openSessionOpenTime, openSessionCloseTime)) {
                setStatus(item, "Betting open in Close Time", R.color.green_color)
            } else {
                setStatus(item, "Market closed", android.R.color.holo_red_dark)
            }
        }

        private fun setStatus(item: market, message: String, colorResId: Int) {
            marketBettingStatus.text = message
            marketBettingStatus.setTextColor(itemView.context.resources.getColor(colorResId))
        }

        private fun isTimeBetween(startTime: String, endTime: String): Boolean {
            val currentTime = getCurrentTimeFromInternet()
            if (currentTime != null) {
                val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
                parser.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
                val startTimeDate = parser.parse(startTime)
                val endTimeDate = parser.parse(endTime)
                return currentTime in startTimeDate..endTimeDate
            }
            return false
        }

        private fun getCurrentTimeFromInternet(): Date? {
            return runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        val url = URL("https://worldtimeapi.org/api/timezone/Asia/Kolkata")
                        val stream = url.openStream()
                        val response = Gson().fromJson(stream.reader(), ApiResponse::class.java)
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(response.datetime)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }
        }

    }
}
data class ApiResponse(val datetime: String)
