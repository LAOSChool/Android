package com.laoschool.entities;

import com.google.gson.Gson;

/**
 * Created by Hue on 5/30/2016.
 */
public class Master {
    int id;//": 2
    int school_id;//": 1,
    String sval;//'": "Ly",
    int fval1;//": 0,
    int fval2;//": 0,
    String notice;//": null,


    public Master(int id, int school_id, String sval, int fval1, int fval2, String notice) {
        this.id = id;
        this.school_id = school_id;
        this.sval = sval;
        this.fval1 = fval1;
        this.fval2 = fval2;
        this.notice = notice;
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

    public String getSval() {
        return sval;
    }

    public void setSval(String sval) {
        this.sval = sval;
    }

    public int getFval1() {
        return fval1;
    }

    public void setFval1(int fval1) {
        this.fval1 = fval1;
    }

    public int getFval2() {
        return fval2;
    }

    public void setFval2(int fval2) {
        this.fval2 = fval2;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static Master fromJson(String jsonString) {
        Gson gson = new Gson();
        Master master = gson.fromJson(jsonString, Master.class);
        return master;
    }
}
