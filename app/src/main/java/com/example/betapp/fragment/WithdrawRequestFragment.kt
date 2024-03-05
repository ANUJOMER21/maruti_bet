package com.example.betapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.WebsiteSettings
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WithdrawRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WithdrawRequestFragment : Fragment() {
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
private lateinit var withdrawEt:EditText
private lateinit var withdrawBtn:TextView
private lateinit var commonSharedPrefernces: CommonSharedPrefernces
private lateinit var notice:TextView;
    private lateinit var txt_minamount:TextView
    private  var min_withdraw:Int= Int.MIN_VALUE;
    private var max_withdraw:Int=Int.MAX_VALUE;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_withdraw_request, container, false)
        val ApiCall =ApiCall()
        commonSharedPrefernces= CommonSharedPrefernces(activity as Context)
        val userid=commonSharedPrefernces.getuser()!!.id
        withdrawEt=view.findViewById(R.id.edt_withdrawPoints);
        withdrawBtn=view.findViewById(R.id.withdrawBtn)
        notice=view.findViewById(R.id.notice);
        txt_minamount=view.findViewById(R.id.txt_minamount);
        var withtime:String=""
        ApiCall.withdrawtime(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                txt_minamount.setText("Withdraw Timing:- $wh")
                withtime=wh;
            }

            override fun onFailure(error: String) {
               txt_minamount.setText("")
            }

        })

        ApiCall.apiconfig(object :ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_withdraw=wh.min_withdraw.toInt()
                max_withdraw=wh.max_withdraw.toInt()
            }

            override fun onFailure(error: String) {
                TODO("Not yet implemented")
            }

        })
        withdrawBtn.setOnClickListener {
            if(withtime.isNotEmpty()&&isWithinWithdrawTiming(withtime)){

          if(withdrawEt.text.isNotEmpty()){
              if(withdrawEt.text.toString().toInt()<min_withdraw){
                  Toast.makeText(activity,"Minimum withdraw is $min_withdraw",Toast.LENGTH_SHORT).show()
              }
              else if(withdrawEt.text.toString().toInt()>max_withdraw){
                  Toast.makeText(activity,"Maximum withdraw is $max_withdraw",Toast.LENGTH_SHORT).show()
              }
              else {

                  ApiCall.withdrawApi(
                      userid,
                      withdrawEt.text.toString(),
                      object : ApiResponse {
                          override fun onSuccess(jsonObject: JsonObject) {
                              if (jsonObject.get("status").toString().equals("\"success\"")) {
                                  Toast.makeText(
                                      activity,
                                      jsonObject.get("message").toString(),
                                      Toast.LENGTH_SHORT
                                  ).show()
                                  withdrawEt.setText("")
                                  LocalBroadcastManager.getInstance(activity as Context)
                                      .sendBroadcast(
                                          Intent("wallet")
                                      )

                              } else {
                                  Toast.makeText(
                                      activity,
                                      jsonObject.get("message").toString(),
                                      Toast.LENGTH_SHORT
                                  ).show()

                              }
                          }

                          override fun onFailure(failure: String) {
                              Toast.makeText(activity, failure, Toast.LENGTH_SHORT).show()
                          }

                      })
              }
          }
            else
          {
              Toast.makeText(activity,"Enter amount to withdraw",Toast.LENGTH_SHORT).show()
          }}
            else{

                    Toast.makeText(activity,"Withdraw Close",Toast.LENGTH_SHORT).show()

            }
        }

ApiCall.notice_withdrae(object :ApiCall.WebseiteSetting{
    override fun onWebsiteSettingReceived(wh: String) {
        notice.setText(wh)
    }

    override fun onFailure(error: String) {
 notice.setText("")
    }

})
        return  view;
    }
    fun isWithinWithdrawTiming(withdrawTiming: String): Boolean {
        // Get current time
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedCurrentTime = timeFormat.format(currentTime)

        // Split the withdraw timing string into start and end times
        val times = withdrawTiming.split(" - ")
        val startTime = times[0]
        val endTime = times[1]

        // Check if the current time is within the specified range
        return formattedCurrentTime in startTime..endTime
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WithdrawRequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WithdrawRequestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}