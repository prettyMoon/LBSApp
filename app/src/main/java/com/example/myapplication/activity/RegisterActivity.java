package com.example.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

/**
 * Created by 孙福来 on 2018/4/16.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private TextView tvLeft, tvMiddle;
    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViewAndListener();
    }

    private void initViewAndListener() {
        tvLeft = this.findViewById(R.id.left);
        tvLeft.setOnClickListener(this);
        tvMiddle = this.findViewById(R.id.middle);
        tvMiddle.setOnClickListener(this);
        tvMiddle.setText("用户注册");
        btnSubmit = this.findViewById(R.id.submit_resgister);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.submit_resgister:
                Toast.makeText(RegisterActivity.this, "注冊成功", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
        }
    }

}

