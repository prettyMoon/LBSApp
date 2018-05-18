package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.config.AppConfiguration;
import com.example.myapplication.entity.ResultEntity;
import com.example.myapplication.tools.MyDialog;
import com.example.myapplication.tools.ShowToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 孙福来 on 2018/4/15.
 */

public class LogInActivity extends Activity implements View.OnClickListener {
    private Button btnLogIn, tvSignIn;
    private MyDialog diaLog;
    private EditText editTextPhoneNumber, editTextPwd;

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
        editTextPhoneNumber = this.findViewById(R.id.edit_phone);
        editTextPwd = this.findViewById(R.id.edit_pwd);
        initDialog();
    }

    private void initListener() {
        btnLogIn.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
        btnLogIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LogInActivity.this, TaskDoneActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    private void initDialog() {
        diaLog = new MyDialog(LogInActivity.this);
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }


    public boolean validate() {
        if (editTextPhoneNumber.getText() == null || editTextPhoneNumber.getText().toString().equals("")) {
            ShowToast.showToast(LogInActivity.this, "请输入账号！");
            return false;
        }
        if (editTextPwd.getText() == null || editTextPwd.getText().toString().equals("")) {
            ShowToast.showToast(LogInActivity.this, "请输入密码！");
            return false;
        }
        return true;
    }

    public void doLogin(String url) {
        AjaxParams params = new AjaxParams();
        params.put("password", editTextPwd.getText().toString());
        params.put("phoneNumber", editTextPhoneNumber.getText().toString());
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure:" + strMsg);
                ShowToast.showToast(LogInActivity.this, "登录失败！");
                if (diaLog.isShowing()) {
                    diaLog.dismiss();
                }
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(Object t) {
                try {
                    JSONObject jsonObject = new JSONObject(t.toString());
                    Log.e("zhl", jsonObject.toString());
                    Gson gson = new Gson();
                    ResultEntity resultEntity = gson.fromJson(t.toString(), new TypeToken<ResultEntity>() {
                    }.getType());
                    if (diaLog.isShowing()) {
                        diaLog.dismiss();
                    }
                    //登陆成功
                    if (resultEntity.getStatus() == 1) {
                        ShowToast.showToast(LogInActivity.this, "登录成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    }
                    //登陆失败
                    if (resultEntity.getStatus() == 2) {
                        ShowToast.showToast(LogInActivity.this, "登录失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_log_in:
//                if (validate()) {
//                    diaLog.show();
//                    doLogin(AppConfiguration.url_log_in);
//                }
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

