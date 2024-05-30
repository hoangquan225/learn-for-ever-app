package com.example.mylap.page.listCourse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.models.Course;
import com.example.mylap.responsive.GetListCourse;
import com.example.mylap.singleton.AuthManager;
import com.example.mylap.utils.ProgressDialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCourse extends AppCompatActivity {
    private ConfigApi configApi = new ConfigApi();
    private List<Course> courseList;
    private ListCourseAdapter courseAdapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);
        TextView textView = findViewById(R.id.textHeader);

        courseList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        courseAdapter = new ListCourseAdapter(courseList, this);
        recyclerView.setAdapter(courseAdapter);


        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        String categoryName = intent.getStringExtra("categoryName");

        Log.d("TAG", "user Id: " + AuthManager.getInstance().getUserId());

        textView.setText(textView.getText().toString() + " " + categoryName);

        progressDialog = new ProgressDialogUtils().createProgressDialog(this);
        progressDialog.show();

//        MyAsyncTask myAsyncTask = new MyAsyncTask();
//        myAsyncTask.execute(categoryId);

        Call<GetListCourse> call = configApi.getApiService().getListCourseByCategoryId(categoryId);
        try {
            Response<GetListCourse> response = call.execute();
            progressDialog.dismiss();
            if (response.isSuccessful()) {
                GetListCourse data = response.body();
                if (data != null) {
                    for (int i = 0; i < data.getData().size(); i++) {
                        courseList.add(data.getData().get(i));
                    }
                    courseAdapter.notifyDataSetChanged();
                }
            } else {
                Log.d("TAG", "error");
            }
        } catch (Throwable e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

        configApi.getApiService().getListCourseByCategoryId(categoryId).enqueue(new Callback<GetListCourse>() {
            @Override
            public void onResponse(Call<GetListCourse> call, Response<GetListCourse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetListCourse data = response.body();
                    for (int i = 0; i < data.getData().size(); i++) {
                        courseList.add(data.getData().get(i));
                    }
                    courseAdapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "error");
                    // Xử lý lỗi khi response không thành
                }
            }

            @Override
            public void onFailure(Call<GetListCourse> call, Throwable t) {
                // Xử lý lỗi khi request thất bại
                Log.d("TAG", "error api:  " + t);
                progressDialog.dismiss();
            }
        });

    }

    // AsyncTask với ba tham số: Params, Progress, Result
//    private class MyAsyncTask extends AsyncTask<String, String, List<Course>> {
//
//        @Override
//        protected List<Course> doInBackground(String ...categoryId) {
//            Log.d("TAG", "doInBackground: ");
//            Log.d("TAG", "categoryId[0]: " + categoryId[0]);
//
//            List<Course> listCourse = new ArrayList<>();
//            Call<GetListCourse> call = configApi.getApiService().getListCourseByCategoryId(categoryId[0]);
//            try {
//                Response<GetListCourse> response = call.execute();
//                if (response.isSuccessful()) {
//                    GetListCourse data = response.body();
//                    if (data != null) {
//                        for (int i = 0; i < data.getData().size(); i++) {
//                            Log.d("TAG", "name course : " + data.getData().get(i).getCourseName());
//                            listCourse.add(data.getData().get(i));
//                        }
//                    }
//                } else {
//                    Log.d("TAG", "error");
//                }
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            Log.d("TAG", "listCourse: " + listCourse.size());
//            return listCourse;
//
//        }
//
//        @Override
//        protected void onPostExecute(List<Course> result) {
//            // Xử lý kết quả sau khi tải xong
//            Log.d("TAG", "onPostExecute: ");
//            progressDialog.dismiss();
//            for (int i = 0; i < result.size(); i++) {
//                Log.d("TAG", "name : " + result.get(i).getCourseName());
//                courseList.add(result.get(i));
//            }
//            courseAdapter.notifyDataSetChanged();
//        }
//    }
}