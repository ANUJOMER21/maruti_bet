
package com.example.betapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Transaction {
    public String getCurrent_ba() {
        return current_ba;
    }

    public void setCurrent_ba(String current_ba) {
        this.current_ba = current_ba;
    }

    @SerializedName("current_balance")
@Expose
private String current_ba;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("gameSubmission")
    @Expose
    private GameSubmission gameSubmission;

    public String getId() {
        return id;
    }

    public GameSubmission getGameSubmission() {
        return gameSubmission;
    }

    public void setGameSubmission(GameSubmission gameSubmission) {
        this.gameSubmission = gameSubmission;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
