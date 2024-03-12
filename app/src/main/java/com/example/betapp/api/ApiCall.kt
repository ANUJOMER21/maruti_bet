package com.example.betapp.api

import RetrofitInstance
import android.annotation.SuppressLint
import android.util.Log
import com.example.betapp.model.BidHistory
import com.example.betapp.model.Game

import com.example.betapp.model.GameDatas
import com.example.betapp.model.SliderItem
import com.example.betapp.model.Transaction
import com.example.betapp.model.TransactionHistory
import com.example.betapp.model.UserGameSubmission
import com.example.betapp.model.WebsiteSettings
import com.example.betapp.model.WebsiteSettingsResponse
import com.example.betapp.model.game_amt
import com.example.betapp.model.market
import com.example.betapp.model.message
import com.example.betapp.model.sentotp
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCall {
    interface SLiderCallback {
        fun onSlierReceived(sliders: List<String>)
        fun onFailure(error: String)
    }
    @SuppressLint("SuspiciousIndentation")
    fun Sliderlist(Lcallback: SLiderCallback){
        val call:Call<List<SliderItem>> =RetrofitInstance.instance.slider()
        call.enqueue(object :Callback<List<SliderItem>>{
            override fun onResponse(
                call: Call<List<SliderItem>>,
                response: Response<List<SliderItem>>
            ) {
                if(response.isSuccessful&& response.body()!!.size>0) {
                  val url= "https://works.diginspire.in/jannat/admin/sliders/"
                    val listslider: ArrayList<String> = ArrayList()
                    for (slider: SliderItem in response.body()!!) {
                        listslider.add(url+slider.sliderImage)
                    }
                    Lcallback.onSlierReceived(listslider)
                }
                else{
                    Lcallback.onFailure("Failed")
                }
            }

            override fun onFailure(call: Call<List<SliderItem>>, t: Throwable) {
       Lcallback.onFailure(
           t.toString()
       )
            }

        })



         }
     fun Login(jsonObject: HashMap<String,String>,callback: ApiResponse){
        val call: Call<JsonObject> = RetrofitInstance.instance.Login(jsonObject)
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
              if(response.body()!=null){
                  Log.d("login_res",response.body().toString())
                  callback.onSuccess(response.body()!!)
              }
                else{
                    callback.onFailure("Null")
              }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
              callback.onFailure("${t.toString()}")
            }

        })

    }
     fun Signup(jsonObject: HashMap<String,String>,callback: ApiResponse){
        val call:Call<JsonObject> =RetrofitInstance.instance.Signup(jsonObject)
         Log.d("signup_map",jsonObject.toString())
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.body()!=null){
                    callback.onSuccess(response.body()!!)
                }
                else{
                    callback.onFailure("Null")
                }
            }


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onFailure("${t.toString()}")
            }

        })
    }
     fun changePass(jsonObject: HashMap<String, String>, callback: ApiResponse){
         val call:Call<JsonObject> =RetrofitInstance.instance.changepass(jsonObject)
         call.enqueue(object :Callback<JsonObject>{
             override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                 if(response.body()!=null){
                     callback.onSuccess(response.body()!!)
                 }
                 else{
                     callback.onFailure("Null")
                 }
             }

             override fun onFailure(call: Call<JsonObject>, t: Throwable) {
     callback.onFailure(t.toString())
             }

         })
     }
    interface MarketCallback {
        fun onMarketsReceived(markets: List<market>)
        fun onFailure(error: Throwable)
    }
    interface WebseiteSettingInterface{
        fun onWebsiteSettingReceived(wh:WebsiteSettings)
        fun onFailure(error: String)
    }

    interface WebseiteSetting{
        fun onWebsiteSettingReceived(wh:String)
        fun onFailure(error: String)
    }
    interface DepositType{
        fun onTypeGet(map:HashMap<String,Boolean>)
        fun  onFailure(error: String)
    }
    fun deposittypr(callback:DepositType){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        var webseiteSettings1: WebsiteSettings? =null
        var map:HashMap<String,Boolean> =HashMap()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success")) {
                        webseiteSettings1 = response.body()!!.websiteSettings
                        if (webseiteSettings1!!.amazonpay.equals("enable")) {
                            map.put("amazon", true)
                        } else {
                            map.put("amazon",false)
                        }
                        if (webseiteSettings1!!.paytm.equals("enable")) {
                            map.put("paytm", true)
                        } else {
                            map.put("paytm",false)
                        }
                        if (webseiteSettings1!!.gpay.equals("enable")) {
                            map.put("gpay", true)
                        } else {
                            map.put("gpay",false)
                        }
                        if (webseiteSettings1!!.phonepe.equals("enable")) {
                            map.put("phonepe", true)
                        } else {
                            map.put("phonepe",false)
                        }
                        if (webseiteSettings1!!.bhim.equals("enable")) {
                            map.put("bhim", true)
                        } else {
                            map.put("bhim",false)
                        }
                        callback.onTypeGet(map)
                    }
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })
    }
    fun youtubesetting(callback: WebseiteSetting){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success"))
                        callback.onWebsiteSettingReceived(if(response.body()!!.websiteSettings.youtube!=null)response.body()!!.websiteSettings.youtube else "")
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })
    }
    fun websiteSetting(callback: WebseiteSetting){
    val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
    call.enqueue(object :Callback<WebsiteSettingsResponse>{
        override fun onResponse(
            call: Call<WebsiteSettingsResponse>,
            response: Response<WebsiteSettingsResponse>
        ) {
            if(response.body()!=null){
                if(response.body()!!.status.equals("success"))
                    callback.onWebsiteSettingReceived(response.body()!!.websiteSettings.homeNumber)
                else{
                    callback.onFailure(response.body()!!.status)
                }
            }
            else{
                callback.onFailure("failed")
            }
        }

        override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
callback.onFailure("failed")
        }


    })
}
    fun apiconfig(callback: WebseiteSettingInterface){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success"))
                        callback.onWebsiteSettingReceived(response.body()!!.websiteSettings)
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })
    }
    fun getupiId(callback: WebseiteSetting){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object : Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()?.websiteSettings!!.upi.isNotEmpty()){
                        callback.onWebsiteSettingReceived(
                            response.
                            body()!!.websiteSettings.upi
                        )
                    }
                    else{
                        callback.onWebsiteSettingReceived("")
                    }
                }
                else{
                    callback.onFailure("Failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
       callback.onFailure(t.toString())
            }

        })
    }
    interface telegramCallback{
        fun telegram(number:String)
        fun failure(fail:String)
    }
    fun telegram(telegramCallback: telegramCallback){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.websiteSettings.telegram_status.equals("hide")){
                        telegramCallback.telegram("")
                    }
                    else{
                        telegramCallback.telegram(
                            response.body()!!.websiteSettings.telegram
                        )
                    }
                }
                else{
                    telegramCallback.failure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
      telegramCallback.failure(t.toString())
            }

        })
    }
    fun notice_withdrae(callback: WebseiteSetting){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success"))
                        callback.onWebsiteSettingReceived(response.body()!!.websiteSettings.notice)
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })
    }
    fun autodeposit(callback: WebseiteSetting){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success"))
                        callback.onWebsiteSettingReceived(response.body()!!.websiteSettings.auto_depost_status)
                    else{
                        callback.onFailure("failed")
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })
    }
    fun withdrawtime(callback: WebseiteSetting){
        val call:Call<WebsiteSettingsResponse> =RetrofitInstance.instance.AppConfig()
        call.enqueue(object :Callback<WebsiteSettingsResponse>{
            override fun onResponse(
                call: Call<WebsiteSettingsResponse>,
                response: Response<WebsiteSettingsResponse>
            ) {
                if(response.body()!=null){
                    if(response.body()!!.status.equals("success"))
                        callback.onWebsiteSettingReceived(response.body()!!.websiteSettings.withdrow_timing)
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<WebsiteSettingsResponse>, t: Throwable) {
                callback.onFailure("failed")
            }


        })

    }

    fun getMarkets(callback: MarketCallback) {
        val call: Call<List<market>> = RetrofitInstance.instance.marketApi()
        call.enqueue(object : Callback<List<market>> {
            override fun onResponse(call: Call<List<market>>, response: Response<List<market>>) {
                if (response.isSuccessful) {
                    val markets = response.body() ?: emptyList()
                    if(markets!=null){
                        callback.onMarketsReceived(markets)
                    }
                    else{
                        callback.onFailure(Throwable("Request failed with code ${response.code()}"))
                    }
                } else {
                    callback.onFailure(Throwable("Request failed with code ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<market>>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
    fun walletApi(userId:String,callback: ApiResponse){
        val call:Call<JsonObject> =RetrofitInstance.instance.walletApi(userId)
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.body()!=null){
                 callback.onSuccess(response.body()!!)
                }
                else{
                    callback.onFailure("failed to get wallet")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
           callback.onFailure(t.toString())
            }

        })
    }
    fun updateProfile(jsonObject: HashMap<String, String>, callback: ApiResponse){
        val call:Call<JsonObject> =RetrofitInstance.instance.updateProfile(jsonObject)
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.body()!=null){
                    callback.onSuccess(response.body()!!)
                }
                else{

                    callback.onFailure("Failed To Update")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onFailure(t.toString())
            }

        })
    }
    fun depositApi(
        userId: String,
        amount: String,
        paymenttype: String,
        transactionId: String?,
        callback: ApiResponse
    ){
        val  call:Call<JsonObject> =RetrofitInstance.instance.depositApi(userId,amount,paymenttype,transactionId)
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                   if(response.body()!=null){
                       callback.onSuccess(response.body()!!)
                   }
                else{
                    callback.onFailure("Failed")
                   }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.onFailure(t.toString())
            }

        })
    }
    fun withdrawApi(userId: String,amount: String,callback: ApiResponse){
        val call:Call<JsonObject> =RetrofitInstance.instance.withdrawApi(userId,amount)
        call.enqueue(object :Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.body()!=null){
                    callback.onSuccess(response.body()!!)
                }
                else{
                    callback.onFailure("Failed")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }

    fun showgame(callback: game){
        val call:Call<game_amt> =RetrofitInstance.instance.showgame()
        call.enqueue(object :Callback<game_amt>{
            override fun onResponse(call: Call<game_amt>, response: Response<game_amt>) {
                    if(response.body()!=null){
                        if(response.body()!!.status.equals("success")){
                            val games = response.body()?.games

// Convert the games to a JSON object using Gson

                            callback.onSuccess(games!!)
                        }
                    }
                else{
                    callback.onFailure("Failed")
                    }
            }

            override fun onFailure(call: Call<game_amt>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    fun submitgame(gameData: GameDatas, callback: ApiResponse){
    val call = RetrofitInstance.instance.submitgamedata(
        gameData.marketId,
        gameData.gameId,
        gameData.userId,
        gameData.gameData,
        gameData.totalAmount,
        gameData.transactionType,
        gameData.transactionNarration,
        gameData.session

    )
    call.enqueue(object :Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if(response.body()!=null){
                callback.onSuccess(response.body()!!)
            }
            else{
                callback.onFailure("Failed")
            }
        }

        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
         callback.onFailure(t.toString())
            Log.d("Failed_game",t.toString())
        }

    })


}
    fun bidHist(userId: String,callback:bid){
    val call:Call<BidHistory> =RetrofitInstance.instance.biddinghistory(userId)
    call.enqueue(object :Callback<BidHistory>{
        override fun onResponse(call: Call<BidHistory>, response: Response<BidHistory>) {
            if(response.body()!!.status.equals("success"))
            {
                callback.onSuccess(response.body()!!.userGameSubmissions)
            }
            else{
                callback.onFailure("failed")
            }
        }

        override fun onFailure(call: Call<BidHistory>, t: Throwable) {
       callback.onFailure(t.toString())
        }

    })
}

    fun deposithist(userId: String,callback:transaction){
        val call:Call<TransactionHistory> =RetrofitInstance.instance.depositHistory(userId)
        call.enqueue(object :Callback<TransactionHistory>{
            override fun onResponse(
                call: Call<TransactionHistory>,
                response: Response<TransactionHistory>
            ) {
                if(response.body()!!.status.equals("success"))
                {
                    callback.onSuccess(response.body()!!.transactions)
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<TransactionHistory>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }

    fun winhisthist(userId: String,callback:transaction){
        val call:Call<TransactionHistory> =RetrofitInstance.instance.windataHistory(userId)
        call.enqueue(object :Callback<TransactionHistory>{
            override fun onResponse(
                call: Call<TransactionHistory>,
                response: Response<TransactionHistory>
            ) {
                if(response.body()!!.status.equals("success"))
                {
                    callback.onSuccess(response.body()!!.transactions)
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<TransactionHistory>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }
    fun withhist(userId: String,callback:transaction){
        val call:Call<TransactionHistory> =RetrofitInstance.instance.withdrawHistory(userId)
        call.enqueue(object :Callback<TransactionHistory>{
            override fun onResponse(
                call: Call<TransactionHistory>,
                response: Response<TransactionHistory>
            ) {
                if(response.body()!!.status.equals("success"))
                {
                    callback.onSuccess(response.body()!!.transactions)
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<TransactionHistory>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }
    fun transhist(userId: String,callback:transaction){
        val call:Call<TransactionHistory> =RetrofitInstance.instance.transactionHistory(userId)
        call.enqueue(object :Callback<TransactionHistory>{
            override fun onResponse(
                call: Call<TransactionHistory>,
                response: Response<TransactionHistory>
            ) {
                if(response.body()!!.status.equals("success"))
                {
                    callback.onSuccess(response.body()!!.transactions)
                }
                else{
                    callback.onFailure("failed")
                }
            }

            override fun onFailure(call: Call<TransactionHistory>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }
    interface otpresponse{
        public fun onSiuccess(sessionId:String)
        fun onFailure(failure: String)
    }

    fun sendotp(Mobile: String,callback: otpresponse){
val call:Call<sentotp> =RetrofitInstance.instance.senotp(Mobile)
        call.enqueue(object :Callback<sentotp>{
            override fun onResponse(call: Call<sentotp>, response: Response<sentotp>) {
                if(response.isSuccessful){
                    if(response.body()!!.status.equals("success"))
                    {
                        callback.onSiuccess(response.body()!!.session_id)
                    }
                    else{
                        callback.onFailure(response.body()!!.status)
                    }
                }
                else callback.onFailure("Failed")
            }

            override fun onFailure(call: Call<sentotp>, t: Throwable) {
                  callback.onFailure(t.toString())
            }

        })


    }
    fun checkotp(sesssionId: String, otp: String, callback: otpresponse){
         val call:Call<message> =RetrofitInstance.instance.verifyOtp(
             otp = otp,
             sessionid = sesssionId
         )
        call.enqueue(object :Callback<message>{
            override fun onResponse(call: Call<message>, response: Response<message>) {
                if(response.isSuccessful){
                    if(response.body()!!.status.equals("success"))
                    {
                        callback.onSiuccess(response.body()!!.message)
                    }
                    else{
                        callback.onFailure(response.body()!!.message)
                    }
                }
                else callback.onFailure("Failed")
            }


            override fun onFailure(call: Call<message>, t: Throwable) {
             callback.onFailure(t.toString())
            }

        })
    }
    fun newpass(Mobile: String,newpass:String,callback: otpresponse){
        Log.d("mobile_pass","$Mobile,$newpass")
              val call:Call<message> =RetrofitInstance.instance.forget(mobile = Mobile, password = newpass)
        call.enqueue(object :Callback<message>{
            override fun onResponse(call: Call<message>, response: Response<message>) {
                if(response.isSuccessful){
                    if(response.body()!!.status.equals("success"))
                    {
                        callback.onSiuccess(response.body()!!.message)
                    }
                    else{
                        callback.onFailure(response.body()!!.message)
                    }
                }
                else callback.onFailure("Failed")
            }

            override fun onFailure(call: Call<message>, t: Throwable) {
                callback.onFailure(t.toString())
            }

        })
    }


}
interface transaction{
    fun onSuccess(games: List<Transaction>)
    fun onFailure(failure:String)
}
interface bid{
    fun onSuccess(games: List<UserGameSubmission>)
    fun onFailure(failure:String)
}
interface game{
    fun onSuccess(games: List<Game>)
    fun onFailure(failure:String)
}