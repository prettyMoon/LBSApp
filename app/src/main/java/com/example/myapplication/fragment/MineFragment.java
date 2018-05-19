package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.activity.DoneActivity;
import com.example.myapplication.activity.TaskDoingActivity;
import com.example.myapplication.activity.TodoActivity;

/**
 * Created by 孙福来 on 2018/4/15.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout todoLayout, doneLayout, doingLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        initListener();
        return view;
    }

    public void initView(View view) {
        todoLayout = view.findViewById(R.id.publish_layout);
        doneLayout = view.findViewById(R.id.done_layout);
        doingLayout = view.findViewById(R.id.doing_layout);
    }

    private void initListener() {
        todoLayout.setOnClickListener(this);
        doneLayout.setOnClickListener(this);
        doingLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.publish_layout:
                intent = new Intent(getActivity(), TodoActivity.class);
                startActivity(intent);
                break;
            case R.id.done_layout:
                intent = new Intent(getActivity(), DoneActivity.class);
                startActivity(intent);
                break;
            case R.id.doing_layout:
                intent = new Intent(getActivity(), TaskDoingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
