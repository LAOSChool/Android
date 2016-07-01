package com.laoschool.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.laoschool.shared.LaoSchoolShared;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tran An on 11/03/2016.
 */
public class ExamResult implements Parcelable, Comparable<ExamResult> {
//             "id": 1,
//            "school_id": 1,
//            "class_id": 1,
//            "student_id": 10,
//            "student_name": "00000010",
//            "subject_id": 1,
//            "subject_name": "Toan",
//            "notice": null,
//            "sch_year_id": 2,
//            "m1": "3.8",
//            "m2": "4.6@2016-06-29",
//            "m3": null,
//            "m4": null,
//            "m5": "4.2",
//            "m6": null,
//            "m7": null,
//            "m8": null,
//            "m9": null,
//            "m10": null,
//            "m11": null,
//            "m12": null,
//            "m13": null,
//            "m14": null,
//            "m15": null,
//            "m16": null,
//            "m17": null,
//            "m18": null,
//            "m19": null,
//            "m20": null,
//            "std_photo": "http://192.168.0.202:9090/eschool_content/avatar/student1.png",
//            "std_nickname": "Student 10"


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

    String term;

    String subject_name;

    int exam_month;

    int exam_year;

    String teacher;

    int exam_id;

    String std_photo;

    String std_nickname;

    String exam_name;

    String m1;

    String m2;
    String m3;
    String m4;
    String m5;
    String m6;
    String m7;
    String m8;
    String m9;
    String m10;
    String m11;
    String m12;
    String m13;

    String m14;
    String m15;
    String m16;
    String m17;
    String m18;
    String m19;
    String m20;

//            "m2": "4.6@2016-06-29",
//            "m3": null,
//            "m4": null,
//            "m5": "4.2",
//            "m6": null,
//            "m7": null,
//            "m8": null,
//            "m9": null,
//            "m10": null,
//            "m11": null,
//            "m12": null,
//            "m13": null,
//            "m14": null,
//            "m15": null,
//            "m16": null,
//            "m17": null,
//            "m18": null,
//            "m19": null,
//            "m20": null,


    public ExamResult() {
    }

    public ExamResult(int id, int school_id, int class_id, int exam_type, String exam_dt, int subject_id, int teacher_id, int student_id, String student_name, String notice, int result_type_id, int iresult, float fresult, String sresult, int term_id, String term, String subject, int exam_month, int exam_year, String teacher, int exam_id, String std_photo, String std_nickname, String exam_name, String m1, String m2, String m3, String m4, String m5, String m6, String m7, String m8, String m9, String m10, String m11, String m12, String m13, String m14, String m15, String m16, String m17, String m18, String m19, String m20) {
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
        this.term = term;
        this.subject_name = subject;
        this.exam_month = exam_month;
        this.exam_year = exam_year;
        this.teacher = teacher;
        this.exam_id = exam_id;
        this.std_photo = std_photo;
        this.std_nickname = std_nickname;
        this.exam_name = exam_name;
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.m4 = m4;
        this.m5 = m5;
        this.m6 = m6;
        this.m7 = m7;
        this.m8 = m8;
        this.m9 = m9;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m14 = m14;
        this.m15 = m15;
        this.m16 = m16;
        this.m17 = m17;
        this.m18 = m18;
        this.m19 = m19;
        this.m20 = m20;
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

    public String getTermName() {
        return term;
    }

    public void setTermName(String term) {
        this.term = term;
    }

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject) {
        this.subject_name = subject;
    }

    public int getExam_month() {
        return exam_month;
    }

    public void setExam_month(int exam_month) {
        this.exam_month = exam_month;
    }

    public int getExam_year() {
        return exam_year;
    }

    public void setExam_year(int exam_year) {
        this.exam_year = exam_year;
    }

    public String getTeacherName() {
        return teacher;
    }

