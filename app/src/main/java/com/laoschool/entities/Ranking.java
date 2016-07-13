package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hue on 7/6/2016.
 */
public class Ranking {
//    {
//        "ave": "4,5",
//            "grade": "A",
//            "allocation": 1
//    }

    String ave;
    String grade;
    String allocation;

    public Ranking() {

    }

    public String getAve() {
        return ave;
    }

    public void setAve(String ave) {
        this.ave = ave;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAllocation() {
        return allocation;
    }

    public void setAllocation(String allocation) {
        this.allocation = allocation;
    }

    public Ranking(String ave, String grade, String allocation) {
        this.ave = ave;
        this.grade = grade;
        this.allocation = allocation;
    }

    public static Ranking toRanking(String m1) {
        Ranking ranking = new Ranking();
        try {
            JSONObject object = new JSONObject(m1);
            ranking.setAve(object.getString("ave"));
            ranking.setGrade(object.getString("grade"));
            ranking.setAllocation(object.getString("allocation"));
        } catch (JSONException e) {
           // e.printStackTrace();
        }
        return ranking;
    }
}
