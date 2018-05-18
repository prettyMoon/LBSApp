package com.example.myapplication.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TaskListAdapter;
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

/**
 * Created by hongli on 2018/5/19.
 */

public class TaskDetailActivity extends Activity implements View.OnClickListener {
    private TextView tvLeft, tvMiddle, tvTime, tvUser, tvDescription;
    private Button btnSubmit;
    private Bundle bundle;
    private MyDialog diaLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initView();
        initContent();
        initDialog();
    }
    private void initDialog() {
        diaLog = new MyDialog(TaskDetailActivity.this);
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }
    private void initView() {
        tvLeft = this.findViewById(R.id.left);
        tvLeft.setOnClickListener(this);
        tvMiddle = this.findViewById(R.id.middle);
        tvMiddle.setText("任务详情");
        tvTime = this.findViewById(R.id.tv_date);
        tvUser = this.findViewById(R.id.tv_user);
        tvDescription = this.findViewById(R.id.tv_description);
        btnSubmit = this.findViewById(R.id.submit);
        btnSubmit.setOnClickListener(this);
    }

    private void initContent() {
        bundle = getIntent().getExtras();
        String date = timeStamp2Date(Long.parseLong(bundle.getString("publish_time")));
        tvTime.setText(date);
        tvUser.setText("用户" + bundle.getString("id"));
        tvDescription.setText(bundle.getString("description"));
    }

    public String timeStamp2Date(long timestamp) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String d = format.format(timestamp);
        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if(!diaLog.isShowing()){
                    diaLog.show();
                }
                doAccept(AppConfiguration.url_accept_task);
                break;
            case R.id.left:
                finish();
                break;
        }
    }
    public void doAccept(String url) {
        if (!diaLog.isShowing()) {
            diaLog.show();
        }
        AjaxParams params = new AjaxParams();
        params.put("task_id",bundle.getString("id"));
        SharedPreferences sp = UserTools.getInstance().getInfo(TaskDetailActivity.this);
        String id = sp.getString("id", "");
        params.put("user_id", id);
        Log.e("zhl1",id);
        Log.e("zhl2",bundle.getString("id"));
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                ShowToast.showToast(TaskDetailActivity.this, "请求失败！");
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
                    Log.e("zhl", jsonObject.toString());
                    if (jsonObject.getInt("status") == 1) {
                        ShowToast.showToast(TaskDetailActivity.this, "请求成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },2000);
                    } else {
                        ShowToast.showToast(TaskDetailActivity.this, jsonObject.getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
