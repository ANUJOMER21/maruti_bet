package com.example.betapp.GameActivity.Gridfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betapp.misc.GameData
import com.example.betapp.model.BetItem

class ViewmodelTriplepatti : ViewModel() {
    private val _betList = MutableLiveData<ArrayList<BetItem>>()
    val betList: LiveData<ArrayList<BetItem>>
        get() = _betList
    fun checkmax(maxBet: Int): String {
        var mes=""
        Log.d("checkmax",maxBet.toString())
        val currentList = _betList.value
        for(item in currentList!!){
            if(item.amount!=0&&item.amount.toInt()>maxBet){
                mes="Please enter amount less than $maxBet in ${item.number}"
                break
            }
        }
        return  mes
    }
    fun checkmin(min:Int):String
    {   var mes=""
        val currentList = _betList.value
        for(item in currentList!!){
            if(item.amount!=0&&item.amount.toInt()<min){
                mes="Please enter amount greater than $min in ${item.number}"
                break
            }
        }
        return  mes
    }
    fun updateBetItemAtPosition(position: Int, newValue: BetItem) {

//        for(item in currentList){
//            if(item.number.equals(position.toString())){
//
//            }
//        }
        for(i in 0..<_betList.value!!.size){
            val item=_betList.value!![i]
            if(item.number.equals(position.toString()))
            {   Log.d("value_total","updated number ${item.number} ${newValue.amount}")
                _betList.value!![i]=newValue
            }
        }


    }
    init {
        populateBetList()
    }
    fun populateBetList() {
        val demoData = ArrayList<BetItem>()
        val data= GameData().TriplePattiList()
        for (i in data) {
            demoData.add(BetItem(0, i.toString()))
        }
        _betList.value = demoData
    }

}