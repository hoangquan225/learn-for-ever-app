package com.example.mylap.page.resetPwd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.page.auth.LoginActivity;
import com.example.mylap.page.auth.RegisterActivity;
import com.example.mylap.responsive.LoginRes;
import com.example.mylap.responsive.ResetPass;
import com.example.mylap.utils.ProgressDialogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_word);
        EditText etAccount = findViewById(R.id.etAccount);
        Button btnSendMail = findViewById(R.id.btnSendMail);
        Button btnBack = findViewById(R.id.btnBack);
        TextView showInfo = findViewById(R.id.showinfo);
        EditText etcode = findViewById(R.id.code);
        EditText etNewPwd = findViewById(R.id.newPwd);
        EditText etConfirmNewPwd = findViewById(R.id.confirmNewPwd);
        Button btnResetPass = findViewById(R.id.btnResetPass);

        ConfigApi configApi = new ConfigApi();
        ProgressDialog progressDialog = ProgressDialogUtils.createProgressDialog(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPassWord.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String accountOrEmail = etAccount.getText().toString().trim();
                configApi.getApiService().sendCodeResetPass(accountOrEmail, accountOrEmail).enqueue(new Callback<ResetPass>() {
                    @Override
                    public void onResponse(Call<ResetPass> call, Response<ResetPass> response) {
                        int status = response.body().getStatus();
                        showInfo.setVisibility(View.VISIBLE);
                        if(status == 0) {
                            etcode.setVisibility(View.VISIBLE);
                            etNewPwd.setVisibility(View.VISIBLE);
                            etConfirmNewPwd.setVisibility(View.VISIBLE);
                            btnResetPass.setVisibility(View.VISIBLE);
                            showInfo.setText("vui lòng check email và nhập mã code được gửi về email của bạn và bấm nút tiếp tục");
                            btnSendMail.setText("Gửi lại");
                        } else {
                            etcode.setVisibility(View.GONE);
                            etNewPwd.setVisibility(View.GONE);
                            etConfirmNewPwd.setVisibility(View.GONE);
                            btnResetPass.setVisibility(View.GONE);
                            btnSendMail.setText("Tiếp Tục");
                            if(status == 2) {
                                showInfo.setText("email hoặc account bạn nhập không tồn tại");
                            } else {
                                Toast.makeText(getApplicationContext(), "lỗi, Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResetPass> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "error api:  " + t);
                    }
                });
            }
        });

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPwd = etNewPwd.getText().toString().trim();
                String confirmNewpwd = etConfirmNewPwd.getText().toString().trim();
                String codeReset = etcode.getText().toString().trim();
                String accountOrEmail = etAccount.getText().toString().trim();
                if(!newPwd.equals(confirmNewpwd)) {
                    Toast.makeText(getApplicationContext(), "mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }
                progressDialog.show();
                configApi.getApiService().resetPassWord(accountOrEmail, accountOrEmail, newPwd, codeReset).enqueue(new Callback<ResetPass>() {
                    @Override
                    public void onResponse(Call<ResetPass> call, Response<ResetPass> response) {

                        int status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if(status != 0) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công , hãy đăng nhập ngay", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPassWord.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResetPass> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "error api:  " + t);
                    }
                });
            }
        });

    }
}