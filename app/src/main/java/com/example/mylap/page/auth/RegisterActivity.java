package com.example.mylap.page.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.page.home.HomeActivity;
import com.example.mylap.responsive.LoginRes;
import com.example.mylap.singleton.AuthManager;
import com.example.mylap.utils.ProgressDialogUtils;
import com.example.mylap.utils.SharedPreferencesUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etUser;
    private EditText etNumber;
    private EditText etPassword;
    private EditText etRePassword;
    private RadioGroup radioGroupGender;
    private Button btnRegister;
    private Button btnReLogin;
    ConfigApi configApi = new ConfigApi();
    private Context registerContext;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            this.registerContext = this;


            etUsername = findViewById(R.id.etUsername);
            etEmail = findViewById(R.id.etEmail);
            etUser = findViewById(R.id.etUser);
            etNumber = findViewById(R.id.etNumber);
            etPassword = findViewById(R.id.etPassword);
            etRePassword = findViewById(R.id.etRePassword);
            radioGroupGender = findViewById(R.id.radioGioiTinh);
            btnRegister = findViewById(R.id.btnRegister);
            btnReLogin = findViewById(R.id.btnReLogin);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String user = etUser.getText().toString();
                    String phoneNumber = etNumber.getText().toString();
                    String password = etPassword.getText().toString();
                    String repassword = etRePassword.getText().toString();

                    int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                    RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
                    String gender = "";
                    if(selectedGenderRadioButton != null) {
                        gender = selectedGenderRadioButton.getTag().toString();
                    }

                    if (!isValidEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (!isValidphoneNumber(phoneNumber)) {
//                        Toast.makeText(getApplicationContext(), "SĐT Không Hợp Lệ", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    if(username.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Nhập Tên Đăng Nhập", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(email.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Nhập Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(user.equals("")){
                        Toast.makeText(getApplicationContext(), "Mời Nhập Tên Người Dùng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(phoneNumber.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Nhập Số Điện Thoại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(gender.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Chọn Giới Tính", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(password.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(repassword.equals("")) {
                        Toast.makeText(getApplicationContext(), "Mời Nhập Lại Mật Khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password.equals(repassword)) {
                        Toast.makeText(getApplicationContext(),"Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // Kiểm tra dữ liệu và xử lý đăng ký tại đây
//                    Log.d("TAG", "username : " + username + " pwd: " + password + " email : " + email  + "use ; " + user + " number : " + phoneNumber + " gender : " + gender);
                    ProgressDialog progressDialog = ProgressDialogUtils.createProgressDialog(registerContext);
                    progressDialog.show();
                    configApi.getApiService().register(username, password, user, email, phoneNumber, Integer.parseInt(gender)).enqueue(new Callback<LoginRes>() {
                        @Override
                        public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                            if (response.isSuccessful()) {
                                int status = response.body().getStatus();
                                if(status == 0) {
                                    String token = response.body().getToken();

                                    if(!token.equals("")) {
                                        SharedPreferencesUtils.saveStringToSharedPreferences(registerContext,"token", token);
                                        SharedPreferencesUtils.saveStringToSharedPreferences(registerContext, "userId", response.body().getId());

                                        AuthManager.getInstance().setLoginStatus(true);
                                        AuthManager.getInstance().setUserId(response.body().getId());
                                        AuthManager.getInstance().setAuthToken(token);

                                        Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(registerContext, HomeActivity.class);
                                        registerContext.startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "server có lỗi , vui lòng thao tác lại", Toast.LENGTH_SHORT).show();
                                    }

                                } else if(status == 1) {
                                    Toast.makeText(getApplicationContext(), "email hoặc tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Log.d("TAG", "error");
                                Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                                // Xử lý lỗi khi response không thành
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRes> call, Throwable t) {
                            // Xử lý lỗi khi request thất bại
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "error api:  " + t);
                        }
                    });

                }
            });
        btnReLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        }



    private boolean isValidphoneNumber(String phoneNumber) {
        String phonePattern = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

        // Tạo một đối tượng Pattern để biên dịch biểu thức chính quy
        Pattern pattern = Pattern.compile(phonePattern);

        // Tạo một đối tượng Matcher để so sánh chuỗi với biểu thức chính quy
        Matcher matcher = pattern.matcher(phoneNumber);

        // Kiểm tra xem chuỗi phù hợp với biểu thức chính quy hay không
        return matcher.matches();
    }


    // Hàm kiểm tra email có đúng định dạng hay không
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+\\@gmail.com+";
        return email.matches(emailPattern);
    }
    }


