package com.zxy.memorandummemo.Service;

import cn.bmob.v3.BmobObject;

/**
 * Created by zxy on 2016/8/8.
 */
public class Data extends BmobObject{
    private String id;
    private String content;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
