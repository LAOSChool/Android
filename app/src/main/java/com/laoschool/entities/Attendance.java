package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Tran An on 11/03/2016.
 */
public class Attendance {

    static final String Entity_Name = "attendance";

    int id;

    int school_id;

    int class_id;

    String att_dt;

    int subject_id;

    int user_id;

    String user_name;

    int absent;

    int excused;

    int late;

    String notice;

    int chk_user_id;

    public Attendance() {
    }

    public Attendance(int id, int school_id, int class_id, String att_dt, int subject_id, int user_id, String user_name, int absent, int excused, int late, String notice, int chk_user_id) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.att_dt = att_dt;
        this.subject_id = subject_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.absent = absent;
        this.excused = excused;
        this.late = late;
        this.notice = notice;
        this.chk_user_id = chk_user_id;
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

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getAtt_dt() {
        return att_dt;
    }

    public void setAtt_dt(String att_dt) {
        this.att_dt = att_dt;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getExcused() {
        return excused;
    }

    public void setExcused(int excused) {
        this.excused = excused;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getChk_user_id() {
        return chk_user_id;
    }

    public void setChk_user_id(int chk_user_id) {
        this.chk_user_id = chk_user_id;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = "{\"" + Entity_Name + "\":" + gson.toJson(this) + "}";
        return jsonString;
    }

    public static Attendance fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject object = json.getJSONObject(Entity_Name);
            Gson gson = new Gson();
            Attendance attendance = gson.fromJson(object.toString(), Attendance.class);
            return attendance;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
