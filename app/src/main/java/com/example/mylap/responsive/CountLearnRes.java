package com.example.mylap.responsive;

import com.google.gson.annotations.SerializedName;

public class CountLearnRes {

    public class DataCountLearn {
        @SerializedName("totalLearn")
        private int totalLearn;

        @SerializedName("totalExam")
        private int totalExam;

        public int getTotalLearn() {
            return totalLearn;
        }

        public int getTotalExam() {
            return totalExam;
        }
    }
//    public CountLearnRes(DataCountLearn data, int status) {
//        this.data = data;
//        this.status = status;
//    }

    @SerializedName("data")
    private DataCountLearn data;

    @SerializedName("status")
    private int status;

    public DataCountLearn getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

}
