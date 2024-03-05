package com.example.betapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameSubmission {

    @SerializedName("marketId")
    @Expose
    private String marketId;
    @SerializedName("gameId")
    @Expose
    private String gameId;
    @SerializedName("gameData")
    @Expose
    private String gameData;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("submissionTime")
    @Expose
    private String submissionTime;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("marketName")
    @Expose
    private String marketname;
    @SerializedName("gameName")
    @Expose
    private String gamename;

    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    public String getGamename() {
        return gamename.toString();
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameData() {
        return gameData;
    }

    public void setGameData(String gameData) {
        this.gameData = gameData;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

}
