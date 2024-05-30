package com.example.mylap.page.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.page.home.HomeActivity;
import com.example.mylap.page.resetPwd.ResetPassWord;
import com.example.mylap.responsive.LoginRes;
import com.example.mylap.responsive.ResetPass;
import com.example.mylap.singleton.AuthManager;
import com.example.mylap.utils.ProgressDialogUtils;
import com.example.mylap.utils.SharedPreferencesUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnReRegister;
    private Button btnResetPass;
    ConfigApi configApi = new ConfigApi();
    private Context loginContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.loginContext = this;

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnReRegister = findViewById(R.id.btnReRegister);
        btnResetPass = findViewById(R.id.btnResetPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                ProgressDialog progressDialog = ProgressDialogUtils.createProgressDialog(loginContext);
                progressDialog.show();
                configApi.getApiService().login(username, password, username).enqueue(new Callback<LoginRes>() {
                    @Override
                    public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getToken();
                            if(token.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            } else if(token.equals("-1")) {
                                Toast.makeText(getApplicationContext(), "Không tồn tại tài khoản", Toast.LENGTH_SHORT).show();
                            } else if(!token.equals("")) {
                                SharedPreferencesUtils.saveStringToSharedPreferences(loginContext,"token", token);
                                SharedPreferencesUtils.saveStringToSharedPreferences(loginContext, "userId", response.body().getId());

                                AuthManager.getInstance().setLoginStatus(true);
                                AuthManager.getInstance().setUserId(response.body().getId());
                                AuthManager.getInstance().setAuthToken(token);

                                Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(loginContext, HomeActivity.class);
                                loginContext.startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Log.d("TAG", "error");
                            Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRes> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "error api:  " + t);
                    }
                });


            }

        });
        btnReRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPassWord.class);
                startActivity(intent);
                finish();
            }
        });

    }
}