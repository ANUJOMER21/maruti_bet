package com.example.betapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.betapp.R
import com.example.betapp.api.ApiCall
import com.example.betapp.api.ApiResponse
import com.example.betapp.misc.CommonSharedPrefernces
import com.example.betapp.model.WebsiteSettings
import com.example.betapp.model.user
import com.google.gson.JsonObject
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus


import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [walletFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class walletFragment : Fragment(), PaymentStatusListener {
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
    private lateinit var successAddmoneyCard: CardView
    private lateinit var successIconWithdraw: ImageView
    private lateinit var successWithdrawLabel: TextView
    private lateinit var successWithdrawDescription: TextView
    private lateinit var successAddMoneyBtn: AppCompatButton

    private lateinit var layoutAddFunds: LinearLayout
    private lateinit var txtWalletBalance: TextView
    private lateinit var walletAmountAdd: EditText
    private lateinit var txtMinAmount: TextView
    private lateinit var layoutGoogle: CardView
    private lateinit var txtGooglePayNote: TextView
    private lateinit var layoutPhonepe: CardView
    private lateinit var layoutphonepe2:CardView
    private lateinit var txtPhonePeNote: TextView
    private lateinit var layoutPaytm: CardView
    private lateinit var txtPaytmNote: TextView
    private lateinit var layoutOther: CardView
    private lateinit var txtOtherNote: TextView
    private  var ApiCall=ApiCall()
    private var userId:user?=null
    private var min_Deposit:Int = Int.MIN_VALUE;
    var upi:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val  view=inflater.inflate(R.layout.fragment_wallet, container, false)
         initview(view)
         updateWallet()

        val ApiCall=ApiCall()
        ApiCall.getupiId(object :ApiCall.WebseiteSetting{
            override fun onWebsiteSettingReceived(wh: String) {
                upi=wh;
                Log.d("upi_wh",wh)
            }

            override fun onFailure(error: String) {
                Log.d("upierror",error)
                upi=""
            }

        })
        ApiCall.apiconfig(object :ApiCall.WebseiteSettingInterface{
            override fun onWebsiteSettingReceived(wh: WebsiteSettings) {
                min_Deposit=wh.min_deposit.toInt();
            }

            override fun onFailure(error: String) {
               min_Deposit= Int.MAX_VALUE
            }

        })

        ApiCall.deposittypr(object : ApiCall.DepositType{
            override fun onTypeGet(map: HashMap<String, Boolean>) {
                    if(map.get("amazon") == true){
                        layoutGoogle.visibility=View.GONE
                    }
                else{
                    layoutGoogle.visibility=View.GONE
                    }
                if(map.get("paytm") == true){

                    layoutPaytm.visibility=View.VISIBLE
                }
                else{
                    layoutPaytm.visibility=View.GONE
                }
                if(map.get("gpay") == true){

                    layoutOther.visibility=View.VISIBLE
                }
                else{
                    layoutOther.visibility=View.GONE
                }
                if(map.get("phonepe") == true){

                    layoutphonepe2.visibility=View.VISIBLE
                }
                else{
                    layoutphonepe2.visibility=View.GONE
                }
                if(map.get("bhim") == true){

                    layoutPhonepe.visibility=View.VISIBLE
                }
                else{
                    layoutPhonepe.visibility=View.GONE
                }
            }

            override fun onFailure(error: String) {
                     layoutGoogle.visibility=View.GONE
                layoutPaytm.visibility=View.GONE
                layoutphonepe2.visibility=View.GONE
                layoutPhonepe.visibility=View.GONE
                layoutOther.visibility=View.GONE
            }

        })
          layoutGoogle.setOnClickListener{
            if(walletAmountAdd.text.toString().isNotEmpty()){
                payment(
                    PaymentApp.AMAZON_PAY
                    ,walletAmountAdd.text.toString()
                )
            }
              else{
                  Toast.makeText(activity,"Enter amount to deposit",Toast.LENGTH_SHORT).show()
            }
          }
        layoutPaytm.setOnClickListener{
            if(walletAmountAdd.text.toString().isNotEmpty()){
                payment(
            PaymentApp.PAYTM
                    ,walletAmountAdd.text.toString()
                )
            }
            else{
                Toast.makeText(activity,"Enter amount to deposit",Toast.LENGTH_SHORT).show()
            }
        }
        layoutPhonepe.setOnClickListener{
            if(walletAmountAdd.text.toString().isNotEmpty()){
                payment(
                    PaymentApp.BHIM_UPI
                    ,walletAmountAdd.text.toString()
                )
            }
            else{
                Toast.makeText(activity,"Enter amount to deposit",Toast.LENGTH_SHORT).show()
            }
        }
        layoutOther.setOnClickListener{
            if(walletAmountAdd.text.toString().isNotEmpty()){
                payment(
                    PaymentApp.GOOGLE_PAY
                    ,walletAmountAdd.text.toString()
                )
            }
            else{
                Toast.makeText(activity,"Enter amount to deposit",Toast.LENGTH_SHORT).show()
            }
        }
        layoutphonepe2.setOnClickListener{
            if(walletAmountAdd.text.toString().isNotEmpty()){
                payment(
                    PaymentApp.PHONE_PE
                    ,walletAmountAdd.text.toString()
                )
            }
            else{
                Toast.makeText(activity,"Enter amount to deposit",Toast.LENGTH_SHORT).show()
            }
        }
        return  view;
    }
    var amt:String="0"
    var pt:PaymentApp?=null
    private fun payment(paymenttype:PaymentApp,amount:String){
        amt=amount
        pt=paymenttype
        if(amount.toInt()<min_Deposit){
            Toast.makeText(activity,"Minimum Deposit is $min_Deposit",Toast.LENGTH_SHORT).show()
        }
        else{


        try {
            val random = Random
            val random10DigitNumber = (1..10).map { random.nextInt(0, 10) }.joinToString("")
            val amt: Double = amount.toDouble();

            val easyUpiPayment = EasyUpiPayment(requireActivity()) {
                //todo change upiId and name
                this.payeeVpa = upi
                Log.d("upi_id",upi)
                this.paymentApp = paymenttype
                this.payeeName = "Sonu Mishra"
                this.payeeMerchantCode = "200"
                this.transactionId = System.nanoTime().toString()
                this.transactionRefId = random10DigitNumber
                this.description = System.nanoTime().toString()
                this.amount = amt.toString()
            }
            easyUpiPayment.setPaymentStatusListener(this)
            easyUpiPayment.startPayment()
        }
        catch (e:Exception){
            Toast.makeText(activity,e.toString(),Toast.LENGTH_SHORT).show()
        }
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun addmoney(amount: String, paymenttype: PaymentApp, transactionId: String?) {
        val random = Random
        val random10DigitNumber = "${userId}_${System.nanoTime()}"

            ApiCall.depositApi(userId!!.id,amount,paymenttype.name,random10DigitNumber,object :ApiResponse{
                override fun onSuccess(jsonObject: JsonObject) {
                    if(jsonObject.get("status").toString().equals("\"success\"")){
                        Toast.makeText(activity,"Amount Deposited",Toast.LENGTH_SHORT).show()
                        val customDialogFragment = CustomDialogFragment()
                        customDialogFragment.show(childFragmentManager, "CustomDialogFragment")
                        updateWallet()
                    }
                }

                override fun onFailure(failure: String) {
                    Toast.makeText(activity,failure,Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun initview(view: View?) {
        userId=CommonSharedPrefernces(activity as Context).getuser()!!
        if(view!=null) {
            successAddmoneyCard = view.findViewById(R.id.successAddmoneyCard)
            successIconWithdraw = view.findViewById(R.id.successIconWithdraw)
            successWithdrawLabel = view.findViewById(R.id.successWithdrawLable)
            successWithdrawDescription = view.findViewById(R.id.succesWithdrawDiscription)
            successAddMoneyBtn = view.findViewById(R.id.successAddMoneyBtn)
            layoutphonepe2=view.findViewById(R.id.layoutPhonepe2)
            layoutAddFunds = view.findViewById(R.id.layout_addFunds)
            txtWalletBalance = view.findViewById(R.id.txtWalletBalance)
            walletAmountAdd = view.findViewById(R.id.wallet_amount_add)
            txtMinAmount = view.findViewById(R.id.txt_minamount)
            layoutGoogle = view.findViewById(R.id.layoutGoogle)
            txtGooglePayNote = view.findViewById(R.id.txt_googlepaynote)
            layoutPhonepe = view.findViewById(R.id.layoutPhonepe)
            txtPhonePeNote = view.findViewById(R.id.txt_phonepenote)
            layoutPaytm = view.findViewById(R.id.layoutPaytm)
            txtPaytmNote = view.findViewById(R.id.txt_paytmnote)
            layoutOther = view.findViewById(R.id.layoutOther)
            txtOtherNote = view.findViewById(R.id.txt_othernote)
        }
        successAddMoneyBtn.setOnClickListener {
            successAddmoneyCard.visibility=View.GONE
            walletAmountAdd.setText("")

        }


    }

    private fun updateWallet() {
        ApiCall.walletApi(userId!!.id,object :ApiResponse{
            override fun onSuccess(jsonObject: JsonObject) {
                if(jsonObject.get("status").toString().equals("\"success\"")){
                    val b=jsonObject.get("walletBalance").toString().removeSurrounding("\"")
                    txtWalletBalance.setText(b)
                    LocalBroadcastManager.getInstance(activity as Context).sendBroadcast(Intent("wallet"))

                }
                else
                    txtWalletBalance.setText("0")
            }


            override fun onFailure(failure: String) {
             txtWalletBalance.setText("0")
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment walletFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            walletFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onTransactionCancelled() {
        Toast.makeText(activity, "Failed to deposit", Toast.LENGTH_SHORT).show()
    }
    var Tsd: TransactionDetails?=null
    private fun onTransactionSuccess()
    {
        // Payment Success
        addmoney(amt,pt!!,Tsd!!.transactionId)
    }

    private fun onTransactionSubmitted() {
        Log.d("wallet","submitted")

    }

    private fun onTransactionFailed() {
        Toast.makeText(activity,"Transaction Failed",Toast.LENGTH_SHORT).show()
    }
    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess()
            TransactionStatus.FAILURE -> onTransactionFailed()
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }
    }


}