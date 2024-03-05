
package com.example.betapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserGameSubmission {

    @SerializedName("marketId")
    @Expose
    private String marketId;
    @SerializedName("gameId")
    @Expose
    private String gameId;
    @SerializedName("gameName")
    @Expose
    private String gameName;
    @SerializedName("marketName")
    @Expose

    private String marketname;
    @SerializedName("session")
    @Expose

    private String session;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("gameData")
    @Expose
    private String gameData;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("submissionTime")
    @Expose
    private String submissionTime;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

}
