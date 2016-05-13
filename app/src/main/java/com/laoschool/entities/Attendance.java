package com.laoschool.entities;

import com.google.gson.Gson;

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

    int student_id;

    String student_name;

    String subject;

    String term;

    String session;

    int absent;

    int excused;

    int state;

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
        this.student_id = user_id;
        this.student_name = user_name;
        this.absent = absent;
        this.excused = excused;
        this.state = late;
        this.notice = notice;
        this.chk_user_id = chk_user_id;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static Attendance fromJson(String jsonString) {
        Gson gson = new Gson();
        Attendance attendance = gson.fromJson(jsonString, Attendance.class);
        return attendance;
    }
}
