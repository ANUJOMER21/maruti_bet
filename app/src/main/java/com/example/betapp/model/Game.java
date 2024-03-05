package com.example.betapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("gameId")
@Expose
private String gameId;
    @SerializedName("gameName")
    @Expose
    private String gameName;
    @SerializedName("multiplyAmount")
    @Expose
    private String multiplyAmount;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMultiplyAmount() {
        return multiplyAmount;
    }

    public void setMultiplyAmount(String multiplyAmount) {
        this.multiplyAmount = multiplyAmount;
    }
}
