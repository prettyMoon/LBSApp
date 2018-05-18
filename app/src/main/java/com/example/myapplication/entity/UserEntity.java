package com.example.myapplication.entity;

import java.io.Serializable;

/**
 * Created by hongli on 2018/5/17.
 */

public class UserEntity implements Serializable {
    private String password;
    private String phoneNumber;
    private String pwdProtectedQuestion;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPwdProtectedQuestion() {
        return pwdProtectedQuestion;
    }

    public void setPwdProtectedQuestion(String pwdProtectedQuestion) {
        this.pwdProtectedQuestion = pwdProtectedQuestion;
    }
}
