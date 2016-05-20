package com.laoschool.entities;

import com.google.gson.Gson;

/**
 * Created by Tran An on 11/03/2016.
 */
public class Class {

    static final String Entity_Name = "class";

    int id;

    int school_id;

    String title;

    String location;

    int term;

    String years;

    String start_dt;

    String end_dt;

    int class_type;

    int grade_type;

    int fee;

    int sts;

    int head_teacher_id;

    String headTeacherName;

    public Class() {
    }

    public Class(int id, int school_id, String title, String location, int term, String years, String start_dt, String end_dt, int class_type, int grade_type, int fee, int sts, int head_teacher_id, String headTeacherName) {
        this.id = id;
        this.school_id = school_id;
        this.title = title;
        this.location = location;
        this.term = term;
        this.years = years;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.class_type = class_type;
        this.grade_type = grade_type;
        this.fee = fee;
        this.sts = sts;
        this.head_teacher_id = head_teacher_id;
        this.headTeacherName = headTeacherName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(String end_dt) {
        this.end_dt = end_dt;
    }

    public int getClass_type() {
        return class_type;
    }

    public void setClass_type(int class_type) {
        this.class_type = class_type;
    }

    public int getGrade_type() {
        return grade_type;
    }

    public void setGrade_type(int grade_type) {
        this.grade_type = grade_type;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getSts() {
        return sts;
    }

    public void setSts(int sts) {
        this.sts = sts;
    }

    public int getHead_teacher_id() { return head_teacher_id; }

    public void setHead_teacher_id(int head_teacher_id) { this.head_teacher_id = head_teacher_id; }

    public String getHeadTeacherName() {
        return headTeacherName;
    }

    public void setHeadTeacherName(String headTeacherName) {
        this.headTeacherName = headTeacherName;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static Class fromJson(String jsonString) {
        Gson gson = new Gson();
        Class aclass = gson.fromJson(jsonString, Class.class);
        return aclass;
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", school_id=" + school_id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", term=" + term +
                ", years='" + years + '\'' +
                ", start_dt='" + start_dt + '\'' +
                ", end_dt='" + end_dt + '\'' +
                ", class_type=" + class_type +
                ", grade_type=" + grade_type +
                ", fee=" + fee +
                ", sts=" + sts +
                ", head_teacher_id=" + head_teacher_id +
                ", headTeacherName='" + headTeacherName + '\'' +
                '}';
    }
}
