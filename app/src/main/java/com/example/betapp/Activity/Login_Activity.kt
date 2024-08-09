package com.example.betapp.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.bankdetail
import com.example.betapp.model.user
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject

class  Login_Activity : AppCompatActivity() {
    lateinit var mobile:TextInputEditText
    lateinit var password:TextInputEditText
    lateinit var login:AppCompatButton
    lateinit var signup:TextView
    lateinit var whatsapp:TextView
    lateinit var forget:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
val commonSharedPrefernces=CommonSharedPrefernces(this)
if(commonSharedPrefernces.getuser()!=null)
{
val intent=Intent(this,MainActivity::class.java)
    startActivity(intent)
    finish()
}
        initview()
        val ApiCall=ApiCall()
        signup.setOnClickListener {
            val  intent = Intent(this@Login_Activity,SignupPage::class.java)
            startActivity(intent)
            finish()
        }


        login.setOnClickListener {
            if(mobile.text!!.isEmpty()){
                Toast.makeText(this@Login_Activity,"Please enter Mobile Number",Toast.LENGTH_SHORT).show()
            }
            else if(mobile.text.toString().length>10){
                Toast.makeText(this@Login_Activity,"Please Enter Correct No.",Toast.LENGTH_SHORT).show()
            }
            else if (password.text!!.isEmpty()){
                Toast.makeText(this@Login_Activity,"Please enter Password",Toast.LENGTH_SHORT).show()
            }
            else{
                login(mobile.text.toString()!!,password.text.toString()!!)

            }
        }
        var whats=""
        ApiCall.websiteSetting(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                whats=wh;
                whatsapp.text=wh
            }

            override fun onFailure(error: String) {
                Toast.makeText(this@Login_Activity,error.toString(),Toast.LENGTH_SHORT).show()
            }

        })
        whatsapp.setOnClickListener {
            sendMessageToWhatsApp("91"+whats, "Hello")

        }
        forget.setOnClickListener {
            startActivity(Intent(this, com.example.betapp.Activity.Forget_password::class.java))
        }
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
    private fun login(mobile:String,pass:String){
        val  JSONObject :HashMap<String,String> =HashMap()
        val ApiCall=ApiCall()
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
                        val commonSharedPrefernces= CommonSharedPrefernces(this@Login_Activity)
                        commonSharedPrefernces.saveuser(user)
                        commonSharedPrefernces.savebank(bank)
                        val intent:Intent=Intent(this@Login_Activity,MainActivity::class.java)

                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@Login_Activity,jsonObject.get("message").toString(),
                            Toast.LENGTH_SHORT).show()

                    }

                    Log.d("login",jsonObject.toString())
                }

                override fun onFailure(failure: String) {
                    Log.d("login_fail",failure)
                    Toast.makeText(this@Login_Activity,failure, Toast.LENGTH_SHORT).show()
                }

            })
        }
        catch (e:Exception){
            Log.d("login_fail",e.toString())
        }
    }
    private fun initview() {
        mobile=findViewById(R.id.phone_num_login)
        forget=findViewById(R.id.forgetpass);
        password=findViewById(R.id.password_login)
whatsapp=findViewById(R.id.whatsapp_number)
        login=findViewById(R.id.loginBtn)
        signup=findViewById(R.id.newRegistrationTextView)
    }
}