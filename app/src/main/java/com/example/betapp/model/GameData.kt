package com.example.betapp.model

data class GameDatas(
    val marketId: Int,
    val gameId: Int,
    val userId: Int,
    val gameData: String,
    val totalAmount: Double,
    val transactionType: String,
    val transactionNarration: String,
    val session:String
)

