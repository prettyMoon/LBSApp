package com.example.myapplication.entity;

import java.io.Serializable;

/**
 * Created by hongli on 2018/5/17.
 */

public class ResultEntity implements Serializable{
    private int status;
    private ContentEntity content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ContentEntity getContent() {
        return content;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }
}
