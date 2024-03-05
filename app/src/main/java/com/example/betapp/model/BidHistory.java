
package com.example.betapp.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BidHistory {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userGameSubmissions")
    @Expose
    private List<UserGameSubmission> userGameSubmissions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserGameSubmission> getUserGameSubmissions() {
        return userGameSubmissions;
    }

    public void setUserGameSubmissions(List<UserGameSubmission> userGameSubmissions) {
        this.userGameSubmissions = userGameSubmissions;
    }

}
