package com.example.mylap.models;

public class Exam {
    private String _id;
    private String id;
    private String courseName;
    private String avatar;
    private String des;
    private String shortDes;
    private String slug;
    private int status;

    public Exam() {
    }

    public Exam(String id, String _id, String courseName, String avatar, String des, String shortDes, String slug, int status) {
        this._id = _id;
        this.courseName = courseName;
        this.avatar = avatar;
        this.des = des;
        this.shortDes = shortDes;
        this.slug = slug;
        this.status = status;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
