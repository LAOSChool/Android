package com.laoschool.entities;

import android.provider.BaseColumns;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    String cc_list;

    int cId;

    public Message() {
    }

    public Message(int id, int school_id, int class_id, int from_usr_id, String from_user_name, int to_usr_id, String to_user_name, String content, int channel, int is_sent, String sent_dt, int is_read, String read_dt, String other, int msg_type_id, int imp_flg, String title, String schoolName, String messageType, String cc_list, int aId) {
        this.id = id;
        this.school_id = school_id;
        this.class_id = class_id;
        this.from_usr_id = from_usr_id;
        this.from_user_name = from_user_name;
        this.to_usr_id = to_usr_id;
        this.to_user_name = to_user_name;
        this.content = content;
        this.channel = channel;
        this.is_sent = is_sent;
        this.sent_dt = sent_dt;
        this.is_read = is_read;
        this.read_dt = read_dt;
        this.other = other;
        this.msg_type_id = msg_type_id;
        this.imp_flg = imp_flg;
        this.title = title;
        this.schoolName = schoolName;
        this.messageType = messageType;
        this.cc_list = cc_list;
        this.cId = aId;
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

    public String getCc_list() {
        return cc_list;
    }

    public void setCc_list(String cc_list) {
        this.cc_list = cc_list;
    }

    public int getClienId() {
        return cId;
    }

    public void setClienId(int cId) {
        this.cId = cId;
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

    public static Message parsefromJson(String jsonString) {
        Message message = new Message();
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            message = fromJson(mainObject.getJSONObject("messageObject").toString());
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
            return message;
        }
    }


    /* Inner class that defines the table contents */
    public static abstract class MessageColumns implements BaseColumns {

        public static final String TABLE_NAME = "messages";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SCHOOL_ID = "school_id";
        public static final String COLUMN_NAME_CLASS_ID = "class_id";

        public static final String COLUMN_NAME_FROM_USR_ID = "from_usr_id";
        public static final String COLUMN_NAME_FROM_USER_NAME = "from_user_name";

        public static final String COLUMN_NAME_TO_USR_ID = "to_usr_id";
        public static final String COLUMN_NAME_TO_USER_NAME = "to_user_name";

        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_MSG_TYPE_ID = "msg_type_id";
        public static final String COLUMN_NAME_CHANNEL = "channel";

        public static final String COLUMN_NAME_IS_SENT = "is_sent";
        public static final String COLUMN_NAME_IS_READ = "is_read";

        public static final String COLUMN_NAME_SENT_DT = "sent_dt";
        public static final String COLUMN_NAME_READ_DT = "read_dt";

        public static final String COLUMN_NAME_IMP_FLG = "imp_flg";

        public static final String COLUMN_NAME_OTHER = "other";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CC_LIST = "cc_list";
        public static final String COLUMN_NAME_SCHOOL_NAME = "schoolName";
        public static final String COLUMN_NAME_MESSAGE_TYPE = "messageType";
        public static final String COLUMN_NAME_CLIENT_ID = "cId";

        public static final int COLUMN_NAME_ID_INDEX_0 = 0;
        public static final int COLUMN_NAME_SCHOOL_ID_INDEX_1 = 1;
        public static final int COLUMN_NAME_CLASS_ID_INDEX_2 = 2;

        public static final int COLUMN_NAME_FROM_USR_ID_INDEX_3 = 3;
        public static final int COLUMN_NAME_FROM_USER_NAME_INDEX_4 = 4;

        public static final int COLUMN_NAME_TO_USR_ID_INDEX_5 = 5;
        public static final int COLUMN_NAME_TO_USER_NAME_INDEX_6 = 6;

        public static final int COLUMN_NAME_CONTENT_INDEX_7 = 7;
        public static final int COLUMN_NAME_MSG_TYPE_ID_INDEX_8 = 8;
        public static final int COLUMN_NAME_CHANNEL_INDEX_9 = 9;

        public static final int COLUMN_NAME_IS_SENT_INDEX_10 = 10;
        public static final int COLUMN_NAME_SENT_DT_INDEX_11 = 11;

        public static final int COLUMN_NAME_IS_READ_INDEX_12 = 12;
        public static final int COLUMN_NAME_READ_DT_INDEX_13 = 13;

        public static final int COLUMN_NAME_IMP_FLG_INDEX_14 = 14;

        public static final int COLUMN_NAME_TITLE_INDEX_15 = 15;
        public static final int COLUMN_NAME_OTHER_INDEX_16 = 16;
        public static final int COLUMN_NAME_CC_LIST_INDEX_17 = 17;
        public static final int COLUMN_NAME_SCHOOL_NAME_INDEX_18 = 18;
        public static final int COLUMN_NAME_MESSAGE_TYPE_INDEX_19 = 19;
        public static final int COLUMN_NAME_CLIENT_ID_INDEX_20 = 20;


        public static final String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_ID + " INTEGER," //0
                + COLUMN_NAME_SCHOOL_ID + " INTEGER,"//1

                + COLUMN_NAME_CLASS_ID + " INTEGER,"//2
                + COLUMN_NAME_FROM_USR_ID + " INTEGER,"//3

                + COLUMN_NAME_FROM_USER_NAME + " TEXT,"//4
                + COLUMN_NAME_TO_USR_ID + " INTEGER,"//5

                + COLUMN_NAME_TO_USER_NAME + " TEXT,"//6
                + COLUMN_NAME_CONTENT + " TEXT,"//7

                + COLUMN_NAME_MSG_TYPE_ID + " INTEGER,"//8
                + COLUMN_NAME_CHANNEL + " INTEGER,"//9

                + COLUMN_NAME_IS_SENT + " INTEGER,"//10
                + COLUMN_NAME_SENT_DT + " TEXT,"//11

                + COLUMN_NAME_IS_READ + " INTEGER,"//12
                + COLUMN_NAME_READ_DT + " TEXT,"//13

                + COLUMN_NAME_IMP_FLG + " INTEGER,"//14
                + COLUMN_NAME_TITLE + " TEXT,"//15

                + COLUMN_NAME_OTHER + " TEXT,"//16
                + COLUMN_NAME_CC_LIST + " TEXT,"//17

                + COLUMN_NAME_SCHOOL_NAME + " TEXT,"//18
                + COLUMN_NAME_MESSAGE_TYPE + " TEXT,"//19

                + COLUMN_NAME_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" //20
                + ")";
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sent_dt='" + sent_dt + '\'' +
                ", title='" + title + '\'' +
                ", cId=" + cId +
                ", from_usr_id=" + from_usr_id +
                ", from_user_name='" + from_user_name + '\'' +
                ", to_usr_id=" + to_usr_id +
                ", to_user_name='" + to_user_name + '\'' +
                ", is_read='" + is_read + '\'' +
                '}';
    }
}
