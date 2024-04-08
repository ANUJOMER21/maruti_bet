package com.example.betapp.misc

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.betapp.R
import com.google.android.material.button.MaterialButton

interface CustomDialogListener {
    fun onCancelClicked()
    fun onConfirmClicked()
}
data class dialogdata(val gamename:String,
    val totalamt:String,
    val balance_before:String,
    val  balanceafter:String)

class customDialog(context: Context,val Dialogdata: dialogdata,val listener: CustomDialogListener):AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.game_dialog_item, null)
        setContentView(view)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        Log.d("game_dialog",Dialogdata.gamename)
        val gameNameTextView = view.findViewById<TextView>(R.id.game_name)
        val totalAmtTextView = view.findViewById<TextView>(R.id.total_amt)
        val balanceBeforeTextView = view.findViewById<TextView>(R.id.balance_before)
        val balanceAfterTextView = view.findViewById<TextView>(R.id.balabce_after)
        val cancelButton = view.findViewById<MaterialButton>(R.id.cancel)
        val confirmButton = view.findViewById<MaterialButton>(R.id.confirm)
        gameNameTextView.setText(Dialogdata.gamename)
        totalAmtTextView.setText(Dialogdata.totalamt)
        balanceAfterTextView.setText(Dialogdata.balanceafter)
        balanceBeforeTextView.setText(Dialogdata.balance_before)
        cancelButton.setOnClickListener {
            // Call onCancelClicked in the main activity
            listener.onCancelClicked()
            dismiss()
        }
        confirmButton.setOnClickListener {
            // Call onConfirmClicked in the main activity
            listener.onConfirmClicked()
            dismiss()
        }
    }
}
