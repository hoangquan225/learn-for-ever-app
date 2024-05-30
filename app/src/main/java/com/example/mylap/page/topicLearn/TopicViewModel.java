package com.example.mylap.page.topicLearn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mylap.models.topic.Topic;

public class TopicViewModel extends ViewModel {
    private Topic _currentTopic;
    private MutableLiveData<Topic> currentTopic = new MutableLiveData<>();

    public LiveData<Topic> getCurrentTopic() {
        currentTopic.setValue(_currentTopic);
        return currentTopic;
    }

    public void setCurrentTopic(Topic data) {
        _currentTopic = data;
        currentTopic.setValue(_currentTopic);
    }

    public Topic get_currentTopic() {
        return _currentTopic;
    }
}
