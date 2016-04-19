package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Tran An on 08/04/2016.
 */
public class ListMessages {

    int total_count;
    int from_row;
    int to_row;
    List<Message> list;

    public ListMessages() {
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getFrom_row() {
        return from_row;
    }

    public void setFrom_row(int from_row) {
        this.from_row = from_row;
    }

    public int getTo_row() {
        return to_row;
    }

    public void setTo_row(int to_row) {
        this.to_row = to_row;
    }

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static ListMessages fromJson(String jsonString) {
        Gson gson = new Gson();
        ListMessages messages = gson.fromJson(jsonString, ListMessages.class);
        return messages;

    }
}
