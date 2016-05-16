package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Tran An on 11/03/2016.
 */
public class TimeTable {

    static final String Entity_Name = "timetable";

    int id;

    int school_id;

    int class_id;

    int teacher_id;

    int subject_id;

    int session_id;

    int weekday_id;

    String description;

    String subject;

    String session;

    String weekday;


    public TimeTable() {
    }

    public TimeTable(int id, int school_id, int class_id, int teacher_id, int subject_id, int session_id, int weekday_id, String description, String subject, String session, String weekday) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
        this.session_id = session_id;
        this.weekday_id = weekday_id;
        this.description = description;
        this.subject = subject;
        this.session = session;
        this.weekday = weekday;
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

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getWeekday_id() {
        return weekday_id;
    }

    public void setWeekday_id(int weekday_id) {
        this.weekday_id = weekday_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSession_Name() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSubject_Name() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWeekday_Name() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static TimeTable fromJson(String jsonString) {
        Gson gson = new Gson();
        TimeTable timetable = gson.fromJson(jsonString, TimeTable.class);
        return timetable;
    }
}
