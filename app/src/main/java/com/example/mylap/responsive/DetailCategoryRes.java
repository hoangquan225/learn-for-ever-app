package com.example.mylap.responsive;

import com.example.mylap.models.Course;

import java.util.List;

public class DetailCategoryRes {
    private List<Course> course;

    public DetailCategoryRes() {};

    public DetailCategoryRes(List<Course> course) {
        this.course = course;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }
}
