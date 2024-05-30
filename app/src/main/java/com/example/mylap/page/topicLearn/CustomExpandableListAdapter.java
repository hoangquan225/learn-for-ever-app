package com.example.mylap.page.topicLearn;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylap.R;
import com.example.mylap.models.topic.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<TypeGroupHeader> groupHeaders;
    private Map<String, List<TypeGroupHeader>> childData;
    private List<Topic> listTopics = new ArrayList<>();

    public CustomExpandableListAdapter(Context context, List<TypeGroupHeader> groupHeaders, Map<String, List<TypeGroupHeader>> childData, List<Topic> listTopics) {
        this.context = context;
        this.groupHeaders = groupHeaders;
        this.childData = childData;
        this.listTopics = listTopics;
    }

    @Override
    public int getGroupCount() {
        return groupHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        TypeGroupHeader groupHeader = groupHeaders.get(groupPosition);
        return childData.get(groupHeader.getKey()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        TypeGroupHeader groupHeader = groupHeaders.get(groupPosition);
        return childData.get(groupHeader.getKey()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TypeGroupHeader groupHeader = groupHeaders.get(groupPosition);
        String groupHeaderTitle = groupHeader.getValue();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(com.example.mylap.R.layout.group_header_layout, null);
        }

        TextView groupHeaderTextView = convertView.findViewById(R.id.groupHeaderTextView);
        ImageView groupIcon = convertView.findViewById(R.id.imageView);

        groupHeaderTextView.setText(groupHeaderTitle);
//        groupHeaderTextView.setTextColor(context.getResources().getColor(android.R.color.black)); // Thiết lập màu cho group header

        if (isExpanded) {
            groupIcon.setImageResource(R.drawable.expand_down_double); // Đặt biểu tượng mở khi mục nhóm mở
        } else {
            groupIcon.setImageResource(R.drawable.expand_right_double); // Đặt biểu tượng đóng khi mục nhóm đóng
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TypeGroupHeader childItem = (TypeGroupHeader) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item_layout, null);
        }

        TextView childItemTextView = convertView.findViewById(R.id.childItemTextView);
        childItemTextView.setText(childItem.getValue());
        childItemTextView.setTextColor(context.getResources().getColor(android.R.color.black)); // Thiết lập màu cho child item

        ImageView childIcon = convertView.findViewById(R.id.imageView);

        String idChildItem = childItem.getKey();

        Topic foundTopic = null;
        for (Topic topic : listTopics) {
            if (topic.get_id() == idChildItem) {
                foundTopic = topic;
                break;
            }
        }
        if(foundTopic != null) {
            int topicType = foundTopic.getTopicType();
            switch (topicType) {
                case 4:
                    //video
                    Log.d("TAG", "video: ");
                    childIcon.setImageResource(R.drawable.icon_video);
                    break;

                case 2:
                    // bai tap
                    Log.d("TAG", "bai tap: ");
                    childIcon.setImageResource(R.drawable.question);
                    break;

                case 5:
                    // document
                    Log.d("TAG", "document: ");
                    childIcon.setImageResource(R.drawable.book_open_alt);
                    break;

                default:
                    break;
            }
        }
        return convertView;
    }

}
