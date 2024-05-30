package com.example.mylap.models.question;

public class AnswerQuestion {
    private String _id;

    private int index;
    private String text;
    private boolean isResult;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    public AnswerQuestion() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public AnswerQuestion(String _id, int index, String text, boolean isResult) {
        this._id = _id;
        this.index = index;
        this.text = text;
        this.isResult = isResult;
    }

    public AnswerQuestion(int index, String text, boolean isResult) {
        this.index = index;
        this.text = text;
        this.isResult = isResult;
    }
}
