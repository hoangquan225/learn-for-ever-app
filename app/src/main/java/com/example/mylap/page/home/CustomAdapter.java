package com.example.mylap.page.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylap.R;
import com.example.mylap.models.Category;
import com.example.mylap.page.listCourse.ListCourse;
import com.example.mylap.utils.Format;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<Category> itemList;
    private Context activity;

    public CustomAdapter(List<Category> itemList, FragmentActivity activity) {
        this.itemList = itemList;
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
        Category item = itemList.get(position);

        Picasso.get().load(item.getAvatar()).into(holder.imageView);
        holder.textName.setText(item.getName());
        holder.textDescription.setText(Format.formatText(item.getDes(), false));
        holder.buttonDoNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút "Làm ngay" được nhấn
//                Log.d("TAG", "id category: " + item.getId());
                Intent intent = new Intent(activity, ListCourse.class);
                intent.putExtra("categoryId", item.getId());
                intent.putExtra("categoryName", item.getName());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

