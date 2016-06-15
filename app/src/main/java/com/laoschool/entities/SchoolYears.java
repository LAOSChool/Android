package com.laoschool.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Hue on 6/15/2016.
 */
public class SchoolYears {
//    {
//        "id": 1,
//            "school_id": 1,
//            "years": "2016-2017",
//            "from_year": 2016,
//            "to_year": 2017,
//            "notice": null
//    }

    int id;
    int school_id;
    String years;
    int from_year;
    int to_year;
    String notice;

    public SchoolYears(int id, int school_id, String years, int from_year, int to_year, String notice) {
        this.id = id;
        this.school_id = school_id;
        this.years = years;
        this.from_year = from_year;
        this.to_year = to_year;
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

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public int getFrom_year() {
        return from_year;
    }

    public void setFrom_year(int from_year) {
        this.from_year = from_year;
    }

    public int getTo_year() {
        return to_year;
    }

    public void setTo_year(int to_year) {
        this.to_year = to_year;
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

    public static SchoolYears fromJson(String jsonString) {
        Gson gson = new Gson();
        SchoolYears schoolYears = gson.fromJson(jsonString, SchoolYears.class);
        return schoolYears;
    }

    public static List<SchoolYears> fromArray(String jsonString) {
        Gson gson = new Gson();
        List<SchoolYears> schoolYears = gson.fromJson(jsonString, new TypeToken<List<SchoolYears>>(){}.getType());
        return schoolYears;
    }
}
