package com.example.betapp.GameActivity.Gridfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.betapp.misc.GameData
import com.example.betapp.model.BetItem

class ViewmodelGrid2 : ViewModel(){
    private val _betList = MutableLiveData<ArrayList<BetItem>>()
    val betList: LiveData<ArrayList<BetItem>>
        get() = _betList
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
        val currentList = _betList.value ?: return
/*        if (position in 0 until currentList.size) {
            currentList[position] = newValue
            _betList.value = currentList
        }*/
        for(i in 0..currentList.size-1){
            val item=currentList[i]
            if(item.number.equals(position.toString()))
            {
                currentList[i]=newValue
            }
        }
    }
    private val _fromtopair = MutableLiveData<Pair<Int,Int>>()
    val fromtopair: LiveData<Pair<Int, Int>>
        get() = _fromtopair


    fun updateFrom_to(from: Int,to:Int) {
        _fromtopair.value=Pair(from,to)

        Log.d("fromvalue","${_fromtopair.value}")
    }



    /*    fun getBetListInRange(): List<BetItem> {
            val fromValue = _from.value ?: 0
            val toValue = _to.value ?: 0
            Log.d("vm_from_to",fromValue.toString())
            val currentBetList = _betList.value ?: emptyList()
            if (fromValue >= 0 && toValue < currentBetList.size && fromValue <= toValue) {
                return currentBetList.subList(fromValue, toValue + 1)
            }
            return emptyList()
        }*/


    init {
        populateBetList()
    }
    fun total():String{
        var total=0
        _betList.value!!.forEach { total += it.amount.toInt() }
        return total.toString()
    }
    fun populateBetList() {
        val demoData = ArrayList<BetItem>()
        val data= GameData().DoubleList()
        for (i in data) {
            demoData.add(BetItem(0, i.toString()))
        }
        _betList.value = demoData
    }

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

    // Function to update the betList


}