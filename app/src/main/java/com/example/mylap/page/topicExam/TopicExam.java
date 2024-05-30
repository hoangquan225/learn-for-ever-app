package com.example.mylap.page.topicExam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mylap.R;
import com.example.mylap.api.ConfigApi;
import com.example.mylap.models.topic.Topic;
import com.example.mylap.page.courseDetail.CourseDetail;
import com.example.mylap.page.topicLearn.CustomExpandableListAdapter;
import com.example.mylap.page.topicLearn.TopicViewModel;
import com.example.mylap.page.topicLearn.TypeGroupHeader;
import com.example.mylap.responsive.GetListTopicRes;
import com.example.mylap.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.mylap.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TopicExam extends AppCompatActivity  {

    private Context activity;
    private SensorManager sensorManager;

    private List<Topic> listTopics = new ArrayList<>();
    private List<Topic> listTopicChilds = new ArrayList<>();
    private TopicViewModel topicModel;
    ConfigApi configApi = new ConfigApi();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_exam);
        activity = this;
        topicModel = new ViewModelProvider(this).get(TopicViewModel.class);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Khởi tạo OrientationEventListener
//        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
//            @Override
//            public void onOrientationChanged(int orientation) {
//                if (orientation == ORIENTATION_UNKNOWN) return;
//
//                int screenOrientation = getWindowManager().getDefaultDisplay().getRotation();
//
//                if (screenOrientation == Surface.ROTATION_90 || screenOrientation == Surface.ROTATION_270) {
//                    // Thiết bị xoay ngang (landscape mode)
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//                } else {
//                    // Thiết bị ở chế độ dọc (portrait mode)
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//                }
//            }
//        };

        // get itent
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");
        int type = intent.getIntExtra("type", 0);
        String courseName = intent.getStringExtra("courseName");
        if (type == 0 || courseId == null) {
            return;
        }

        //map element
        ProgressDialog progress = ProgressDialogUtils.createProgressDialog(this);
        progress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userId = SharedPreferencesUtils.getStringToSharedPreferences(activity, "userId");
                configApi.getApiService().getTopicExamByCourse(courseId, 2, 1, userId).enqueue(new Callback<GetListTopicRes>() {
                    @Override
                    public void onResponse(Call<GetListTopicRes> call, Response<GetListTopicRes> response) {
                        progress.dismiss();
                        if (response.isSuccessful() && response.body().getStatus() == 0) {
                            ArrayList<Topic> topics = response.body().getData();
                            List<TypeGroupHeader> groupHeaders = new ArrayList<>();
                            Map<String, List<TypeGroupHeader>> childData = new HashMap<>();

                            for (int i = 0; i < topics.size(); i++) {
                                Topic topic = topics.get(i);
                                listTopics.add(topic);
                                if (topic.getParentId() == null) {
                                    groupHeaders.add(new TypeGroupHeader(topic.get_id(), topic.getName()));
                                } else {
                                    listTopicChilds.add(topic);
                                    List<TypeGroupHeader> childDatasInTopic = new ArrayList<>();

                                    if (childData.containsKey(topic.getParentId())) {
                                        childDatasInTopic = childData.get(topic.getParentId());
                                    }
                                    String score = Objects.isNull(topic.getScore()) ? "" : " - " + topic.getScore() + " Điểm";
                                    childDatasInTopic.add(new TypeGroupHeader(topic.get_id(), topic.getName()+score));
                                    childData.put(topic.getParentId(), childDatasInTopic);
                                }
                            }
                            updateMenu(courseName, groupHeaders, childData, listTopics);
                        } else {
                            Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<GetListTopicRes> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "server bị lỗi", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "error api:  " + t);
                    }
                });
            }
        }).start();

    }

    private void updateMenu(String courseName, List<TypeGroupHeader> groupHeaders, Map<String, List<TypeGroupHeader>> childData, List<Topic> listTopics) {
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        TextView headerTitle = findViewById(R.id.textHeader);
        headerTitle.setText("Luyện đề trắc nghiệm " + courseName);
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this, groupHeaders, childData, listTopics);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(
                    ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // Xử lý sự kiện ở đây
                Topic topicClick = new Topic();
                TypeGroupHeader childItem = (TypeGroupHeader) adapter.getChild(groupPosition, childPosition);
                String idChildItem = childItem.getKey();
                for (Topic topic : listTopicChilds) {
                    if (topic.get_id() == idChildItem) {
                        topicClick = topic;
                        break;
                    }
                }
                Log.d("TAG", "topicId " + idChildItem);
                Intent intent = new Intent(TopicExam.this, TopicExamDetail.class);
                intent.putExtra("topicId", idChildItem);
                intent.putExtra("timeExam", topicClick.getTimeExam());
                intent.putExtra("numQuestion", topicClick.getNumQuestion());
                intent.putExtra("name", topicClick.getName());
                TopicExam.this.startActivity(intent);
                return true;
            }
        });

    }
}