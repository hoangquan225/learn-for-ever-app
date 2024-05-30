package com.example.mylap.models;

public class User {
    private String _id;
    private String account;
    private String name;
    private String avatar;
    private String email;
    private String phoneNumber;
    private long birth;
    private int gender;
    private long registerDate;
    private int status;

    public User(String _id, String account, String name, String username, String avatar, String email, String phoneNumber, int birth, int gender, long registerDate, int status) {
        this._id = _id;
        this.account = account;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.registerDate = registerDate;
        this.status = status;
    }

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(long registerDate) {
        this.registerDate = registerDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


