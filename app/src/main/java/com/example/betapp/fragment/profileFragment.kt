package com.example.betapp.fragment

import android.content.Context
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
import com.example.betapp.misc.ToolbarChangeListener
import com.example.betapp.model.bankdetail
import com.google.gson.JsonObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class profileFragment : Fragment() {
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
    private var toolbarChangeListener: ToolbarChangeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarChangeListener) {
            toolbarChangeListener = context
        } else {
            throw ClassCastException("$context must implement ToolbarChangeListener")
        }
    }

    override fun onResume() {
        super.onResume()
        //toolbarChangeListener?.onToolbarTitleChange("Profile")
    }
    private var sessionId:String=""
    private lateinit var commonSharedPrefernces:CommonSharedPrefernces;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
          commonSharedPrefernces= CommonSharedPrefernces(activity as Context)
        val user=commonSharedPrefernces.getuser();
        val name:TextView=view.findViewById(R.id.profile_name);
        val number:TextView=view.findViewById(R.id.profile_number);
        var otp:PinView=view.findViewById(R.id.edtPin)
        var updateprofile:AppCompatButton=view.findViewById(R.id.sendprofile)
        val otpll:LinearLayout=view.findViewById(R.id.otpll);
        val profilell:LinearLayout=view.findViewById(R.id.layoutInformation)

        name.setText(user!!.name)
        number.setText(user!!.mobile)
        val email:EditText=view.findViewById(R.id.emailRegistration)
        email.setText(user.email)

        val phonepe:EditText=view.findViewById(R.id.phone_pe_no)
        val upiid:EditText=view.findViewById(R.id.upi_id)
        val bank:EditText=view.findViewById(R.id.bank_account)
        val ifsc:EditText=view.findViewById(R.id.Ifsc)
        val gpay:EditText=view.findViewById(R.id.google_pay)

        val bankdetail=commonSharedPrefernces.getbank()
        if(bankdetail!=null){
Log.d("bankdetail",bankdetail.bankacNo)
            phonepe.setText(
               if(!bankdetail.phonePe.equals("null")) bankdetail.phonePe else ""
            )
            upiid.setText(
                if(!bankdetail.upiId.equals("null")) bankdetail.upiId else ""
            )
            bank.setText(    if(!bankdetail.bankacNo.equals("null")) bankdetail.bankacNo else "")
            ifsc.setText(    if(!bankdetail.ifsc.equals("null")) bankdetail.ifsc else "")
            gpay.setText(    if(!bankdetail.gpay.equals("null")) bankdetail.gpay else "")
        }
        val update_profile:AppCompatButton=view.findViewById(R.id.sendOtpRegistration);
        update_profile.setOnClickListener {
            if (email.text.isEmpty() || phonepe.text.isEmpty() ||
          /*      upiid.text.isEmpty() ||*/ bank.text.isEmpty() || ifsc.text.isEmpty()) {
                Toast.makeText(activity, "Please Fill All Field", Toast.LENGTH_SHORT).show()
                // Handle the case where any of the fields is empty, show an error message or take appropriate action
            }
            else {
                val jsonObject = HashMap<String,String>()
                jsonObject.put("userId", user.id)
                jsonObject.put("email", email.text.toString())
                jsonObject.put("phoneme", phonepe.text.toString())
                jsonObject.put("upi_id", "")
                jsonObject.put("acno", bank.text.toString())
                jsonObject.put("ifsc", ifsc.text.toString())
                jsonObject.put("Gpay",gpay.text.toString())
                Log.d("json",jsonObject.toString());
                // Now you have a JSON object with all the fields and userId
                val apiCall:ApiCall= ApiCall()
                apiCall.updateProfile(jsonObject,object :ApiResponse{
                    override fun onSuccess(jsonObject: JsonObject) {
                        if (!jsonObject.isJsonNull) {
                            if (jsonObject.get("status").toString().equals("\"success\"")) {
                                val data = jsonObject.getAsJsonObject("data")

                                val bankdetail=bankdetail(
                                    phonePe = data.get("phoneme").toString().removeSurrounding("\""),
                                    upiId =data.get("upi_id").toString().removeSurrounding("\""),
                                    bankacNo = data.get("acno").toString().removeSurrounding("\""),
                                    gpay=gpay.text.toString(),
                                    ifsc=data.get("ifsc").toString().removeSurrounding("\"")

                                )
                                commonSharedPrefernces.savebank(bankdetail)
                                otpll.visibility=View.GONE
                                profilell.visibility=View.VISIBLE
                                Toast.makeText(activity,"Detail Updated",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity,"Error: ${jsonObject.get("status")}",Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(activity,"Failed",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(failure: String) {
                        Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
                    }

                })

                // All fields are non-empty, create a JSON object

                // Do something with the jsonString, such as sending it to a server or storing it locally
                // Example: sendJsonToServer(jsonString)
            }

        }
        updateprofile.setOnClickListener {
            if(otp.text.toString().isEmpty()){
                Toast.makeText(activity,"Please enter otp",Toast.LENGTH_SHORT).show()
            }
            else {
                val apiCall=ApiCall()
                apiCall.checkotp(sessionId,
                    otp.text.toString(),object :ApiCall.otpresponse{
                        override fun onSiuccess(sessionId: String) {



                        }

                        override fun onFailure(failure: String) {
                     Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
                        }

                    })


            }


        }


        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}