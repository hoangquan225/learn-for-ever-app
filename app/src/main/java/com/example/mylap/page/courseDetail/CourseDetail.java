package com.example.mylap.page.courseDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.models.Course;
import com.example.mylap.page.topicExam.TopicExam;
import com.example.mylap.page.topicLearn.TopicLearn;
import com.example.mylap.responsive.CountLearnRes;
import com.example.mylap.responsive.CourseDetailRes;
import com.example.mylap.utils.ProgressDialogUtils;
import com.squareup.picasso.Picasso;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetail extends AppCompatActivity {

    TextView titleCourse;
    TextView desCourse;
    TextView numCourse;
    TextView numExam;
    TextView shortDesCourse;
    ImageView imageCourse;
    Button btn_learn;
    Button btn_exam;
    Course courseData = new Course();

    ConfigApi configApi = new ConfigApi();

    final CountDownLatch latch = new CountDownLatch(1);

    Thread thread1;
    Thread thread2;

//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case 0:
////                    String str = (String) msg.obj;
//                    thread2.start();
//                    break;
//            }
//            return true;
//        }
//    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");

        // map element
        titleCourse = findViewById(R.id.titleCourse);
        desCourse = findViewById(R.id.desCourse);
        numCourse = findViewById(R.id.numCourse);
        numExam = findViewById(R.id.numExam);
        shortDesCourse = findViewById(R.id.shortDesCourse);
        imageCourse = findViewById(R.id.imageCourse);
        btn_learn = findViewById(R.id.btn_learn);
        btn_exam = findViewById(R.id.btn_exam);

        AtomicInteger completedAPICalls = new AtomicInteger(0);

        ProgressDialog progress = ProgressDialogUtils.createProgressDialog(this);
        progress.show();

        // call api get course detail by id course
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "Task one ");
                configApi.getApiService().getCourseById(courseId).enqueue(new Callback<CourseDetailRes>() {
                    @Override
                    public void onResponse(Call<CourseDetailRes> call, Response<CourseDetailRes> response) {
                        Log.d("TAG", "Task one successful");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                latch.countDown();
                                if (response.isSuccessful() && response.body().getStatus() == 0) {
                                    courseData = response.body().getData();
//                            Message message = handler.obtainMessage(0, )
//                            handler.sendMessage(message);
//                                    handler.sendEmptyMessage(0);
                                    titleCourse.setText(courseData.getCourseName());
                                    shortDesCourse.setText(courseData.getShortDes());
                                    Picasso.get().load(courseData.getAvatar()).into(imageCourse);

                                }
                                completedAPICalls.incrementAndGet();
                                CheckCompletedCallApi(completedAPICalls, progress);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CourseDetailRes> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                completedAPICalls.incrementAndGet();
                                CheckCompletedCallApi(completedAPICalls, progress);
                                Toast.makeText(getApplicationContext(), "Server bị lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("TAG", "error api:  " + t);
                    }
                });
            }
        });

        Log.d("TAG", "main thread !");

        // call api get total learn and exam
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "Task two ");
                configApi.getApiService().countTopicInCourse(courseId).enqueue(new Callback<CountLearnRes>() {
                    @Override
                    public void onResponse(Call<CountLearnRes> call, Response<CountLearnRes> response) {
                        Log.d("TAG", "Task two successful");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful() && response.body().getStatus() == 0) {
                                    numCourse.setText("Tổng số bài học: " + response.body().getData().getTotalLearn());
                                    numExam.setText("Tổng đề kiểm tra: " + response.body().getData().getTotalExam());
                                }
                                completedAPICalls.incrementAndGet();
                                CheckCompletedCallApi(completedAPICalls, progress);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CountLearnRes> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                completedAPICalls.incrementAndGet();
                                CheckCompletedCallApi(completedAPICalls, progress);
                                Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("TAG", "error api:  " + t);
                    }
                });
            }
        });

        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetail.this, TopicLearn.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("type", 1);
                intent.putExtra("courseName", courseData.getCourseName());
//                Log.d("TAG", "courseData.getCourseName(): " + courseData.getCourseName());
                CourseDetail.this.startActivity(intent);
            }
        });

        btn_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetail.this, TopicExam.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("type", 2);
                intent.putExtra("courseName", courseData.getCourseName());
//                Log.d("TAG", "courseData.getCourseName(): " + courseData.getCourseName());
                CourseDetail.this.startActivity(intent);
            }
        });

        thread1.start();
        thread2.start();
    }

    private void CheckCompletedCallApi(AtomicInteger count, ProgressDialog progressDialog) {
        if (count.get() == 2) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}