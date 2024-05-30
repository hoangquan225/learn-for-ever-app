package com.example.mylap.singleton;

import androidx.lifecycle.MutableLiveData;

public class AuthManager {
    private static AuthManager instance;
    private MutableLiveData<Boolean> isLoginLiveData = new MutableLiveData<>();
    private String userId;
    private String token;

    private AuthManager() {
        // Khởi tạo trạng thái đăng nhập mặc định
        isLoginLiveData.setValue(false);
        this.token = "";
    }

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public MutableLiveData<Boolean> getIsLoginLiveData() {
        return isLoginLiveData;
    }

    public void setLoginStatus(boolean status) {
        isLoginLiveData.setValue(status);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAuthToken(String token) {
        this.token = token;
    }

    public String getAuthToken() {
        return token;
    }

    public void clearAuthToken() {
        this.token = null;
    }
}
