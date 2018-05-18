package com.example.myapplication.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hongli on 2018/5/17.
 */

public class ShowToast {
    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
