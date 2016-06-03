package com.laoschool.entities;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 6/1/2016.
 */
public class AttendanceRollup {

    List<Attendance> attendances = new ArrayList<>();
    List<User> students = new ArrayList<>();
    List<TimeTable> timetables = new ArrayList<>();

    public AttendanceRollup() {
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<TimeTable> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<TimeTable> timetables) {
        this.timetables = timetables;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static AttendanceRollup fromJson(String jsonString) {
        Gson gson = new Gson();
        AttendanceRollup attendanceRollup = gson.fromJson(jsonString, AttendanceRollup.class);
        return attendanceRollup;
    }
}
