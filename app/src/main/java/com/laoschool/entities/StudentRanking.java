package com.laoschool.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 7/6/2016.
 */
public class StudentRanking {
//    "id": 1,
//            "school_id": 1,
//            "class_id": 1,
//            "student_id": 10,
//            "student_name": "00000010",
//            "notice": "test data",
//            "sch_year_id": 1,
//            "m1": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m2": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m3": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m4": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m5": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m6": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m7": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m8": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m9": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m10": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m11": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m12": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m13": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m14": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m15": "{\"ave\":\"4,5\",\"grade\":\"A\",\"allocation\":1}",
//            "m16": null,
//            "m17": null,
//            "m18": null,
//            "m19": null,
//            "m20": null,
//            "std_photo": "http://192.168.0.202:9090/eschool_content/avatar/student1.png",
//            "std_nickname": "Boy 10",
//            "std_fullname": "Student 10"

    int id;
    int school_id;
    int class_id;
    int student_id;
    String student_name;
    String notice;
    int sch_year_id;
    String std_photo;
    String std_nickname;
    String std_fullname;
    List<Ranking> rankings;

    public StudentRanking(int id, int school_id, int class_id, int student_id, String student_name, String notice, int sch_year_id, String std_photo, String std_nickname, String std_fullname, List<Ranking> rankings) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.student_id = student_id;
        this.student_name = student_name;
        this.notice = notice;
        this.sch_year_id = sch_year_id;
        this.std_photo = std_photo;
        this.std_nickname = std_nickname;
        this.std_fullname = std_fullname;
        this.rankings = rankings;
    }

    public StudentRanking() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
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

    public int getSch_year_id() {
        return sch_year_id;
    }

    public void setSch_year_id(int sch_year_id) {
        this.sch_year_id = sch_year_id;
    }

    public String getStd_photo() {
        return std_photo;
    }

    public void setStd_photo(String std_photo) {
        this.std_photo = std_photo;
    }

    public String getStd_nickname() {
        return std_nickname;
    }

    public void setStd_nickname(String std_nickname) {
        this.std_nickname = std_nickname;
    }

    public String getStd_fullname() {
        return std_fullname;
    }

    public void setStd_fullname(String std_fullname) {
        this.std_fullname = std_fullname;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public void setRankings(List<Ranking> rankings) {
        this.rankings = rankings;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public static StudentRanking toStudentRanking(JSONObject obj) {
        StudentRanking studentRanking = new StudentRanking();
        try {
            studentRanking.setId(obj.getInt("id"));
            studentRanking.setSchool_id(obj.getInt("school_id"));
            studentRanking.setClass_id(obj.getInt("class_id"));
            studentRanking.setStudent_id(obj.getInt("student_id"));
            studentRanking.setStudent_name(obj.getString("student_name"));
            studentRanking.setStd_fullname(obj.getString("std_fullname"));
            studentRanking.setStd_nickname(obj.getString("std_nickname"));
            studentRanking.setStd_photo(obj.getString("std_photo"));
            List<Ranking> rankings = new ArrayList<>();
            String m1 = "", m2 = "", m3 = "", m4 = "", m5 = "", m6 = "", m7 = "", m8 = "", m9 = "", m10 = "", m11 = "", m12 = "", m13 = "", m14 = "", m15 = "", m16 = "", m17 = "", m18 = "", m19 = "", m20 = "";
            m1 = obj.getString("m1");
            m2 = obj.getString("m2");
            m3 = obj.getString("m3");
            m4 = obj.getString("m4");
            m5 = obj.getString("m5");

            m6 = obj.getString("m6");
            m7 = obj.getString("m7");
            m8 = obj.getString("m8");
            m9 = obj.getString("m9");
            m10 = obj.getString("m10");

            m11 = obj.getString("m11");
            m12 = obj.getString("m12");
            m13 = obj.getString("m13");
            m14 = obj.getString("m14");
            m15 = obj.getString("m15");

            m16 = obj.getString("m16");
            m17 = obj.getString("m17");
            m18 = obj.getString("m18");
            m19 = obj.getString("m19");
            m20 = obj.getString("m20");
            rankings.add(Ranking.toRanking(m1));
            rankings.add(Ranking.toRanking(m2));
            rankings.add(Ranking.toRanking(m3));
            rankings.add(Ranking.toRanking(m4));
            rankings.add(Ranking.toRanking(m5));

            rankings.add(Ranking.toRanking(m6));
            rankings.add(Ranking.toRanking(m7));
            rankings.add(Ranking.toRanking(m8));
            rankings.add(Ranking.toRanking(m9));
            rankings.add(Ranking.toRanking(m10));

            rankings.add(Ranking.toRanking(m11));
            rankings.add(Ranking.toRanking(m12));
            rankings.add(Ranking.toRanking(m13));
            rankings.add(Ranking.toRanking(m14));
            rankings.add(Ranking.toRanking(m15));

            rankings.add(Ranking.toRanking(m16));
            rankings.add(Ranking.toRanking(m17));
            rankings.add(Ranking.toRanking(m18));
            rankings.add(Ranking.toRanking(m19));
            rankings.add(Ranking.toRanking(m20));

            studentRanking.setRankings(rankings);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return studentRanking;
    }
}
