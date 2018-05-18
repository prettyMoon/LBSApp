package com.example.myapplication.config;

/**
 * Created by hongli on 2018/5/17.
 */

public interface AppConfiguration {
    public static final String IP = "http://192.168.1.105:8080/";

    //注册
    public static final String url_regist = IP + "app/user/regist";
    //登录
    public static final String url_log_in = IP + "app/user/login";
}
