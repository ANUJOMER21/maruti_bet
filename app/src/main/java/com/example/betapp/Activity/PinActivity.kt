package com.example.betapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.chaos.view.PinView
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.bankdetail
import com.example.betapp.model.user
import com.google.gson.JsonObject
import kotlin.math.log

class PinActivity : AppCompatActivity() {
private lateinit var ApiCall:ApiCall
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        ApiCall= ApiCall()
        val pinView:PinView=findViewById(R.id.edtPin);
        val button:AppCompatButton=findViewById(R.id.sendOtpRegistration)
        val  mobile:String?=intent.getStringExtra("mobile")
        val pass:String?=intent.getStringExtra("pass")
        val sessionId:String?=intent.getStringExtra("sessionId")
        button.setOnClickListener{
            if(pinView.text.toString().isEmpty()){
                Toast.makeText(this@PinActivity,"Please enter Otp",Toast.LENGTH_SHORT).show()
            }
          else
            {

               ApiCall.checkotp(sessionId!!,
                   pinView.text.toString(),
                   object :ApiCall.otpresponse{
                       override fun onSiuccess(sessionId: String) {
                           Toast.makeText(applicationContext,sessionId,Toast.LENGTH_SHORT).show()
                           login(mobile!!,pass!!)
                       }

                       override fun onFailure(failure: String) {
                          Toast.makeText(applicationContext,failure,Toast.LENGTH_SHORT).show()
                       }

                   })

            }

        }
    }
    private fun login(mobile:String,pass:String){
        val  JSONObject :HashMap<String,String> =HashMap()
        try {
            JSONObject.put("mobile",mobile)
            JSONObject.put("password",pass)
            ApiCall.Login(jsonObject = JSONObject,object: ApiResponse {
                override fun onSuccess(jsonObject: JsonObject) {
                    val status=jsonObject.get("status").toString()
                    if(status.equals("\"success\"")){
                        val user= user(
                            id=jsonObject.get("id").toString().removeSurrounding("\""),
                            name=jsonObject.get("name").toString().removeSurrounding("\""),
                            mobile=jsonObject.get("mobile").toString().removeSurrounding("\""),
                            email = jsonObject.get("email").toString().removeSurrounding("\"")
                        )
                        val bank= bankdetail(
                            phonePe = jsonObject.get("phoneme")!!.toString().removeSurrounding("\""),
                            upiId = jsonObject.get("upi_id")!!.toString().removeSurrounding("\""),
                            bankacNo = jsonObject.get("acno")!!.toString().removeSurrounding("\""),
                            gpay = jsonObject.get("Gpay")!!.toString().removeSurrounding("\""),
                            ifsc = jsonObject.get("ifsc")!!.toString().removeSurrounding("\""),

                            )
                        val commonSharedPrefernces= CommonSharedPrefernces(this@PinActivity)
                        commonSharedPrefernces.saveuser(user)
                        commonSharedPrefernces.savebank(bank)
                        val intent:Intent=Intent(this@PinActivity,MainActivity::class.java)

                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@PinActivity,jsonObject.get("message").toString(),
                            Toast.LENGTH_SHORT).show()

                    }

                    Log.d("login",jsonObject.toString())
                }

                override fun onFailure(failure: String) {
                    Log.d("login_fail",failure)
                    Toast.makeText(this@PinActivity,failure, Toast.LENGTH_SHORT).show()
                }

            })
        }
        catch (e:Exception){
            Log.d("login_fail",e.toString())
        }
    }
}