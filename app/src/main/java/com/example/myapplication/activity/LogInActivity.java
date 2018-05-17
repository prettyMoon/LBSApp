package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

/**
 * Created by 孙福来 on 2018/4/15.
 */

public class LogInActivity extends Activity implements View.OnClickListener {
    private Button btnLogIn, tvSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initView();
        initListener();
    }

    private void initView() {
        btnLogIn = (Button) this.findViewById(R.id.tv_log_in);
        tvSignIn = this.findViewById(R.id.tv_sign_in);
    }

    private void initListener() {
        btnLogIn.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_log_in:
                intent = new Intent(LogInActivity.this, HomePageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sign_in:
                intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
