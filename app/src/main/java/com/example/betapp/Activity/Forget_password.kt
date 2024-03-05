package com.example.betapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.chaos.view.PinView
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.model.message
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject

class Forget_password : AppCompatActivity() {
    private lateinit var imgLogo: ImageView
    private lateinit var titleText: TextView
    private lateinit var phoneNumEditText: TextInputEditText
    private lateinit var otpPinView: PinView
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginBtn: AppCompatButton
    private lateinit var otpBtn: AppCompatButton
    private lateinit var passBtn: AppCompatButton
    private lateinit var apicall:ApiCall
    private lateinit var phoneLinearLayout: LinearLayout
    private lateinit var otpLinearLayout: LinearLayout
    private lateinit var newPasswordLinearLayout: LinearLayout
    private var sessionId:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        initView()

    }


    private fun initView() {
        imgLogo = findViewById(R.id.imgLogo)
        titleText = findViewById(R.id.titleText)
         apicall=ApiCall()
        phoneNumEditText = findViewById(R.id.phone_num_login)
        otpPinView = findViewById(R.id.edtPin)
        passwordEditText = findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginBtn)
        otpBtn = findViewById(R.id.otpBtn)
        passBtn = findViewById(R.id.passBtn)
        phoneLinearLayout = findViewById(R.id.phonell)
        otpLinearLayout = findViewById(R.id.otpll)
        newPasswordLinearLayout = findViewById(R.id.newpassll)

        // Set onClickListener for buttons if needed
        loginBtn.setOnClickListener {
            // Handle login button click

            if(phoneNumEditText.text.toString().isNotEmpty()){
                otpLinearLayout.visibility=View.VISIBLE
                phoneLinearLayout.visibility=View.GONE
                 apicall.sendotp(Mobile = phoneNumEditText.text.toString(),
                     object :ApiCall.otpresponse{
                         override fun onSiuccess(sessionId: String) {
                             this@Forget_password.sessionId =sessionId


                         }

                         override fun onFailure(failure: String) {
                             Toast.makeText(this@Forget_password,failure,Toast.LENGTH_SHORT).show()
                         }

                     })

            }
            else{
                Toast.makeText(this@Forget_password,"Please Enter Number",Toast.LENGTH_SHORT).show()
            }

        }

        otpBtn.setOnClickListener {
            if(otpPinView.text.toString()
                .isNotEmpty()){
                if(sessionId.isNotEmpty()) {
                    apicall.checkotp(sessionId, otp = otpPinView.text.toString(), object : ApiCall.otpresponse {
                        override fun onSiuccess(sessionId: String) {
                            otpLinearLayout.visibility = View.GONE
                            newPasswordLinearLayout.visibility = View.VISIBLE

                        }



                        override fun onFailure(failure: String) {
                            Toast.makeText(this@Forget_password, failure, Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
                }
        }
            else{
                Toast.makeText(this@Forget_password,"Please enter otp",Toast.LENGTH_SHORT).show()
            }

        }
        passBtn.setOnClickListener {
             if(passwordEditText.text.toString().isNotEmpty()){
                 apicall.newpass(phoneNumEditText.text.toString(),passwordEditText.text.toString(),object :ApiCall.otpresponse{
                     override fun onSiuccess(sessionId: String) {
                         Toast.makeText(this@Forget_password,sessionId,Toast.LENGTH_SHORT).show()

                         finish()
                     }

                     override fun onFailure(failure: String) {
                         Toast.makeText(this@Forget_password,failure,Toast.LENGTH_SHORT).show()
                     }
                 })

             }
        }
    }


}