    public void setTeacherName(String teacher) {
        this.teacher = teacher;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
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

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getM2() {
        return m2;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public String getM3() {
        return m3;
    }

    public void setM3(String m3) {
        this.m3 = m3;
    }

    public String getM4() {
        return m4;
    }

    public void setM4(String m4) {
        this.m4 = m4;
    }

    public String getM5() {
        return m5;
    }

    public void setM5(String m5) {
        this.m5 = m5;
    }

    public String getM6() {
        return m6;
    }

    public void setM6(String m6) {
        this.m6 = m6;
    }

    public String getM7() {
        return m7;
    }

    public void setM7(String m7) {
        this.m7 = m7;
    }

    public String getM8() {
        return m8;
    }

    public void setM8(String m8) {
        this.m8 = m8;
    }

    public String getM9() {
        return m9;
    }

    public void setM9(String m9) {
        this.m9 = m9;
    }

    public String getM10() {
        return m10;
    }

    public void setM10(String m10) {
        this.m10 = m10;
    }

    public String getM11() {
        return m11;
    }

    public void setM11(String m11) {
        this.m11 = m11;
    }

    public String getM12() {
        return m12;
    }

    public void setM12(String m12) {
        this.m12 = m12;
    }

    public String getM13() {
        return m13;
    }

    public void setM13(String m13) {
        this.m13 = m13;
    }

    public String getM14() {
        return m14;
    }

    public void setM14(String m14) {
        this.m14 = m14;
    }

    public String getM15() {
        return m15;
    }

    public void setM15(String m15) {
        this.m15 = m15;
    }

    public String getM16() {
        return m16;
    }

    public void setM16(String m16) {
        this.m16 = m16;
    }

    public String getM17() {
        return m17;
    }

    public void setM17(String m17) {
        this.m17 = m17;
    }

    public String getM18() {
        return m18;
    }

    public void setM18(String m18) {
        this.m18 = m18;
    }

    public String getM19() {
        return m19;
    }

    public void setM19(String m19) {
        this.m19 = m19;
    }

    public String getM20() {
        return m20;
    }

    public void setM20(String m20) {
        this.m20 = m20;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static ExamResult fromJson(String jsonString) {
        Gson gson = new Gson();
        ExamResult examResult = gson.fromJson(jsonString, ExamResult.class);
        return examResult;
    }


    @Override
    public String toString() {
        return "ExamResult{" +
                "school_id=" + school_id +
                ", term_id=" + term_id +
                ", class_id=" + class_id +
                ", subject_id=" + subject_id +
                ", teacher_id=" + teacher_id +
                ", student_id=" + student_id +
                ", exam_id=" + exam_id +
                ", sresult='" + sresult + '\'' +
                '}';
    }

    protected ExamResult(Parcel in) {

        id = in.readInt();
        school_id = in.readInt();
        class_id = in.readInt();
        exam_type = in.readInt();
        exam_dt = in.readString();
        subject_id = in.readInt();
        teacher_id = in.readInt();
        student_id = in.readInt();
        student_name = in.readString();
        notice = in.readString();
        result_type_id = in.readInt();
        iresult = in.readInt();
        fresult = in.readFloat();
        sresult = in.readString();
        term_id = in.readInt();
        term = in.readString();
        subject_name = in.readString();
        exam_month = in.readInt();
        exam_year = in.readInt();
        teacher = in.readString();
        exam_id = in.readInt();
        std_photo = in.readString();
        std_nickname = in.readString();
        exam_name = in.readString();

        m1 = in.readString();
        m2 = in.readString();
        m3 = in.readString();
        m4 = in.readString();
        m5 = in.readString();

        m6 = in.readString();
        m7 = in.readString();
        m8 = in.readString();
        m9 = in.readString();
        m10 = in.readString();

        m11 = in.readString();
        m12 = in.readString();
        m13 = in.readString();
        m14 = in.readString();
        m15 = in.readString();

        m16 = in.readString();
        m17 = in.readString();
        m18 = in.readString();
        m19 = in.readString();
        m20 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Entity_Name);
        dest.writeInt(id);
        dest.writeInt(school_id);
        dest.writeInt(class_id);
        dest.writeInt(exam_type);
        dest.writeString(exam_dt);
        dest.writeInt(subject_id);
        dest.writeInt(teacher_id);
        dest.writeInt(student_id);
        dest.writeString(student_name);
        dest.writeString(notice);
        dest.writeInt(result_type_id);
        dest.writeInt(iresult);
        dest.writeFloat(fresult);
        dest.writeString(sresult);
        dest.writeInt(term_id);
        dest.writeString(term);
        dest.writeString(subject_name);
        dest.writeInt(exam_month);
        dest.writeInt(exam_year);
        dest.writeString(teacher);
        dest.writeInt(exam_id);
        dest.writeString(std_photo);
        dest.writeString(std_nickname);
        dest.writeString(exam_name);

        dest.writeString(m1);
        dest.writeString(m2);
        dest.writeString(m3);
        dest.writeString(m4);
        dest.writeString(m5);

        dest.writeString(m6);
        dest.writeString(m7);
        dest.writeString(m8);
        dest.writeString(m9);
        dest.writeString(m10);

        dest.writeString(m11);
        dest.writeString(m12);
        dest.writeString(m13);
        dest.writeString(m14);
        dest.writeString(m15);

        dest.writeString(m16);
        dest.writeString(m17);
        dest.writeString(m18);
        dest.writeString(m19);
        dest.writeString(m20);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExamResult> CREATOR = new Parcelable.Creator<ExamResult>() {
        @Override
        public ExamResult createFromParcel(Parcel in) {
            return new ExamResult(in);
        }

        @Override
        public ExamResult[] newArray(int size) {
            return new ExamResult[size];
        }
    };

    public String toCreateJsonString() {
//        {
//            "m6": {“sresult":"10","exam_dt":"2016-06-30","notice":"Comment"},
//            "class_id":"1",
//                    "subject_id":"1",
//                    "school_id":1,
//                    "student_id":"10"
//        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("school_id", school_id);
        jsonObject.addProperty("class_id", LaoSchoolShared.myProfile.getEclass().getId());

        jsonObject.addProperty("student_id", student_id);
        jsonObject.addProperty("subject_id", subject_id);

        if (notice != null)
            jsonObject.addProperty("notice", notice);
        if (m1 != null) jsonObject.addProperty("m1", m1);
        if (m2 != null) jsonObject.addProperty("m2", m2);
        if (m3 != null) jsonObject.addProperty("m3", m3);
        if (m4 != null) jsonObject.addProperty("m4", m4);
        if (m6 != null) jsonObject.addProperty("m6", m6);
        if (m7 != null) jsonObject.addProperty("m7", m7);
        if (m8 != null) jsonObject.addProperty("m8", m8);
        if (m9 != null) jsonObject.addProperty("m9", m9);
        if (m10 != null) jsonObject.addProperty("m10", m10);
        if (m12 != null) jsonObject.addProperty("m12", m12);


        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        //Log.d("", "toCreateJsonString():" + jsonString);
        return jsonString;
    }

    public JSONObject toCreateJson() {
//        {
//            "m6": {“sresult":"10","exam_dt":"2016-06-30","notice":"Comment"},
//            "class_id":"1",
//                    "subject_id":"1",
//                    "school_id":1,
//                    "student_id":"10"
//        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("school_id", school_id);
            jsonObject.put("class_id", LaoSchoolShared.myProfile.getEclass().getId());

            jsonObject.put("student_id", student_id);
            jsonObject.put("subject_id", subject_id);

            if (notice != null)
                jsonObject.put("notice", notice);
            if (m1 != null) jsonObject.put("m1", m1);
            if (m2 != null) jsonObject.put("m2", m2);
            if (m3 != null) jsonObject.put("m3", m3);
            if (m4 != null) jsonObject.put("m4", m4);
            if (m6 != null) jsonObject.put("m6", m6);
            if (m7 != null) jsonObject.put("m7", m7);
            if (m8 != null) jsonObject.put("m8", m8);
            if (m9 != null) jsonObject.put("m9", m9);
            if (m10 != null) jsonObject.put("m10", m10);
            if (m12 != null)
                jsonObject.put("m12", m12);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public int compareTo(ExamResult examResult) {
        int student_id = ((ExamResult) examResult).getStudent_id();

        //ascending order
        return this.student_id - student_id;
    }
}