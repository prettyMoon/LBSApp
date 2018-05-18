package com.example.myapplication.config;

/**
 * Created by hongli on 2018/5/17.
 */

public interface AppConfiguration {
    /**
     * 无人接受
     */
    int NOT_ACCEPTED = 1;
    /**
     * 已经接受/进行中
     */
    int ACCEPTED = 2;
    /**
     * 已完成
     */
    int FINISHED = 3;
    /**
     * 已删除
     */
    int DELETED = 4;
    String IP = "http://192.168.1.109:8080/";

    //注册
    String url_regist = IP + "app/user/regist";
    //登录
    String url_log_in = IP + "app/user/login";
    //发布任务
    String url_post_task = IP + "app/task/save";
    //任务接单
    String url_task_list = IP + "app/task/showAll";
    //任务列表
    String url_accept_task = IP + "app/task/acceptTask";
}
