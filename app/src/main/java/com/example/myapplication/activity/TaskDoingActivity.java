package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TaskDoingListAdapter;
import com.example.myapplication.adapter.TaskListAdapter;
import com.example.myapplication.config.AppConfiguration;
import com.example.myapplication.tools.MyDialog;
import com.example.myapplication.tools.ShowToast;
import com.example.myapplication.tools.UserTools;
import com.example.myapplication.tools.view.SmoothListView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hongli on 2018/5/19.
 */

public class TaskDoingActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private TaskDoingListAdapter adapter;
    private List<HashMap<String, String>> dataList;
    private MyDialog diaLog;
    private TextView tvLeft, tvMiddle, tvRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_doing);
        initView();

    }

    private void initView() {
        initDialog();
        listView = this.findViewById(R.id.listView);
        initListView(AppConfiguration.url_has_accept);
        tvLeft = this.findViewById(R.id.left);
        tvMiddle = this.findViewById(R.id.middle);
        tvRight = this.findViewById(R.id.right);
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("zhl", "setOnItemClickListener");
                Intent intent = new Intent(TaskDoingActivity.this, TaskDoingDetailActivity.class);
                Bundle bundle = new Bundle();
                HashMap<String, String> map = dataList.get(position);
                bundle.putString("user_id", map.get("user_id"));
                bundle.putString("publish_time", map.get("publish_time"));
                bundle.putString("accept_user_id", map.get("accept_user_id"));
                bundle.putString("id", map.get("id"));
                bundle.putString("description", map.get("description"));
                bundle.putString("status", map.get("status"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initDialog() {
        diaLog = new MyDialog(TaskDoingActivity.this);
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }

    public void initListView(String url) {
        if (!diaLog.isShowing()) {
            diaLog.show();
        }
        AjaxParams params = new AjaxParams();
        SharedPreferences sp = UserTools.getInstance().getInfo(TaskDoingActivity.this);
        String id = sp.getString("id", "");
        params.put("user_id", id);
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                ShowToast.showToast(TaskDoingActivity.this, "请求失败！");
                if (diaLog.isShowing()) {
                    diaLog.dismiss();
                }
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
                try {
                    if (diaLog.isShowing()) {
                        diaLog.dismiss();
                    }
                    JSONObject jsonObject = new JSONObject(t.toString());
                    Log.e("zhl****", jsonObject.toString());
                    if (jsonObject.getInt("status") != 1) {
                        ShowToast.showToast(TaskDoingActivity.this, "请求失败！");
                    } else {
                        if (dataList == null) {
                            dataList = new ArrayList<HashMap<String, String>>();
                        }
                        if (dataList.size() > 1) {
                            dataList.clear();
                        }
                        Log.e("zhl", "1");
                        JSONArray array = jsonObject.getJSONArray("content");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            if (object.getInt("status") != 2) {
                                continue;
                            }
                            Log.e("zhl", "22");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("user_id", object.getString("user_id"));
                            map.put("publish_time", object.getString("publish_time"));
                            map.put("accept_user_id", object.getString("accept_user_id"));
                            map.put("id", object.getString("id"));
                            map.put("description", object.getString("description"));
                            map.put("status", object.getString("status"));
                            dataList.add(map);
                        }
                        adapter = new TaskDoingListAdapter(TaskDoingActivity.this, dataList);
                        listView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
        }
    }
}
