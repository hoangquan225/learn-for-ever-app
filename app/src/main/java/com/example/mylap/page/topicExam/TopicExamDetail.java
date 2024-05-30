package com.example.mylap.page.topicExam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.models.question.Question;
import com.example.mylap.page.topicExam.adapterQuestion.QuestionExamAdapter;
import com.example.mylap.responsive.GetQuestionRes;
import com.example.mylap.responsive.ResetPass;
import com.example.mylap.responsive.UpdateStudy;
import com.example.mylap.singleton.AuthManager;
import com.example.mylap.utils.ProgressDialogUtils;
import com.example.mylap.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
public class TopicExamDetail extends AppCompatActivity  {
    private Context activity;
    ConfigApi configApi = new ConfigApi();

    private QuestionExamAdapter questionExamAdapter;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_exam_detail);
        submitButton = findViewById(R.id.btn_submit);
        Button backButton = findViewById(R.id.btn_back);
        activity = this;

        // get itent
        Intent intent = getIntent();
        String topicId = intent.getStringExtra("topicId");
        int timeExam = intent.getIntExtra("timeExam", 0);
        int numQuestion = intent.getIntExtra("numQuestion", 0);
        String name = intent.getStringExtra("name");

        if (timeExam == 0 || topicId == null) {
            return;
        }
        ProgressDialog progress = ProgressDialogUtils.createProgressDialog(this);
        progress.show();
        // get element
        RecyclerView recyclerView = findViewById(R.id.recyclerView_listQuestion);
        new Thread(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
                TextView topic_name = findViewById(R.id.topic_name);
                topic_name.setText(name);
                configApi.getApiService().getQuestion(topicId).enqueue(new Callback<GetQuestionRes>() {
                    @Override
                    public void onResponse(Call<GetQuestionRes> call, Response<GetQuestionRes> response) {
                        progress.dismiss();
                        List<Question> questions = response.body().getData();
//                        Collections.sort(questions);
                        if(questions.size() > 0) {
                            questionExamAdapter = new QuestionExamAdapter(activity, questions);
                            recyclerView.setAdapter(questionExamAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        } else {
                            Toast.makeText(getApplicationContext(), "Không có câu hỏi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<GetQuestionRes> call, Throwable t) {
                        progress.dismiss();
                        Log.d("TAG", "onFailure: " + t);
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButton.getText().equals("Làm lại")) {
                    resetQuiz();
                    backButton.setVisibility(View.GONE);
                } else {
                    progress.show();
                    if (questionExamAdapter != null) {
                        Map<String, String> selectedAnswers = questionExamAdapter.getSelectedAnswers();
                        String questionAnswers = gson.toJson(selectedAnswers);
//                        String idUser = AuthManager.getInstance().getUserId();
                        String idUser = SharedPreferencesUtils.getStringToSharedPreferences(activity, "userId");
                        String courseId = SharedPreferencesUtils.getStringToSharedPreferences(activity, "courseId");
                        double timeStudy = 0.5;
                        Log.d("TAG",
                            "idUser: " + idUser + "\n" +
                                    "courseId: " + courseId + "\n" +
                                    "topicId: " + topicId + "\n"
                                );
                        configApi.getApiService().updateStudy(topicId, idUser, courseId, timeStudy, questionAnswers).enqueue(new Callback<UpdateStudy>() {
                            @Override
                            public void onResponse(Call<UpdateStudy> call, Response<UpdateStudy> response) {
                                int status = response.body().getStatus();
                                if(status == 0) {
                                    double score = response.body().getScore();
                                    int numCorrect = response.body().getNumCorrect();
                                    int numIncorrect = response.body().getNumIncorrect();
                                    showResultDialog(score, numCorrect, numIncorrect);
                                    submitButton.setText("Làm lại");
                                    backButton.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                                }
                                progress.dismiss();
                            }

                            @Override
                            public void onFailure(Call<UpdateStudy> call, Throwable t) {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "error api:  " + t);
                            }
                        });
                    }
                    progress.dismiss();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetQuiz() {
        submitButton = findViewById(R.id.btn_submit);
        questionExamAdapter.setReviewMode(false);
        questionExamAdapter.getSelectedAnswers().clear();
        questionExamAdapter.notifyDataSetChanged();
        submitButton.setText("Nộp bài");
    }

    private void showResultDialog(double score, int numCorrect, int numIncorrect) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Kết quả bài thi");
            builder.setMessage("Điểm: " + score + "\nSố câu đúng: " + numCorrect + "\nSố câu sai: " + numIncorrect);
            builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Xem lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    questionExamAdapter.setReviewMode(true);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception error) {
            System.out.println("Exception" + error.getMessage());
        }
    };

}