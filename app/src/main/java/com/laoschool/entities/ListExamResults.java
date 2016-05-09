package com.laoschool.entities;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Hue on 5/9/2016.
 */
public class ListExamResults {

    int total_count;
    int from_row;
    int to_row;
    List<ExamResult> list;

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

    public List<ExamResult> getList() {
        return list;
    }

    public void setList(List<ExamResult> list) {
        this.list = list;
    }
    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }
    public static ListExamResults fromJson(String jsonString) {
        Gson gson = new Gson();
        ListExamResults examResults = gson.fromJson(jsonString, ListExamResults.class);
        return examResults;
    }
}
