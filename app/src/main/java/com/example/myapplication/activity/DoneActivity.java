package com.example.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * Created by 孙福来 on 2018/4/16.
 */

public class DoneActivity extends Activity implements View.OnClickListener {
    private TextView tvLeft, tvMiddle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        initViewAndListener();
    }

    private void initViewAndListener() {
        tvLeft = this.findViewById(R.id.left);
        tvLeft.setOnClickListener(this);
        tvMiddle = this.findViewById(R.id.middle);
        tvMiddle.setOnClickListener(this);
        tvMiddle.setText("历史接受");
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
