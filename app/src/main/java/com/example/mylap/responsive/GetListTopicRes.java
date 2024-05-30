package com.example.mylap.responsive;

import com.example.mylap.models.topic.Topic;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetListTopicRes {
    @SerializedName("data")
    private ArrayList<Topic> data;

    @SerializedName("status")
    private int status;

    @SerializedName("topicChildData")
    private ArrayList<Topic> topicChildData;

    public ArrayList<Topic> getTopicChildData() {
        return topicChildData;
    }

    public void setTopicChildData(ArrayList<Topic> topicChildData) {
        this.topicChildData = topicChildData;
    }

    public void setData(ArrayList<Topic> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GetListTopicRes(ArrayList<Topic> data, int status) {
        this.data = data;
        this.status = status;
    }

    public ArrayList<Topic> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
