package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.activity.TaskDetailActivity;
import com.example.myapplication.adapter.TaskListAdapter;
import com.example.myapplication.config.AppConfiguration;
import com.example.myapplication.tools.MyDialog;
import com.example.myapplication.tools.ShowToast;
import com.example.myapplication.tools.view.SmoothListView;

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
 * Created by 孙福来 on 2018/4/15.
 */

public class TaskFragment extends Fragment implements SmoothListView.ISmoothListViewListener {
    private SmoothListView listView;
    private TaskListAdapter adapter;
    private List<HashMap<String, String>> dataList;
    private MyDialog diaLog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initDialog();
        listView = view.findViewById(R.id.listView);
        initListView(AppConfiguration.url_task_list, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
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
        listView.setSmoothListViewListener(this);
        listView.setRefreshEnable(true);
        listView.setLoadMoreEnable(false);
    }

    private void initDialog() {
        diaLog = new MyDialog(getActivity());
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }

    public void initListView(String url, final boolean firstLoad) {
        if (!diaLog.isShowing() && firstLoad) {
            diaLog.show();
        }
        AjaxParams params = new AjaxParams();
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                ShowToast.showToast(getActivity(), "请求失败！");
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
                    if (jsonObject.getInt("status") != 1) {
                        ShowToast.showToast(getActivity(), "请求失败！");
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
                            Log.e("zhl", "2");
                            JSONObject object = array.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("user_id", object.getString("user_id"));
                            map.put("publish_time", object.getString("publish_time"));
                            map.put("accept_user_id", object.getString("accept_user_id"));
                            map.put("id", object.getString("id"));
                            map.put("description", object.getString("description"));
                            map.put("status", object.getString("status"));
                            dataList.add(map);
                        }
                        if (firstLoad) {
                            adapter = new TaskListAdapter(getActivity(), dataList);
                            listView.setAdapter(adapter);
                        } else {
                            listView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String timeStamp2Date(long timestamp) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String d = format.format(timestamp);
        return d;
    }

    @Override
    public void onRefresh() {
        listView.setRefreshTime(timeStamp2Date(System.currentTimeMillis()));
        initListView(AppConfiguration.url_task_list, false);
    }

    @Override
    public void onLoadMore() {

    }
}
