package com.laoschool.entities;

import com.google.gson.Gson;

/**
 * Created by Tran An on 7/4/2016.
 */
public class MessageSample {

    int id;
    String sval;
    String lval;

    public MessageSample() {
    }

    public MessageSample(int id, String sval, String lval) {
        this.id = id;
        this.sval = sval;
        this.lval = lval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSval() {
        return sval;
    }

    public void setSval(String sval) {
        this.sval = sval;
    }

    public String getLval() {
        return lval;
    }

    public void setLval(String lval) {
        this.lval = lval;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static MessageSample fromJson(String jsonString) {
        Gson gson = new Gson();
        MessageSample messageSample = gson.fromJson(jsonString, MessageSample.class);
        return messageSample;
    }
}
