package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

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

    public User() {
    }

    public User(int id, String sso_id, String password, String fullname, String nickname, String state, int school_id, String roles, UserDetail userDetail) {
        this.id = id;
        this.sso_id = sso_id;
        this.password = password;
        this.fullname = fullname;
        this.nickname = nickname;
        this.state = state;
        this.school_id = school_id;
        this.roles = roles;
        this.user_detail = userDetail;
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

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = "{\"" + Entity_Name + "\":" + gson.toJson(this) + "}";
        return jsonString;
    }

    public static User fromJson(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject object = json.getJSONObject(Entity_Name);
            Gson gson = new Gson();
            User user = gson.fromJson(object.toString(), User.class);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
