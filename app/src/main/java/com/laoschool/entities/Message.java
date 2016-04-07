package com.laoschool.entities;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tran An on 23/03/2016.
 */
public class Message {

    static final String Entity_Name = "message";

    int id;

    int school_id;

    int class_id;

    int from_usr_id;

    String from_user_name;

    int to_usr_id;

    String to_user_name;

    String content;

    int channel;

    int is_sent;

    String sent_dt;

    int is_read;

    String read_dt;

    String other;

    int msg_type_id;

    int imp_flg;

    String title;

    String schoolName;

    String messageType;


    public Message() {
    }

    public Message(int channel, int class_id, String content, String from_user_name, int from_usr_id, int id, int imp_flg, int is_read, int is_sent, String messageType, int msg_type_id, String other, String read_dt, int school_id, String schoolName, String sent_dt, String title, String to_user_name, int to_usr_id) {
        this.channel = channel;
        this.class_id = class_id;
        this.content = content;
        this.from_user_name = from_user_name;
        this.from_usr_id = from_usr_id;
        this.id = id;
        this.imp_flg = imp_flg;
        this.is_read = is_read;
        this.is_sent = is_sent;
        this.messageType = messageType;
        this.msg_type_id = msg_type_id;
        this.other = other;
        this.read_dt = read_dt;
        this.school_id = school_id;
        this.schoolName = schoolName;
        this.sent_dt = sent_dt;
        this.title = title;
        this.to_user_name = to_user_name;
        this.to_usr_id = to_usr_id;
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

    public int getFrom_usr_id() {
        return from_usr_id;
    }

    public void setFrom_usr_id(int from_usr_id) {
        this.from_usr_id = from_usr_id;
    }

    public int getTo_usr_id() {
        return to_usr_id;
    }

    public void setTo_usr_id(int to_usr_id) {
        this.to_usr_id = to_usr_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getIs_sent() {
        return is_sent;
    }

    public void setIs_sent(int is_sent) {
        this.is_sent = is_sent;
    }

    public String getSent_dt() {
        return sent_dt;
    }

    public void setSent_dt(String sent_dt) {
        this.sent_dt = sent_dt;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getRead_dt() {
        return read_dt;
    }

    public void setRead_dt(String read_dt) {
        this.read_dt = read_dt;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public int getImp_flg() {
        return imp_flg;
    }

    public void setImp_flg(int imp_flg) {
        this.imp_flg = imp_flg;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getMsg_type_id() {
        return msg_type_id;
    }

    public void setMsg_type_id(int msg_type_id) {
        this.msg_type_id = msg_type_id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static Message fromJson(String jsonString) {
        Gson gson = new Gson();
        Message message = gson.fromJson(jsonString, Message.class);
        return message;
    }
}
