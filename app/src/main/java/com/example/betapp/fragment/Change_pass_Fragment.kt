package com.example.betapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.chaos.view.PinView
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.bankdetail
import com.google.android.material.button.MaterialButton
import com.google.gson.JsonObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Change_pass_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Change_pass_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
private lateinit var oldpass_edt:EditText
    private lateinit var newpass_edt:EditText
    private lateinit var re_type_pass:EditText
    private lateinit var otpll:LinearLayout
    private lateinit var forgetll:LinearLayout
    private lateinit var pinview:PinView
    private lateinit var send:AppCompatButton
private var sid:String=""
private lateinit var submit:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_change_pass_, container, false)
         oldpass_edt=view.findViewById(R.id.edt_OldPassword)
        newpass_edt=view.findViewById(R.id.edt_NewPassword);
        re_type_pass=view.findViewById(R.id.edt_ConfirmNewPassword);
        otpll=view.findViewById(R.id.otpll)
        forgetll=view.findViewById(R.id.forgetll)
        pinview=view.findViewById(R.id.edtPin)
        send=view.findViewById(R.id.sendprofile);
        submit=view.findViewById(R.id.btnAddPoints);
        val commonSharedPrefernces:CommonSharedPrefernces= CommonSharedPrefernces(requireActivity())
        val uid=commonSharedPrefernces.getuser()!!.id
        submit.setOnClickListener{
            val oldPass = oldpass_edt.text.toString().trim()
            val newPass = newpass_edt.text.toString().trim()
            val retypePass = re_type_pass.text.toString().trim()

            if (oldPass.isEmpty() || newPass.isEmpty() || retypePass.isEmpty() || newPass != retypePass) {
                // Show a Toast message for the relevant condition
                when {
                    oldPass.isEmpty() -> showToast("Old password cannot be empty")
                    newPass.isEmpty() -> showToast("New password cannot be empty")
                    retypePass.isEmpty() -> showToast("Retype password cannot be empty")
                    newPass != retypePass -> showToast("Passwords do not match")
                }
            } else {
               val mobile=commonSharedPrefernces.getuser()!!.mobile
                val apiCall=ApiCall()
                apiCall.sendotp(mobile,
                    object :ApiCall.otpresponse{
                        override fun onSiuccess(sessionId: String) {
                            sid=sessionId
                            otpll.visibility=View.VISIBLE
                            forgetll.visibility=View.GONE;
                        }

                        override fun onFailure(failure: String) {
                          Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
                        }

                    })



            }
        }
        send.setOnClickListener {
            val oldPass = oldpass_edt.text.toString().trim()
            val newPass = newpass_edt.text.toString().trim()
            val retypePass = re_type_pass.text.toString().trim()
            if(pinview.text.toString().isEmpty()){
                Toast.makeText(activity,"Please Enter otp",Toast.LENGTH_SHORT).show()
            }
            else{
                val apiCall=ApiCall()
                apiCall.checkotp(sid,
                    pinview.text.toString(),object :ApiCall.otpresponse{
                        override fun onSiuccess(sessionId: String) {
                            val apicall=ApiCall()
                            val hashmap:HashMap<String,String> =HashMap()
                            hashmap.put("userId",uid)
                            hashmap.put("currentPassword",oldPass);
                            hashmap.put("newPassword",newPass)
                            apicall.changePass(hashmap,object :ApiResponse{
                                override fun onSuccess(jsonObject: JsonObject) {
                                    val status =jsonObject.get("status").toString()
                                    if(status.equals("\"success\"")){
                                        showToast("Password Change Succesfully")
                                        otpll.visibility=View.GONE
                                        forgetll.visibility=View.VISIBLE
                                        oldpass_edt.setText("");
                                        newpass_edt.setText("")
                                        re_type_pass.setText("")

                                    }
                                    else{
                                        val message =jsonObject.get("message").toString()
                                        showToast(message)
                                        otpll.visibility=View.GONE
                                        forgetll.visibility=View.VISIBLE
                                        oldpass_edt.setText("");
                                        newpass_edt.setText("")
                                        re_type_pass.setText("")
                                    }
                                }

                                override fun onFailure(failure: String) {
                                    showToast(failure)
                                    otpll.visibility=View.GONE
                                    forgetll.visibility=View.VISIBLE
                                    oldpass_edt.setText("");
                                    newpass_edt.setText("")
                                    re_type_pass.setText("")
                                }

                            })


                        }

                        override fun onFailure(failure: String) {
                            Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
                        }

                    })

            }

        }
        return  view
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Change_pass_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Change_pass_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}