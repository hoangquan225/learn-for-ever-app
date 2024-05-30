package com.example.mylap.page.topicLearn;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.example.mylap.R;

public class CustomMediaController extends MediaController {
    private Context mContext;
    private MediaControllerListener listener;

    public CustomMediaController(Context context, MediaControllerListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        // Tạo nút xoay và thêm nó vào MediaController
        ImageButton rotateButton = new ImageButton(getContext());
        rotateButton.setImageResource(R.drawable.icon_full_video); // Thay thế bằng hình ảnh biểu tượng xoay của bạn
        rotateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRotateClicked();
                }
            }
        });

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addView(rotateButton, layoutParams);
    }
}


