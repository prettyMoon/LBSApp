package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.config.AppConfiguration;


import net.tsz.afinal.FinalBitmap;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hongli on 2018/5/19.
 */

public class ProgressListAdapter extends BaseAdapter {
    private List<HashMap<String, String>> dataList;
    private LayoutInflater inflater;
    private Context context;

    public ProgressListAdapter(List<HashMap<String, String>> dataList, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("zhl", "3");
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.process_list_item, null);
        }
        ViewHolder holder = new ViewHolder(convertView);
        long time = 0L;
        if (dataList.get(position).get("publish_time") == null || dataList.get(position).get("publish_time").equals("") || dataList.get(position).get("publish_time").equals("null")) {
            time = System.currentTimeMillis();
        } else {
            time = Long.parseLong(dataList.get(position).get("publish_time"));
        }
        String date = timeStamp2Date(time);
        holder.tvTime.setText(date);
        holder.tvMsg.setText(dataList.get(position).get("msg"));
        FinalBitmap fb = FinalBitmap.create(context);//初始化FinalBitmap模块
        fb.configLoadingImage(R.drawable.loading);//设置加载图片
        fb.display(holder.img, AppConfiguration.IP + "/" + dataList.get(position).get("link"));
        return convertView;
    }

    public String timeStamp2Date(long timestamp) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String d = format.format(timestamp);
        return d;
    }

    class ViewHolder {
        public TextView tvMsg, tvTime;
        public ImageView img;

        public ViewHolder(View view) {
            tvMsg = view.findViewById(R.id.tv_msg);
            tvTime = view.findViewById(R.id.tv_time);
            img = view.findViewById(R.id.progress_img);
        }
    }
}
