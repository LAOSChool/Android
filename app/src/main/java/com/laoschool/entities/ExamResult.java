package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Tran An on 11/03/2016.
 */
public class ExamResult {

    static final String Entity_Name = "exam_results";

    int id;

    int school_id;

    int class_id;

    int exam_type;

    String exam_dt;

    int subject_id;

    int teacher_id;

    int student_id;

    String student_name;

    String notice;

    int result_type_id;

    int iresult;

    float fresult;

    String sresult;

    int term_id;

    public ExamResult() {
    }

    public ExamResult(int id, int school_id, int class_id, int exam_type, String exam_dt, int subject_id, int teacher_id, int student_id, String student_name, String notice, int result_type_id, int iresult, float fresult, String sresult, int term_id) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.exam_type = exam_type;
        this.exam_dt = exam_dt;
        this.subject_id = subject_id;
        this.teacher_id = teacher_id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.notice = notice;
        this.result_type_id = result_type_id;
        this.iresult = iresult;
        this.fresult = fresult;
        this.sresult = sresult;
        this.term_id = term_id;
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

    public int getExam_type() {
        return exam_type;
    }

    public void setExam_type(int exam_type) {
        this.exam_type = exam_type;
    }

    public String getExam_dt() {
        return exam_dt;
    }

    public void setExam_dt(String exam_dt) {
        this.exam_dt = exam_dt;
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

    public int getResult_type_id() {
        return result_type_id;
    }

    public void setResult_type_id(int result_type_id) {
        this.result_type_id = result_type_id;
    }

    public int getIresult() {
        return iresult;
    }

    public void setIresult(int iresult) {
        this.iresult = iresult;
    }

    public float getFresult() {
        return fresult;
    }

    public void setFresult(float fresult) {
        this.fresult = fresult;
    }

    public String getSresult() {
        return sresult;
    }

    public void setSresult(String sresult) {
        this.sresult = sresult;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = "{\"" + Entity_Name + "\":" + gson.toJson(this) + "}";
        return jsonString;
    }

    public static ExamResult fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject object = json.getJSONObject(Entity_Name);
            Gson gson = new Gson();
            ExamResult examResult = gson.fromJson(object.toString(), ExamResult.class);
            return examResult;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
