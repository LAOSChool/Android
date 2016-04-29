package com.laoschool.entities;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran An on 11/03/2016.
 */
public class User {

    static final String Entity_Name = "user";

    int id;

    String sso_id;

    String password;

    String fullname;

    String nickname;

    String state;

    int school_id;

    String roles;

    UserDetail user_detail;

    Class eclass;

    List<Class> classes;

    public User() {
    }

    public User(int id, String sso_id, String password, String fullname, String nickname, String state, int school_id, String roles, UserDetail userDetail, Class eclass, List<Class> classes) {
        this.id = id;
        this.sso_id = sso_id;
        this.password = password;
        this.fullname = fullname;
        this.nickname = nickname;
        this.state = state;
        this.school_id = school_id;
        this.roles = roles;
        this.user_detail = userDetail;
        this.eclass = eclass;
        this.classes = classes;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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
        User user = new User();
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            user.setId(mainObject.getInt("id"));
            user.setSso_id(mainObject.getString("sso_id"));
            user.setFullname(mainObject.getString("fullname"));
            user.setNickname(mainObject.getString("nickname"));
            user.setState(mainObject.getString("state"));
            user.setSchool_id(mainObject.getInt("school_id"));
            user.setRoles(mainObject.getString("roles"));
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
                eclass.setTerm(eclassObj.getInt("term"));
                eclass.setYears(eclassObj.getString("years"));
                eclass.setStart_dt(eclassObj.getString("start_dt"));
                eclass.setEnd_dt(eclassObj.getString("end_dt"));
                eclass.setHead_teacher_id(eclassObj.getInt("head_teacher_id"));
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
            return user;
        }
    }


}
