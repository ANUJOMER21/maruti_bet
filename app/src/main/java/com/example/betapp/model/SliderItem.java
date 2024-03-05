package com.example.betapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderItem {
    @SerializedName("sliderId")
    @Expose
    private String sliderId;
    @SerializedName("sliderImage")
    @Expose
    private String sliderImage;

    public String getSliderId() {
        return sliderId;
    }

    public void setSliderId(String sliderId) {
        this.sliderId = sliderId;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }
}
