package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.TaskDoneActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hongli on 2018/5/19.
 */

public class TaskListAdapter extends BaseAdapter {
    private List<HashMap<String, String>> dataList;
    private LayoutInflater inflater;
    private Context context;

    public TaskListAdapter(Context context, List<HashMap<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("zhl", "3");
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        ViewHolder holder = new ViewHolder(convertView);
        holder.tvName.setText("用戶" + dataList.get(position).get("user_id").toString());
        holder.tvDescription.setText(dataList.get(position).get("description"));
        String date = timeStamp2Date(Long.parseLong(dataList.get(position).get("publish_time")));
        holder.tvTime.setText(date);
        return convertView;
    }

    public String timeStamp2Date(long timestamp) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String d = format.format(timestamp);
        return d;
    }

    class ViewHolder {
        public TextView tvName, tvDescription, tvTime;

        public ViewHolder(View view) {
            tvDescription = view.findViewById(R.id.tv_description);
            tvName = view.findViewById(R.id.tv_name);
            tvTime = view.findViewById(R.id.tv_time);
        }
    }
}
