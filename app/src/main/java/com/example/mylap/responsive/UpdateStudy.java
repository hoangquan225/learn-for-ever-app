package com.example.mylap.responsive;

import com.google.gson.annotations.SerializedName;

public class UpdateStudy {
    @SerializedName("status")
    private int status;
    @SerializedName("score")
    private double score;
    @SerializedName("numCorrect")
    private int numCorrect;
    @SerializedName("numIncorrect")
    private int numIncorrect;

    public int getStatus() {
        return status;
    }

    public double getScore() {
        return score;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public int getNumIncorrect() {
        return numIncorrect;
    }
}
