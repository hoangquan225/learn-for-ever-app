package com.example.mylap.responsive;

import com.example.mylap.models.Course;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class CourseDetailRes {
    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private Course data;

    public Course getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
