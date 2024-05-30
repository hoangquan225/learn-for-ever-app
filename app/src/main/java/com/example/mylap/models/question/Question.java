package com.example.mylap.models.question;

import java.util.ArrayList;

public class Question implements Comparable<Question>{
    private String id;
    private String question;
    private ArrayList<AnswerQuestion> answer;
    private String parentId;
    private int status;
    private int index;
    private String hint;
    private long createDate;
    private long updateDate;

    public Question() {
    }

    public Question(String id, String question, ArrayList<AnswerQuestion> answer, String parentId, int status, int index, String hint, long createDate, long updateDate) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.parentId = parentId;
        this.status = status;
        this.index = index;
        this.hint = hint;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<AnswerQuestion> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<AnswerQuestion> answer) {
        this.answer = answer;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int compareTo(Question otherQuestion) {
        // So sánh theo trường index tăng dần
        return Integer.compare(this.index, otherQuestion.index);
    }

}
