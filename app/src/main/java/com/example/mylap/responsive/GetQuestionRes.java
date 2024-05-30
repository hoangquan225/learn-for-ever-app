package com.example.mylap.responsive;

import com.example.mylap.models.question.Question;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetQuestionRes {
    @SerializedName("data")
    private List<Question> data;

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public List<Question> getData() {
        return data;
    }
}
