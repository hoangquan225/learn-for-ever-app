package com.example.mylap.page.listCourse;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.models.Course;
import com.example.mylap.page.courseDetail.CourseDetail;
import com.example.mylap.utils.Format;
import com.example.mylap.utils.SharedPreferencesUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListCourseAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<Course> courseList;
    private Context activity;

    public ListCourseAdapter(List<Course> courseList, FragmentActivity activity) {
        this.courseList = courseList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Course item = courseList.get(position);

        Picasso.get().load(item.getAvatar()).into(holder.imageView);
        holder.textName.setText(item.getCourseName());
        holder.textDescription.setText(Format.formatText(item.getShortDes(), false));
        holder.buttonDoNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút "Làm ngay" được nhấn
//                Log.d("TAG", "id category: " + item.getId());
                Intent intent = new Intent(activity, CourseDetail.class);
                intent.putExtra("courseId", item.getId());
                SharedPreferencesUtils.removeStringToSharedPreferences(activity, "courseId");
                SharedPreferencesUtils.saveStringToSharedPreferences(activity, "courseId", item.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}

