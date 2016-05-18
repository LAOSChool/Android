package com.laoschool.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Tran An on 11/03/2016.
 */
public class TimeTable implements Parcelable {

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

    String teacher;

    String className;


    public TimeTable() {
    }

    public TimeTable(int id, int school_id, int class_id, int teacher_id, int subject_id, int session_id, int weekday_id, String description, String subject, String session, String weekday, String teacher, String className) {
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
        this.teacher = teacher;
        this.className = className;
    }

    public TimeTable(String subject) {
        this.subject=subject;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacher;
    }

    public void setTeacherName(String teacher) {
        this.teacher = teacher;
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

    protected TimeTable(Parcel in) {
        id = in.readInt();
        school_id = in.readInt();
        class_id = in.readInt();
        teacher_id = in.readInt();
        subject_id = in.readInt();
        session_id = in.readInt();
        weekday_id = in.readInt();
        description = in.readString();
        subject = in.readString();
        session = in.readString();
        weekday = in.readString();
        teacher = in.readString();
        className = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(school_id);
        dest.writeInt(class_id);
        dest.writeInt(teacher_id);
        dest.writeInt(subject_id);
        dest.writeInt(session_id);
        dest.writeInt(weekday_id);
        dest.writeString(description);
        dest.writeString(subject);
        dest.writeString(session);
        dest.writeString(weekday);
        dest.writeString(teacher);
        dest.writeString(className);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeTable> CREATOR = new Parcelable.Creator<TimeTable>() {
        @Override
        public TimeTable createFromParcel(Parcel in) {
            return new TimeTable(in);
        }

        @Override
        public TimeTable[] newArray(int size) {
            return new TimeTable[size];
        }
    };
}