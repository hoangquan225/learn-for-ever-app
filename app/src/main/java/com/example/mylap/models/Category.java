package com.example.mylap.models;

public class Category {
    private String _id;
    private String name;
    private int status;
    private String avatar;
    private String des;
    private int index;
    private String slug;
    private Long createDate;
    private Long updateDate;

    public Category() {
    }

    public Category(String _id, String name, int status, String avatar, String des, int index, String slug, Long createDate, Long updateDate) {
        this._id = _id;
        this.name = name;
        this.status = status;
        this.avatar = avatar;
        this.des = des;
        this.index = index;
        this.slug = slug;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
}
