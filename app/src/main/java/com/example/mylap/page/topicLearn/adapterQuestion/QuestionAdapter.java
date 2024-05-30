package com.example.mylap.page.topicLearn.adapterQuestion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.models.question.AnswerQuestion;
import com.example.mylap.models.question.Question;
import com.example.mylap.utils.Format;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private Context context;

    public QuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_question, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionTextView.setText("Câu hỏi " + (position + 1) + ": " + Format.formatText(question.getQuestion(), false));

        // Xóa tất cả các RadioButton hiện tại trong RadioGroup
        holder.answerOptionsRadioGroup.removeAllViews();
        // Thêm các RadioButton cho các phương án trả lời từ danh sách options
        for (AnswerQuestion option : question.getAnswer()) {

            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(Format.formatText(option.getText(), false));
            int textColor = ContextCompat.getColor(context, R.color.textColorSecondary);
            radioButton.setTextColor(textColor);

            // Xử lý sự kiện khi người dùng thay đổi lựa chọn
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (option.isResult()) {
                        // trả lời đúng
                        Log.d("TAG", "result correct");
                        radioButton.setTextColor(ContextCompat.getColor(context, R.color.answerCorrect));
                        for (int i = 0; i < holder.answerOptionsRadioGroup.getChildCount(); i++) {
                            RadioButton radio = (RadioButton) holder.answerOptionsRadioGroup.getChildAt(i);
                            radio.setEnabled(false);
                        }
                    } else {
                        Log.d("TAG", "result incorrect");
                        // trả lời sai
                        // tìm câu trả lời đúng
                        AnswerQuestion correctAnswer = null;

                        for (AnswerQuestion _option : question.getAnswer()) {
                            if(_option.isResult()) {
                                correctAnswer = _option;
                            }
                        }

                        radioButton.setTextColor(ContextCompat.getColor(context, R.color.answerIncorrect));
                        for (int i = 0; i < holder.answerOptionsRadioGroup.getChildCount(); i++) {
                            RadioButton radio = (RadioButton) holder.answerOptionsRadioGroup.getChildAt(i);
                            radio.setEnabled(false);
                            if (option.getIndex() != i && correctAnswer != null && correctAnswer.getIndex() == i) {
                                radio.setTextColor(ContextCompat.getColor(context, R.color.answerCorrect));
                                radio.setBackgroundColor(ContextCompat.getColor(context, R.color.secondaryColor));
                                radio.setLayoutParams(new RadioGroup.LayoutParams(
                                        RadioGroup.LayoutParams.MATCH_PARENT,
                                        RadioGroup.LayoutParams.WRAP_CONTENT
                                ));
                            }
                        }
                    }
                }
            });

            holder.answerOptionsRadioGroup.addView(radioButton);
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
}

