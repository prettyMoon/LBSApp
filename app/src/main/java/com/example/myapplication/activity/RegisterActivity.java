package com.example.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by 孙福来 on 2018/4/16.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private TextView tvLeft, tvMiddle;
    private Button btnSubmit;
    private MyDialog diaLog;
    private EditText editTextPhoneNumner, editTextPwd, editTextPwdSure, editTextQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViewAndListener();
        initDialog();
    }

    private void initDialog() {
        diaLog = new MyDialog(RegisterActivity.this);
        diaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diaLog.setCancelable(true);
    }

    private void initViewAndListener() {
        tvLeft = this.findViewById(R.id.left);
        tvLeft.setOnClickListener(this);
        tvMiddle = this.findViewById(R.id.middle);
        tvMiddle.setOnClickListener(this);
        tvMiddle.setText("用户注册");
        btnSubmit = this.findViewById(R.id.submit_resgister);
        btnSubmit.setOnClickListener(this);
        editTextPhoneNumner = this.findViewById(R.id.phoneNumber);
        editTextPwd = this.findViewById(R.id.pwd);
        editTextPwdSure = this.findViewById(R.id.pwdSure);
        editTextQuestion = this.findViewById(R.id.question);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.submit_resgister:
                if (vaildate()) {
                    diaLog.show();
                    doRegister(AppConfiguration.url_regist);
                }
                break;
        }
    }

    private boolean vaildate() {
        if (editTextPhoneNumner.getText() == null || editTextPhoneNumner.getText().toString().equals("")) {
            ShowToast.showToast(RegisterActivity.this, "请输入手机号码！");
            return false;
        }
        if (editTextPwd.getText() == null) {
            ShowToast.showToast(RegisterActivity.this, "请输入密码！");
            return false;
        }
        if (editTextQuestion.getText() == null || editTextQuestion.getText().toString().equals("")) {
            ShowToast.showToast(RegisterActivity.this, "请输入密保！");
            return false;
        }
        if (editTextPwdSure.getText() == null || !editTextPwdSure.getText().toString().equals(editTextPwd.getText().toString())) {
            ShowToast.showToast(RegisterActivity.this, "两次输入密码不一致！");
            return false;
        }
        return true;
    }

    public void doRegister(String url) {

        AjaxParams params = new AjaxParams();
        params.put("password", editTextPwd.getText().toString());
        params.put("phoneNumber", editTextPhoneNumner.getText().toString());
        params.put("pwdProtectedQuestion", editTextQuestion.getText().toString());
        FinalHttp fh = new FinalHttp();
        fh.post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.i("zhl", "onFailure");
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
                    if (resultEntity.getStatus() == 1) {
                        ShowToast.showToast(RegisterActivity.this, "注册成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                    if (resultEntity.getStatus() == 2) {
                        ShowToast.showToast(RegisterActivity.this, "注册失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

