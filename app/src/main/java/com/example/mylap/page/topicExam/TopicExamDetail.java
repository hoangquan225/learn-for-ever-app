package com.example.mylap.page.topicExam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    Button backButton;

    String topicId;
    int timeExam;
    double timeStudy;
    String timeStudyText;

    CountDownTimer countDownTimer;
    TextView countdownText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_exam_detail);
        submitButton = findViewById(R.id.btn_submit);
        backButton = findViewById(R.id.btn_back);
        countdownText = findViewById(R.id.countdownText);
        activity = this;

        // get itent
        Intent intent = getIntent();
        topicId = intent.getStringExtra("topicId");
        timeExam = intent.getIntExtra("timeExam", 0);
        int numQuestion = intent.getIntExtra("numQuestion", 0);
        String name = intent.getStringExtra("name");

        if (timeExam == 0 || topicId == null) {
            return;
        }
        ProgressDialog progress = ProgressDialogUtils.createProgressDialog(this);
        progress.show();
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

        startCountdown(timeExam);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (submitButton.getText().equals("Làm lại")) {
                    resetQuiz();
                    backButton.setVisibility(View.GONE);
                } else {
                    progress.show();
                    if (questionExamAdapter != null) {
                        Map<String, String> selectedAnswers = questionExamAdapter.getSelectedAnswers();
                        String questionAnswers = gson.toJson(selectedAnswers);
                        String idUser = SharedPreferencesUtils.getStringToSharedPreferences(activity, "userId");
                        String courseId = SharedPreferencesUtils.getStringToSharedPreferences(activity, "courseId");
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
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onBackPressed();
    }

    private void startCountdown(int timeExam) {
        countDownTimer = new CountDownTimer(timeExam * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdownText.setText(String.format("%02d:%02d", minutes, seconds));
                long timeUsed = timeExam * 60 - minutes * 60 - seconds;
                timeStudyText = timeUsed / 60 + ":" + timeUsed % 60;
                timeStudy = timeExam - Math.round((minutes + seconds / 60.0) * 10.0) / 10.0;
            }

            public void onFinish() {
                handleSubmit();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }.start();
    }

    private void resetQuiz() {
        submitButton = findViewById(R.id.btn_submit);
        questionExamAdapter.setReviewMode(false);
        questionExamAdapter.getSelectedAnswers().clear();
        questionExamAdapter.notifyDataSetChanged();
        startCountdown(timeExam);
        submitButton.setText("Nộp bài");
    }

    private void handleSubmit() {
        ProgressDialog progress = ProgressDialogUtils.createProgressDialog(this);
        progress.show();
        if (questionExamAdapter != null) {
            Map<String, String> selectedAnswers = questionExamAdapter.getSelectedAnswers();
            String questionAnswers = gson.toJson(selectedAnswers);
            String idUser = SharedPreferencesUtils.getStringToSharedPreferences(activity, "userId");
            String courseId = SharedPreferencesUtils.getStringToSharedPreferences(activity, "courseId");
            configApi.getApiService().updateStudy(topicId, idUser, courseId, timeStudy, questionAnswers).enqueue(new Callback<UpdateStudy>() {
                @Override
                public void onResponse(Call<UpdateStudy> call, Response<UpdateStudy> response) {
                    int status = response.body().getStatus();
                    if (status == 0) {
                        double score = response.body().getScore();
                        int numCorrect = response.body().getNumCorrect();
                        int numIncorrect = response.body().getNumIncorrect();
                        showResultDialog(score, numCorrect, numIncorrect);
                        submitButton.setText("Làm lại");
                        if (backButton != null) {
                            backButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<UpdateStudy> call, Throwable t) {
                    if (progress != null) {
                        progress.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "error api:  " + t);
                }
            });
        }
    }

    private void showResultDialog(double score, int numCorrect, int numIncorrect) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Kết quả bài thi");
            builder.setMessage("Điểm: " + score +
                    "\nSố câu đúng: " + numCorrect +
                    "\nSố câu sai: " + numIncorrect +
                    "\nThời gian làm: " + timeStudyText);
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