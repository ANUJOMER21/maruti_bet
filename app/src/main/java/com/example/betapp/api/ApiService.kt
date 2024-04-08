package com.example.betapp.api

import com.example.betapp.model.WebsiteSettingsResponse
import com.example.betapp.model.appversion
import com.example.betapp.model.message
import com.example.betapp.model.sentotp
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("SliderApi.php")
    fun slider():Call<List<com.example.betapp.model.SliderItem>>
    @POST("LoginApi.php")
    fun Login(@Body jsonObject: HashMap<String,String>) :Call<JsonObject>
    @POST("RegisterApi.php")
    fun Signup(@Body  jsonObject: HashMap<String,String>):Call<JsonObject>
    @POST("ChangePassword.php")
    fun changepass(@Body jsonObject: HashMap<String, String>):Call<JsonObject>
    @POST("MarketApi.php")
    fun marketApi():Call<List<com.example.betapp.model.market>>
    @GET("app_version.php")
    fun app_version():Call<appversion>
@POST("WalletApi.php")
@FormUrlEncoded
fun walletApi(@Field("userId") userId:String):Call<JsonObject>
@POST("AppConfig.php")
fun AppConfig():Call<WebsiteSettingsResponse>
@POST("UpdateProfile.php")
fun updateProfile(@Body jsonObject: HashMap<String, String>):Call<JsonObject>
@POST("DepositApi.php")
@FormUrlEncoded
fun depositApi(
    @Field("userId") userid: String,
    @Field("amount") amount: String,
    @Field("payment_type") paymenttype: String,
    @Field("remarks") transactionId: String?
):Call<JsonObject>
@POST("WithdrawApi.php")
@FormUrlEncoded
fun withdrawApi(@Field("userId") userid:String,@Field("amount")amount:String):Call<JsonObject>
@POST("ShowGameApi.php")
fun showgame():Call<com.example.betapp.model.game_amt>
@POST("SubmitGameData.php")
@FormUrlEncoded
fun submitgamedata(
    @Field("marketId") marketId: Int,
    @Field("gameId") gameId: Int,
    @Field("userId") userId: Int,
    @Field("gameData") gameData: String,
    @Field("totalAmount") totalAmount: Double,
    @Field("transactionType") transactionType: String,
    @Field("transactionNarration") transactionNarration: String,
    @Field("session") session:String

):Call<JsonObject>
@POST("BiddingHistoryApi.php")
@FormUrlEncoded
fun  biddinghistory(@Field("userId")userid: String):Call<com.example.betapp.model.BidHistory>
    @POST("TransactionsApi.php")
    @FormUrlEncoded
    fun  transactionHistory(@Field("userId")userid: String):Call<com.example.betapp.model.TransactionHistory>
  @POST("DepositData.php")
    @FormUrlEncoded
    fun  depositHistory(@Field("userId")userid: String):Call<com.example.betapp.model.TransactionHistory>
  @POST("WithdrawData.php")
    @FormUrlEncoded
    fun  withdrawHistory(@Field("userId")userid: String):Call<com.example.betapp.model.TransactionHistory>
  @POST("WinData.php")
    @FormUrlEncoded
    fun  windataHistory(@Field("userId")userid: String):Call<com.example.betapp.model.TransactionHistory>
    @POST("forgetpasswordApi.php")
    @FormUrlEncoded
    fun forget(@Field("phone")mobile:String,@Field("password")password:String):Call<message>
 @POST("verifyOtp.php")
 @FormUrlEncoded
 fun verifyOtp(@Field("otp")otp:String,@Field("session_id")sessionid:String):Call<message>
 @POST("sentOtp.php")
 @FormUrlEncoded
 fun senotp(@Field("phone")phone:String):Call<sentotp>

}