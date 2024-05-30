package com.example.mylap.models.topic;

import com.example.mylap.models.question.Question;

import java.util.ArrayList;

public class TimePracticeInVideo {
    private int time;
    private int totalQuestion;
    private ArrayList<String> idQuestion;
    private ArrayList<Question> questionData;

    public TimePracticeInVideo(int time, int totalQuestion, ArrayList<String> idQuestion, ArrayList<Question> questionData) {
        this.time = time;
        this.totalQuestion = totalQuestion;
        this.idQuestion = idQuestion;
        this.questionData = questionData;
    }

    public TimePracticeInVideo() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public ArrayList<String> getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(ArrayList<String> idQuestion) {
        this.idQuestion = idQuestion;
    }

    public ArrayList<Question> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(ArrayList<Question> questionData) {
        this.questionData = questionData;
    }
}
