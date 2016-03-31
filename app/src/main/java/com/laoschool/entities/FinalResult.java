package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Tran An on 11/03/2016.
 */
public class FinalResult {

    static final String Entity_Name = "final_results";

    int id;

    int school_id;

    int class_id;

    int subject_id;

    int teacher_id;

    int student_id;

    String student_name;

    String notice;

    int term_id;

    String start_dt;

    String end_dt;

    float average_score;

    float bonus_score;

    float final_result;

    int pass;

    int rank_id;

    float absent;

    float excused;

    float late;

    public FinalResult() {
    }

    public FinalResult(int id, int school_id, int class_id, int subject_id, int teacher_id, int student_id, String student_name, String notice, int term_id, String start_dt, String end_dt, float average_score, float bonus_score, float final_result, int pass, int rank_id, float absent, float excused, float late) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.subject_id = subject_id;
        this.teacher_id = teacher_id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.notice = notice;
        this.term_id = term_id;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.average_score = average_score;
        this.bonus_score = bonus_score;
        this.final_result = final_result;
        this.pass = pass;
        this.rank_id = rank_id;
        this.absent = absent;
        this.excused = excused;
        this.late = late;
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

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
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

    public float getAverage_score() {
        return average_score;
    }

    public void setAverage_score(float average_score) {
        this.average_score = average_score;
    }

    public float getBonus_score() {
        return bonus_score;
    }

    public void setBonus_score(float bonus_score) {
        this.bonus_score = bonus_score;
    }

    public float getFinal_result() {
        return final_result;
    }

    public void setFinal_result(float final_result) {
        this.final_result = final_result;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getRank_id() {
        return rank_id;
    }

    public void setRank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public float getAbsent() {
        return absent;
    }

    public void setAbsent(float absent) {
        this.absent = absent;
    }

    public float getExcused() {
        return excused;
    }

    public void setExcused(float excused) {
        this.excused = excused;
    }

    public float getLate() {
        return late;
    }

    public void setLate(float late) {
        this.late = late;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = "{\"" + Entity_Name + "\":" + gson.toJson(this) + "}";
        return jsonString;
    }

    public static FinalResult fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject object = json.getJSONObject(Entity_Name);
            Gson gson = new Gson();
            FinalResult finalResult = gson.fromJson(object.toString(), FinalResult.class);
            return finalResult;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
