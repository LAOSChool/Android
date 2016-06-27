package com.laoschool.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 11/03/2016.
 */
public class FinalResult implements Parcelable {

    //    "id": 4,
//            "school_id": 1,
//            "school_name": "Truong Tieu Hoc Thanh Xuan Trung",
//            "cls_id": 1,
//            "cls_name": "1A1",
//            "cls_level": 1,
//            "cls_location": "1A1room - floor 2",
//            "teacher_id": 5,
//            "teacher_name": "Teacher class 1",
//            "student_id": 11,
//            "student_name": "Student 11",
//            "school_year_id": 1,
//            "school_years": "2016-2017",
//            "day_absents": 0,
//            "passed": 1,
//            "eval_dt": null,
//            "start_dt": "2016-06-10 16:12:08.0",
//            "closed_dt": null,
//            "closed": 0,
//            "notice": null
    int id;

    int school_id;

    String school_name;

    int cls_id;

    String cls_name;

    int cls_level;

    String cls_location;

    int teacher_id;

    String teacher_name;

    int student_id;

    String student_name;

    int school_year_id;

    String school_years;

    int day_absents;

    int passed;

    String start_dt;

    String eval_dt;

    String closed_dt;

    int closed;

    String notice;

    List<ExamResult> exam_results;

    public FinalResult(int id, int school_id, String school_name, int cls_id, String cls_name, int cls_level, String cls_location, int teacher_id, String teacher_name, int student_id, String student_name, int school_year_id, String school_years, int day_absents, int passed, String start_dt, String eval_dt, String closed_dt, int closed, String notice, List<ExamResult> exam_results) {
        this.id = id;
        this.school_id = school_id;
        this.school_name = school_name;
        this.cls_id = cls_id;
        this.cls_name = cls_name;
        this.cls_level = cls_level;
        this.cls_location = cls_location;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.student_id = student_id;
        this.student_name = student_name;
        this.school_year_id = school_year_id;
        this.school_years = school_years;
        this.day_absents = day_absents;
        this.passed = passed;
        this.start_dt = start_dt;
        this.eval_dt = eval_dt;
        this.closed_dt = closed_dt;
        this.closed = closed;
        this.notice = notice;
        this.exam_results = exam_results;
    }

