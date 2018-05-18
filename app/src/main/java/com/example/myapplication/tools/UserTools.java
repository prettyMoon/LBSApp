package com.example.myapplication.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by hongli on 2018/5/18.
 */

public class UserTools {

    private static UserTools userTools;

    private UserTools() {
    }

    public static UserTools getInstance() {
        if (userTools == null) {
            userTools = new UserTools();
        }
        return userTools;
    }

    public void saveInfo(Context context, HashMap<String, String> content) {
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (String key : content.keySet()) {
            editor.putString(key, content.get(key));
        }
        editor.commit();
    }

    public SharedPreferences getInfo(Context context) {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }
}
