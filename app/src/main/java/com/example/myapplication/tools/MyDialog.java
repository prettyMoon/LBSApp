package com.example.myapplication.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.myapplication.R;

/**
 * Created by hongli on 2018/5/17.
 */

public class MyDialog extends Dialog {

    public MyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dialog_layout);
    }
}