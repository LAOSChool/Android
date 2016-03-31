package com.laoschool.entities;

import com.google.gson.Gson;

/**
 * Created by Tran An on 23/03/2016.
 */
public class UserDetail {

    int id;

    String addr1;

    String addr2;

    String phone;

    String ext;

    String photo;

    String birthday;

    String gender;

    String email;

    String std_contact_name;

    String std_contact_phone;

    String std_contact_email;

    String std_payment_dt;

    String std_valid_through_dt;

    String std_graduation_dt;

    String std_mother_name;

    String std_father_name;

    public UserDetail() {
    }

    public UserDetail(int id, String addr1, String addr2, String phone, String ext, String photo, String birthday, String gender, String email, String std_contact_name, String std_contact_phone, String std_contact_email, String std_payment_dt, String std_valid_through_dt, String std_graduation_dt, String std_mother_name, String std_father_name) {
        this.id = id;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.phone = phone;
        this.ext = ext;
        this.photo = photo;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.std_contact_name = std_contact_name;
        this.std_contact_phone = std_contact_phone;
        this.std_contact_email = std_contact_email;
        this.std_payment_dt = std_payment_dt;
        this.std_valid_through_dt = std_valid_through_dt;
        this.std_graduation_dt = std_graduation_dt;
        this.std_mother_name = std_mother_name;
        this.std_father_name = std_father_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStd_contact_name() {
        return std_contact_name;
    }

    public void setStd_contact_name(String std_contact_name) {
        this.std_contact_name = std_contact_name;
    }

    public String getStd_contact_phone() {
        return std_contact_phone;
    }

    public void setStd_contact_phone(String std_contact_phone) {
        this.std_contact_phone = std_contact_phone;
    }

    public String getStd_contact_email() {
        return std_contact_email;
    }

    public void setStd_contact_email(String std_contact_email) {
        this.std_contact_email = std_contact_email;
    }

    public String getStd_payment_dt() {
        return std_payment_dt;
    }

    public void setStd_payment_dt(String std_payment_dt) {
        this.std_payment_dt = std_payment_dt;
    }

    public String getStd_valid_through_dt() {
        return std_valid_through_dt;
    }

    public void setStd_valid_through_dt(String std_valid_through_dt) {
        this.std_valid_through_dt = std_valid_through_dt;
    }

    public String getStd_graduation_dt() {
        return std_graduation_dt;
    }

    public void setStd_graduation_dt(String std_graduation_dt) {
        this.std_graduation_dt = std_graduation_dt;
    }

    public String getStd_mother_name() {
        return std_mother_name;
    }

    public void setStd_mother_name(String std_mother_name) {
        this.std_mother_name = std_mother_name;
    }

    public String getStd_father_name() {
        return std_father_name;
    }

    public void setStd_father_name(String std_father_name) {
        this.std_father_name = std_father_name;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static UserDetail fromJson(String jsonString) {
        Gson gson = new Gson();
        UserDetail userDetail = gson.fromJson(jsonString, UserDetail.class);
        return userDetail;
    }
}
