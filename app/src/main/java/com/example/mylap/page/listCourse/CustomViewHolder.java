package com.example.mylap.page.listCourse;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textName;
    TextView textDescription;
    Button buttonDoNow;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        textName = itemView.findViewById(R.id.textName);
        textDescription = itemView.findViewById(R.id.textDescription);
        buttonDoNow = itemView.findViewById(R.id.buttonDoNow);
    }
}

