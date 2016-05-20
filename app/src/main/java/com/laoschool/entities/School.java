package com.laoschool.entities;

import com.google.gson.Gson;

/**
 * Created by Tran An on 11/03/2016.
 */
public class School {

    static final String Entity_Name = "school";

    int id;

    String title;

    String addr1;

    String addr2;

    int prov_city;

    int county;

    int dist;

    String url;

    String phone;

    String ext;

    String fax;

    int degree;

    String description;

    String principal;

    String found_dt;

    String photo;

    String state;

    public School() {
    }

    public School(int id, String title, String addr1, String addr2, int prov_city, int county, int dist, String url, String phone, String ext, String fax, int degree, String description, String principal, String found_dt, String photo, String state) {
        this.id = id;
        this.title = title;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.prov_city = prov_city;
        this.county = county;
        this.dist = dist;
        this.url = url;
        this.phone = phone;
        this.ext = ext;
        this.fax = fax;
        this.degree = degree;
        this.description = description;
        this.principal = principal;
        this.found_dt = found_dt;
        this.photo = photo;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getProv_city() {
        return prov_city;
    }

    public void setProv_city(int prov_city) {
        this.prov_city = prov_city;
    }

    public int getCounty() {
        return county;
    }

    public void setCounty(int county) {
        this.county = county;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getFound_dt() {
        return found_dt;
    }

    public void setFound_dt(String found_dt) {
        this.found_dt = found_dt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static School fromJson(String jsonString) {
        Gson gson = new Gson();
        School school = gson.fromJson(jsonString, School.class);
        return school;
    }
}
