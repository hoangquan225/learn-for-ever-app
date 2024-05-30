package com.example.mylap.page.userInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.models.User;
import com.example.mylap.responsive.UpdateUserRes;
import com.example.mylap.utils.ProgressDialogUtils;
import com.example.mylap.utils.SharedPreferencesUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfo extends AppCompatActivity {

    private TextView tvEmail;
    private TextView tvUser;
    private TextView tvNumber;
    private RadioGroup radioGioiTinh;

    private Button btnFix;
    private Button btnExit;
    private Context userinfoContext;
    private ConfigApi configApi = new ConfigApi();

    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        this.userinfoContext = this;

        tvUser = findViewById(R.id.tvUser);
        tvEmail = findViewById(R.id.tvEmail);
        tvNumber = findViewById(R.id.tvNumber);
        radioGioiTinh = findViewById(R.id.radioGioiTinh);
        btnFix = findViewById(R.id.btnFix);
        btnExit = findViewById(R.id.btnExit);

        int idGenderMale = R.id.radioButtonMale;
        int idGenderFemale = R.id.radioButtonFemale;


        String token = SharedPreferencesUtils.getStringToSharedPreferences(this, "token");
        // api get category
        ProgressDialog progressDialog = ProgressDialogUtils.createProgressDialog(userinfoContext);
        progressDialog.show();

        configApi.getApiService().getUserInfo(token).enqueue(new Callback<User>() {

            // Gửi yêu cầu lấy thông tin người dùng
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        // Gán thông tin người dùng lên các thành phần hiển thị
                        idUser = user.get_id();
                        tvUser.setText(user.getName());
                        tvEmail.setText(user.getEmail());
                        tvNumber.setText(user.getPhoneNumber());

                        if(user.getGender() == 1) {
                            radioGioiTinh.check(R.id.radioButtonMale);
                        } else if(user.getGender() == 2) {
                            radioGioiTinh.check(R.id.radioButtonFemale);
                        }

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
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý lỗi khi request thất bại
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error api:  " + t);
            }
        });

        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gender;
                if(radioGioiTinh.getCheckedRadioButtonId() == idGenderMale) {
                    gender = 1;
                } else {
                    gender = 2;
                }
                progressDialog.show();
                configApi.getApiService().updateUser(idUser, tvEmail.getText().toString(), tvUser.getText().toString(), tvNumber.getText().toString(), gender).enqueue(new Callback<UpdateUserRes>() {
                    @Override
                    public void onResponse(Call<UpdateUserRes> call, Response<UpdateUserRes> response) {
                        progressDialog.dismiss();
                        int status = response.body().getStatus();
                        if(status == 0) {
                            // success
                            Toast.makeText(getApplicationContext(), "Update thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserRes> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // trở về activity trước đó
            }
        });
    }
}