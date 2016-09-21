package com.laoschool.entities;

import android.widget.EditText;

import com.google.gson.Gson;

/**
 * Created by Hue on 6/9/2016.
 */
public class ExamType {
    //    {
//        "id": 10,
//            "school_id": 1,
//            "term_val": 2,
//            "ex_month": 4,
//            "ex_type": 1,
//            "ex_name": "Normal exam",
//            "cls_levels": "--ALL--"
//    }

    //    {
//        "id": 170,
//            "school_id": 10,
//            "term_val": 2,
//            "ex_month": 0,
//            "ex_type": 7,
//            "ex_name": "Grade Test",
//            "cls_levels": ".4.7.",
//            "ex_displayname": "Grade Test",
//            "ex_key": "m17",
//            "min": 0,
//            "max": 10
//    }
    int id;
    int school_id;
    int term_val;
    int ex_month;
    int ex_type;
    String ex_name;
    String cls_levels;
    String ex_key;

    public ExamType(int id, int school_id, int term_val, int ex_month, int ex_type, String ex_name, String cls_levels, String ex_key) {
        this.id = id;
        this.school_id = school_id;
        this.term_val = term_val;
        this.ex_month = ex_month;
        this.ex_type = ex_type;
        this.ex_name = ex_name;
        this.cls_levels = cls_levels;
        this.ex_key = ex_key;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public int getTerm_val() {
        return term_val;
    }

    public void setTerm_val(int term_val) {
        this.term_val = term_val;
    }

    public int getEx_month() {
        return ex_month;
    }

    public void setEx_month(int ex_month) {
        this.ex_month = ex_month;
    }

    public int getEx_type() {
        return ex_type;
    }

    public void setEx_type(int ex_type) {
        this.ex_type = ex_type;
    }

    public String getEx_name() {
        return ex_name;
    }

    public void setEx_name(String ex_name) {
        this.ex_name = ex_name;
    }

    public String getCls_levels() {
        return cls_levels;
    }

    public void setCls_levels(String cls_levels) {
        this.cls_levels = cls_levels;
    }

    public String getEx_key() {
        return ex_key;
    }

    public void setEx_key(String ex_key) {
        this.ex_key = ex_key;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static ExamType fromJson(String jsonString) {
        Gson gson = new Gson();
        ExamType examType = gson.fromJson(jsonString, ExamType.class);
        return examType;
    }

    @Override
    public String toString() {
        return "ExamType{" +
                "id=" + id +
                ", school_id=" + school_id +
                ", term_val=" + term_val +
                ", ex_month=" + ex_month +
                ", ex_type=" + ex_type +
                ", ex_name='" + ex_name + '\'' +
                ", cls_levels='" + cls_levels + '\'' +
                ", ex_key='" + ex_key + '\'' +
                '}';
    }
}
