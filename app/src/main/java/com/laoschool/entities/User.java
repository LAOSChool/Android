package com.laoschool.entities;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Tran An on 11/03/2016.
 */
public class User implements Comparable<User>{

    static final String Entity_Name = "user";
    public static final String USER_ROLE_STUDENT = "STUDENT";
    public static final String USER_ROLE_TEACHER = "TEACHER";

    int id;

    String sso_id;

    String password;

    String fullname;

    String nickname;

    String phone;

    String state;

    int school_id;

    String schoolName;

    String photo;

    String roles;

    String gender;

    UserDetail user_detail;

    Class eclass;

    List<Class> classes;

    transient Bitmap userPhoto;

    public User() {
    }

    public User(int id, String sso_id, String password, String fullname, String nickname, String state, int school_id, String roles, UserDetail user_detail, Class eclass, List<Class> classes, String schoolName) {
        this.id = id;
        this.sso_id = sso_id;
        this.password = password;
        this.fullname = fullname;
        this.nickname = nickname;
        this.state = state;
        this.school_id = school_id;
        this.roles = roles;
        this.user_detail = user_detail;
        this.eclass = eclass;
        this.classes = classes;
        this.schoolName = schoolName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSso_id() {
        return sso_id;
    }

    public void setSso_id(String sso_id) {
        this.sso_id = sso_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserDetail getUserDetail() {
        return user_detail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.user_detail = userDetail;
    }

    public Class getEclass() {
        return eclass;
    }

    public void setEclass(Class eclass) {
        this.eclass = eclass;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static User fromJson(String jsonString) {
        Gson gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        return user;
    }


    public static User parseFromJson(String jsonString) {
        try {
            User user = new User();
            JSONObject mainObject = new JSONObject(jsonString);
            user.setId(mainObject.getInt("id"));
            user.setSso_id(mainObject.getString("sso_id"));
            user.setFullname(mainObject.getString("fullname"));
            user.setNickname(mainObject.getString("nickname"));
            user.setPhone(mainObject.getString("phone"));
            user.setState(mainObject.getString("state"));
            user.setSchool_id(mainObject.getInt("school_id"));
            user.setRoles(mainObject.getString("roles"));
            user.setSchoolName(mainObject.getString("schoolName"));
            user.setPhoto(mainObject.getString("photo"));
            user.setGender(mainObject.getString("gender"));
            // User detail
            UserDetail userDetail = new UserDetail();
            userDetail.setAddr1(mainObject.getString("addr1"));
            userDetail.setAddr2(mainObject.getString("addr2"));
            userDetail.setPhone(mainObject.getString("phone"));
            userDetail.setExt(mainObject.getString("ext"));
            userDetail.setPhoto(mainObject.getString("photo"));
            userDetail.setBirthday(mainObject.getString("birthday"));
            userDetail.setGender(mainObject.getString("gender"));
            userDetail.setEmail(mainObject.getString("email"));
            // Class
            JSONArray classarray = mainObject.getJSONArray("classes");
            List<Class> classes = new ArrayList<Class>();
            for (int i = 0; i < classarray.length(); i++) {
                JSONObject eclassObj = classarray.getJSONObject(i);
                Class eclass = new Class();
                eclass.setId(eclassObj.getInt("id"));
                eclass.setSchool_id(eclassObj.getInt("school_id"));
                eclass.setTitle(eclassObj.getString("title"));
                eclass.setLocation(eclassObj.getString("location"));
                try {
                    eclass.setTerm(eclassObj.getInt("term"));
                } catch (JSONException e) {
                    Log.w("UserProfile", "term in class is null");
                }
                eclass.setYears(eclassObj.getString("years"));
                eclass.setStart_dt(eclassObj.getString("start_dt"));
                eclass.setEnd_dt(eclassObj.getString("end_dt"));
                eclass.setHead_teacher_id(eclassObj.getInt("head_teacher_id"));
                eclass.setHeadTeacherName(eclassObj.getString("headTeacherName"));
                classes.add(eclass);
                Log.d("", eclass.toString());
            }

            //user.setEclass(eclass);
            user.setUserDetail(userDetail);
            user.setClasses(classes);
            if (classes.size() > 0)
                user.setEclass(classes.get(0));
            // Return user
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Override
//    public boolean equals(Object other) {
//        return (other != null && other instanceof User && ((User) other).id == id);
//    }

    public int compareTo(User user) {

        int userId = ((User) user).getId();

        //ascending order
        return this.id - userId;

        //descending order
        //return userId - this.quantity;

    }


}
