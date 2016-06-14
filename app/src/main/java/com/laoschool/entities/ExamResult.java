package com.laoschool.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Tran An on 11/03/2016.
 */
public class ExamResult implements Parcelable, Comparable<ExamResult> {
//            "id": null,
//            "school_id": 1,
//            "class_id": 1,
//            "exam_id": 1,
//            "exam_name": "Normal exam",
//            "exam_type": 1,
//            "exam_dt": null,
//            "subject_id": 2,
//            "teacher_id": null,
//            "student_id": 10,
//            "student_name": "00000010",
//            "notice": "BLANK",
//            "sresult": null,
//            "term_id": 1,
//            "exam_month": 9,
//            "exam_year": null,
//            "term_val": 1,
//            "sch_year_id": 1,
//            "subject": "Ly",
//            "teacher": null,
//            "std_photo": "http://192.168.0.202:9090/eschool_content/avatar/student1.png",
//            "std_nickname": "Student 10",
//            "term": "HK 1"

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

    String subject;

    int exam_month;

    int exam_year;

    String teacher;

    int exam_id;

    String std_photo;

    String std_nickname;

    String exam_name;


    public ExamResult() {
    }

    public ExamResult(int id, int school_id, int class_id, int exam_type, String exam_dt, int subject_id, int teacher_id, int student_id, String student_name, String notice, int result_type_id, int iresult, float fresult, String sresult, int term_id, String term, String subject, int exam_month, int exam_year, String teacher, int exam_id, String std_photo, String std_nickname, String exam_name) {
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
        this.subject = subject;
        this.exam_month = exam_month;
        this.exam_year = exam_year;
        this.teacher = teacher;
        this.exam_id = exam_id;
        this.std_photo = std_photo;
        this.std_nickname = std_nickname;
        this.exam_name = exam_name;
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
        return subject;
    }

    public void setSubjectName(String subject) {
        this.subject = subject;
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
        subject = in.readString();
        exam_month = in.readInt();
        exam_year = in.readInt();
        teacher = in.readString();
        exam_id = in.readInt();
        std_photo = in.readString();
        std_nickname = in.readString();
        exam_name = in.readString();
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
        dest.writeString(subject);
        dest.writeInt(exam_month);
        dest.writeInt(exam_year);
        dest.writeString(teacher);
        dest.writeInt(exam_id);
        dest.writeString(std_photo);
        dest.writeString(std_nickname);
        dest.writeString(exam_name);
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

    public String toCreateJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("school_id", school_id);
        jsonObject.addProperty("class_id", class_id);
        jsonObject.addProperty("sresult", sresult);

        jsonObject.addProperty("student_id", student_id);
        jsonObject.addProperty("subject_id", subject_id);

        jsonObject.addProperty("exam_id", exam_id);
        jsonObject.addProperty("teacher_id", teacher_id);
        jsonObject.addProperty("term_id", term_id);
        jsonObject.addProperty("notice", notice);

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        Log.d("", "toCreateJson():" + jsonString);
        return jsonString;
    }

    @Override
    public int compareTo(ExamResult examResult) {
        int exam_id = ((ExamResult) examResult).getExam_id();

        //ascending order
        return this.exam_id - exam_id;
    }
}