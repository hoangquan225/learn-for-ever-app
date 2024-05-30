package com.example.mylap.page.topicExam.adapterQuestion;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.models.question.AnswerQuestion;
import com.example.mylap.models.question.Question;
import com.example.mylap.utils.Format;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionExamAdapter extends RecyclerView.Adapter<QuestionExamAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private Context context;

    private boolean isReviewMode = false;
    private Map<String, String> selectedAnswers = new HashMap<>(); // Store selected answers

    public QuestionExamAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_question_exam, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionTextView.setText("Câu hỏi " + (position + 1) + ": " + Format.formatText(question.getQuestion(), false));
        holder.answerOptionsRadioGroup.removeAllViews();

        String selectedAnswer = selectedAnswers.get(question.getId());
        for (AnswerQuestion option : question.getAnswer()) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(Format.formatText(option.getText(), false));
            boolean isChecked1 = selectedAnswer != null && selectedAnswer.equals(option.get_id());
            radioButton.setChecked(isChecked1);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedAnswers.put(question.getId(), option.get_id());
                } else {
                    selectedAnswers.remove(question.getId());
                }
                notifyItemChanged(position);
            });
            holder.answerOptionsRadioGroup.addView(radioButton);

            if (isReviewMode && option.isResult()) {
                radioButton.setTextColor(Color.GREEN);
            } else if (isReviewMode && !option.isResult() && selectedAnswer != null && selectedAnswer.equals(option.get_id())) {
                radioButton.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextView;
        public RadioGroup answerOptionsRadioGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerOptionsRadioGroup = itemView.findViewById(R.id.answerOptionsRadioGroup);
        }
    }

    public Map<String, String> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setReviewMode(boolean isReviewMode) {
        this.isReviewMode = isReviewMode;
        notifyDataSetChanged();
    }
}

