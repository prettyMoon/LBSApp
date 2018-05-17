package com.example.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

/**
 * Created by 孙福来 on 2018/4/16.
 */

public class PublishTaskActivity extends Activity implements View.OnClickListener {
    private TextView left, middle;
    private EditText textContent;
    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initListener();
    }

    private void initView() {
        left = this.findViewById(R.id.left);
        middle = this.findViewById(R.id.middle);
        textContent = this.findViewById(R.id.taskContent);
        btnSubmit = this.findViewById(R.id.submit);
        middle.setText("发布任务");
    }

    private void initListener() {
        left.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.submit:
                Toast.makeText(PublishTaskActivity.this, "任务已成功提交", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
                break;
        }
    }
}
