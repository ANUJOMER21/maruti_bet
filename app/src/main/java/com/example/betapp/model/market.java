package com.example.betapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class market {


    public Object getMarketClosePatti() {
        return marketClosePatti;
    }

        @SerializedName("marketId")
        @Expose
        private String marketId;
        @SerializedName("marketName")
        @Expose
        private String marketName;
        @SerializedName("marketOpenTime")
        @Expose
        private String marketOpenTime;
        @SerializedName("marketCloseTime")
        @Expose
        private String marketCloseTime;
        @SerializedName("open_session_open_time")
        @Expose
        private String openSessionOpenTime;
        @SerializedName("open_session_close_time")
        @Expose
        private String openSessionCloseTime;
        @SerializedName("close_session_open_time")
        @Expose
        private String closeSessionOpenTime;
        @SerializedName("close_session_close_time")
        @Expose
        private String closeSessionCloseTime;
        @SerializedName("marketOpenAkar")
        @Expose
        private Object marketOpenAkar;
        @SerializedName("marketCloseAkar")
        @Expose
        private Object marketCloseAkar;
        @SerializedName("marketOpenPatti")
        @Expose
        private Object marketOpenPatti;
        @SerializedName("marketClosePatti")
        @Expose
        private Object marketClosePatti;
        @SerializedName("marketTodayOpenNumber")
        @Expose
        private String marketTodayOpenNumber;

        public String getMarketId() {
            return marketId;
        }

        public void setMarketId(String marketId) {
            this.marketId = marketId;
        }

        public String getMarketName() {
            return marketName;
        }

        public void setMarketName(String marketName) {
            this.marketName = marketName;
        }

        public String getMarketOpenTime() {
            return marketOpenTime;
        }

        public void setMarketOpenTime(String marketOpenTime) {
            this.marketOpenTime = marketOpenTime;
        }

        public String getMarketCloseTime() {
            return marketCloseTime;
        }

        public void setMarketCloseTime(String marketCloseTime) {
            this.marketCloseTime = marketCloseTime;
        }

        public String getOpenSessionOpenTime() {
            return openSessionOpenTime;
        }

        public void setOpenSessionOpenTime(String openSessionOpenTime) {
            this.openSessionOpenTime = openSessionOpenTime;
        }

        public String getOpenSessionCloseTime() {
            return openSessionCloseTime;
        }

        public void setOpenSessionCloseTime(String openSessionCloseTime) {
            this.openSessionCloseTime = openSessionCloseTime;
        }

        public String getCloseSessionOpenTime() {
            return closeSessionOpenTime;
        }

        public void setCloseSessionOpenTime(String closeSessionOpenTime) {
            this.closeSessionOpenTime = closeSessionOpenTime;
        }

        public String getCloseSessionCloseTime() {
            return closeSessionCloseTime;
        }

        public void setCloseSessionCloseTime(String closeSessionCloseTime) {
            this.closeSessionCloseTime = closeSessionCloseTime;
        }

        public Object getMarketOpenAkar() {
            return marketOpenAkar;
        }

        public void setMarketOpenAkar(Object marketOpenAkar) {
            this.marketOpenAkar = marketOpenAkar;
        }

        public Object getMarketCloseAkar() {
            return marketCloseAkar;
        }

        public void setMarketCloseAkar(Object marketCloseAkar) {
            this.marketCloseAkar = marketCloseAkar;
        }

        public Object getMarketOpenPatti() {
            return marketOpenPatti;
        }

        public void setMarketOpenPatti(Object marketOpenPatti) {
            this.marketOpenPatti = marketOpenPatti;
        }



        public void setMarketClosePatti(Object marketClosePatti) {
            this.marketClosePatti = marketClosePatti;
        }




    public void setMarketOpenPatti(String marketOpenPatti) {
        this.marketOpenPatti = marketOpenPatti;
    }



    public void setMarketClosePatti(String marketClosePatti) {
        this.marketClosePatti = marketClosePatti;
    }

    public String getMarketTodayOpenNumber() {
        return marketTodayOpenNumber;
    }

    public void setMarketTodayOpenNumber(String marketTodayOpenNumber) {
        this.marketTodayOpenNumber = marketTodayOpenNumber;
    }
}
