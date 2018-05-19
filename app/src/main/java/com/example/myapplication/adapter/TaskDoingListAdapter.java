package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.TaskDoingActivity;
import com.example.myapplication.activity.UploadDataActivity;
import com.example.myapplication.config.AppConfiguration;
import com.example.myapplication.tools.MyDialog;
import com.example.myapplication.tools.ShowToast;
import com.example.myapplication.tools.UserTools;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hongli on 2018/5/19.
 */

public class TaskDoingListAdapter extends BaseAdapter {
    private List<HashMap<String, String>> dataList;
    private LayoutInflater inflater;
    private Context context;

    public TaskDoingListAdapter(Context context, List<HashMap<String, String>> dataList) {
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
            convertView = inflater.inflate(R.layout.doing_list_item, null);
        }
        ViewHolder holder = new ViewHolder(convertView);
        holder.tvName.setText("用戶" + dataList.get(position).get("user_id").toString());
        holder.tvDescription.setText(dataList.get(position).get("description"));
        String date = timeStamp2Date(Long.parseLong(dataList.get(position).get("publish_time")));
        holder.tvTime.setText(date);
        holder.btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("task_id", dataList.get(position).get("id"));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFinish(AppConfiguration.url_task_finish, position);
            }
        });
        return convertView;
    }

    public String timeStamp2Date(long timestamp) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String d = format.format(timestamp);
        return d;
    }

    public void doFinish(String url, final int position) {
        final MyDialog myDialog = new MyDialog(context);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setCancelable(true);
        myDialog.show();
        AjaxParams params = new AjaxParams();
        SharedPreferences sp = UserTools.getInstance().getInfo(context);
        String id = sp.getString("id", "");
        params.put("user_id", id);
        params.put("task_id", dataList.get(position).get("id"));
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                myDialog.dismiss();
                ShowToast.showToast(context, "请求失败！");
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object t) {
                myDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(t.toString());
                    Log.e("zhl****", jsonObject.toString());
                    if (jsonObject.getInt("status") != 1) {
                        ShowToast.showToast(context, "请求失败！");
                    } else {
                        ShowToast.showToast(context, "请求成功！");
                        dataList.remove(position);
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class ViewHolder {
        public TextView tvName, tvDescription, tvTime;
        public Button btnUp, btnDone;

        public ViewHolder(View view) {
            tvDescription = view.findViewById(R.id.tv_description);
            tvName = view.findViewById(R.id.tv_name);
            tvTime = view.findViewById(R.id.tv_time);
            btnUp = view.findViewById(R.id.btn_up);
            btnDone = view.findViewById(R.id.btn_done);

        }
    }


}
