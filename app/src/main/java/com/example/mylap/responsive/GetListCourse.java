package com.example.mylap.responsive;

import com.example.mylap.models.Category;
import com.example.mylap.models.Course;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetListCourse {
    @SerializedName("data")
    private ArrayList<Course> data;

    @SerializedName("status")
    private int status;

    public ArrayList<Course> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}

//public class GetListCourse {
//    private List<Course> data;
//    private Number status;
//
//    public GetListCourse(List<Course> data, Number status) {
//        this.data = data;
//        this.status = status;
//    }
//
//    public List<Course> getData() {
//        return data;
//    }
//
//    public void setData(List<Course> data) {
//        this.data = data;
//    }
//
//    public Number getStatus() {
//        return status;
//    }
//
//    public void setStatus(Number status) {
//        this.status = status;
//    }
//}