    public FinalResult() {

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

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public int getCls_id() {
        return cls_id;
    }

    public void setClassId(int cls_id) {
        this.cls_id = cls_id;
    }

    public String getClassName() {
        return cls_name;
    }

    public void setClass_name(String cls_name) {
        this.cls_name = cls_name;
    }

    public int getCls_level() {
        return cls_level;
    }

    public void setClass_level(int cls_level) {
        this.cls_level = cls_level;
    }

    public String getCls_location() {
        return cls_location;
    }

    public void setClass_location(String cls_location) {
        this.cls_location = cls_location;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
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

    public int getSchool_year_id() {
        return school_year_id;
    }

    public void setSchool_year_id(int school_year_id) {
        this.school_year_id = school_year_id;
    }

    public String getSchool_years() {
        return school_years;
    }

    public void setSchool_years(String school_years) {
        this.school_years = school_years;
    }

    public int getDay_absents() {
        return day_absents;
    }

    public void setDay_absents(int day_absents) {
        this.day_absents = day_absents;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEval_dt() {
        return eval_dt;
    }

    public void setEval_dt(String eval_dt) {
        this.eval_dt = eval_dt;
    }

    public String getClosed_dt() {
        return closed_dt;
    }

    public void setClosed_dt(String closed_dt) {
        this.closed_dt = closed_dt;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<ExamResult> getExam_results() {
        return exam_results;
    }

    public void setExam_results(List<ExamResult> exam_results) {
        this.exam_results = exam_results;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static FinalResult fromJson(String jsonString) {
        Gson gson = new Gson();
        FinalResult finalResult = gson.fromJson(jsonString, FinalResult.class);
        List<ExamResult> examResults = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject finalJson = jsonObject.getJSONObject("messageObject");
            JSONArray examArray = finalJson.getJSONArray("exam_results");
            for (int i = 0; i < examArray.length(); i++) {
                JSONObject examJson = examArray.getJSONObject(i);
                examResults.add(ExamResult.fromJson(examJson.toString()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finalResult.setExam_results(examResults);
        return finalResult;
    }

    public static FinalResult fromJson(JSONObject response) {
//                {
//            "id": 4,
//                "school_id": 1,
//                "school_name": "Truong Tieu Hoc Thanh Xuan Trung",
//                "cls_id": 1,
//                "cls_name": "1A1",
//                "cls_level": 1,
//                "cls_location": "1A1room - floor 2",
//                "teacher_id": 5,
//                "teacher_name": "Teacher class 1",
//                "student_id": 11,
//                "student_name": "Student 11",
//                "school_year_id": 1,
//                "school_years": "2016-2017",
//                "day_absents": 0,
//                "passed": 1,
//                "eval_dt": null,
//                "start_dt": "2016-06-10 16:12:08.0",
//                "closed_dt": null,
//                "closed": 0,
//                "notice": null
//        }
        FinalResult finalResult = new FinalResult();
        List<ExamResult> examResults = new ArrayList<>();
        try {

            JSONObject finalJson = response.getJSONObject("messageObject");
            JSONArray examArray = finalJson.getJSONArray("exam_results");
            for (int i = 0; i < examArray.length(); i++) {
                JSONObject examJson = examArray.getJSONObject(i);
                examResults.add(ExamResult.fromJson(examJson.toString()));
            }
            finalResult.setId(finalJson.getInt("id"));

            finalResult.setSchool_id(finalJson.getInt("school_id"));

            finalResult.setSchool_name(finalJson.getString("school_name"));

            finalResult.setClassId(finalJson.getInt("cls_id"));


            finalResult.setClass_name(finalJson.getString("cls_name"));

            finalResult.setClass_level(finalJson.getInt("cls_level"));

            finalResult.setClass_location(finalJson.getString("cls_location"));

            finalResult.setTeacher_id(finalJson.getInt("teacher_id"));


            finalResult.setTeacher_name(finalJson.getString("teacher_name"));

            finalResult.setStudent_id(finalJson.getInt("student_id"));

            finalResult.setStudent_name(finalJson.getString("student_name"));

            finalResult.setSchool_year_id(finalJson.getInt("school_year_id"));


            finalResult.setSchool_years(finalJson.getString("school_years"));

            finalResult.setDay_absents(finalJson.getInt("day_absents"));

            finalResult.setPassed(finalJson.getInt("passed"));

            finalResult.setEval_dt(finalJson.getString("eval_dt"));


            finalResult.setStart_dt(finalJson.getString("start_dt"));

            finalResult.setClosed_dt(finalJson.getString("closed_dt"));

            finalResult.setClosed(finalJson.getInt("closed"));

            finalResult.setNotice(finalJson.getString("notice"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finalResult.setExam_results(examResults);
        return finalResult;
    }

    protected FinalResult(Parcel in) {
        id = in.readInt();
        school_id = in.readInt();
        school_name = in.readString();
        cls_id = in.readInt();
        cls_name = in.readString();
        cls_level = in.readInt();
        cls_location = in.readString();
        teacher_id = in.readInt();
        teacher_name = in.readString();
        student_id = in.readInt();
        student_name = in.readString();
        school_year_id = in.readInt();
        school_years = in.readString();
        day_absents = in.readInt();
        passed = in.readInt();
        start_dt = in.readString();
        eval_dt = in.readString();
        closed_dt = in.readString();
        closed = in.readInt();
        notice = in.readString();
        if (in.readByte() == 0x01) {
            exam_results = new ArrayList<ExamResult>();
            in.readList(exam_results, ExamResult.class.getClassLoader());
        } else {
            exam_results = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(school_id);
        dest.writeString(school_name);
        dest.writeInt(cls_id);
        dest.writeString(cls_name);
        dest.writeInt(cls_level);
        dest.writeString(cls_location);
        dest.writeInt(teacher_id);
        dest.writeString(teacher_name);
        dest.writeInt(student_id);
        dest.writeString(student_name);
        dest.writeInt(school_year_id);
        dest.writeString(school_years);
        dest.writeInt(day_absents);
        dest.writeInt(passed);
        dest.writeString(start_dt);
        dest.writeString(eval_dt);
        dest.writeString(closed_dt);
        dest.writeInt(closed);
        dest.writeString(notice);
        if (exam_results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(exam_results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FinalResult> CREATOR = new Parcelable.Creator<FinalResult>() {
        @Override
        public FinalResult createFromParcel(Parcel in) {
            return new FinalResult(in);
        }

        @Override
        public FinalResult[] newArray(int size) {
            return new FinalResult[size];
        }
    };
}