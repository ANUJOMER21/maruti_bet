package com.example.betapp.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject

class SignupPage : AppCompatActivity() {
    private  lateinit var fullname:TextInputEditText
    private  lateinit var email:TextInputEditText
    private  lateinit var mobile:TextInputEditText
    private  lateinit var password:TextInputEditText
    private  lateinit var retype:TextInputEditText
    private lateinit var backToLogin:TextView
    private lateinit var signup:AppCompatButton
    private lateinit var ApiCall:ApiCall
    lateinit var whatsapp:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)
        initview()
         ApiCall=ApiCall()
        backToLogin.setOnClickListener {
            val intent= Intent(this,Login_Activity::class.java)
            startActivity(intent)
            finish()
        }
        signup.setOnClickListener {
             if(fullname.text!!.isEmpty()){
                toast("Please enter Name")
             }
            else if(mobile.text!!.isEmpty()){
                toast("Please enter Mobile No.")
             }
            else if(email.text!!.isEmpty()){
                toast("Please enter Email")
             }
            else if (password.text!!.isEmpty()){
                toast("Please enter Password")

             }
            else if(retype.text!!.isEmpty()){
                toast("Please enter Re-Type Password")
             }
            else if(password.text.toString() != retype.text.toString()){
                toast("Password Not Matching")
             }
            else{
                  signup()
             }
        }
        var whats=""
        ApiCall.websiteSetting(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                whats=wh;
                whatsapp.text=wh
            }

            override fun onFailure(error: String) {
                Toast.makeText(this@SignupPage,error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
        whatsapp.setOnClickListener {
            sendMessageToWhatsApp("91"+whats, "Hello")

        }
    }

    private fun sendotp(mobile: String) {
ApiCall.sendotp(mobile,
    object :ApiCall.otpresponse{
        override fun onSiuccess(sessionId: String) {
            val intent:Intent= Intent(this@SignupPage,PinActivity::class.java)
            intent.putExtra("mobile",mobile.toString())
            intent.putExtra("pass",password.text.toString())
            intent.putExtra("sessionId",sessionId)
            startActivity(intent)
            finish()
        }

        override fun onFailure(failure: String) {
         Toast.makeText(applicationContext,failure,Toast.LENGTH_SHORT).show()
        }

    })
    }

    private fun signup(){
        val map:HashMap<String,String> = HashMap()
        try {
            map.put("name",fullname.text.toString())
            map.put("email",email.text.toString())
            map.put("mobile",mobile.text.toString())
            map.put("password",password.text.toString())
            ApiCall.Signup(map,object :ApiResponse{
                override fun onSuccess(jsonObject: JsonObject) {
                    val status=jsonObject.get("status").toString()
                    if(status == "\"success\"")
                    {
                     //   toast(jsonObject.get("message").toString())
                         sendotp(mobile.text.toString())
                    }
                    else
                    {
                        toast(
                            if(jsonObject.get("message")!=null) jsonObject.get("message").toString()
                            else "Failed"
                        )
                    }
                }

                override fun onFailure(failure: String) {
                    Log.d("Signup",failure)
                    toast(failure)
                }

            })
        }
        catch (e:Exception){
            Log.d("Signup",e.toString())
        }
    }
private fun toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}
    private fun sendMessageToWhatsApp(phoneNumber: String, message: String?) {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            val url =
                "https://api.whatsapp.com/send?phone=" + "$phoneNumber" + "&text=$message"
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun initview() {
        whatsapp=findViewById(R.id.whatsapp_number)
        fullname=findViewById(R.id.nameRegistration);
        email=findViewById(R.id.emailRegistration);
        mobile=findViewById(R.id.phone_num_registration);
        password=findViewById(R.id.pass_registration);
        retype=findViewById(R.id.retype_pass_registration);
        backToLogin=findViewById(R.id.txtBackLogin)
        signup=findViewById(R.id.sendOtpRegistration)
    }
}