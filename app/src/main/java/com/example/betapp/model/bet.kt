package com.example.betapp.model

data class BetItem (
    val amount:Number,
    val number:String

){
    override fun toString(): String {
        return "number :$number amount $amount"
    }
}