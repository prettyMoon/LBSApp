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
    String IP = "http://192.168.1.109:8080/app";

    //注册
    String url_regist = IP + "/user/regist";
    //登录
    String url_log_in = IP + "/user/login";
    //发布任务
    String url_post_task = IP + "/task/save";
    //任务接单
    String url_task_list = IP + "/task/showAll";
    //任务列表
    String url_accept_task = IP + "/task/acceptTask";
    //上传数据
    String url_upload_data = IP + "/tdata/save";
    //历史接收
    String url_has_accept = IP + "/task/hasAccepted";
    //历史发布
    String url_has_publish = IP + "/task/hasPublish";
    //任务数据
    String url_task_data = IP + "/tdata/taskData";
    //任务完成
    String url_task_finish = IP + "/task/finishTask";
}
