package com.laoschool.entities;

import android.provider.BaseColumns;

import com.google.gson.Gson;

/**
 * Created by Hue on 4/20/2016.
 */
public class Image {
    int id;
    int notify_id;
    int user_id;
    String upload_dt;
    String file_name;
    String file_path;
    String file_url;
    String local_file_url;
    String caption;
    int task_id;
    int idx;

    public Image() {
    }

    public Image(int id, int notify_id, int user_id, String upload_dt, String file_name, String file_path, String file_url, String local_file_url, String caption, int task_id, int idx) {
        this.id = id;
        this.notify_id = notify_id;
        this.user_id = user_id;
        this.upload_dt = upload_dt;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_url = file_url;
        this.local_file_url = local_file_url;
        this.caption = caption;
        this.task_id = task_id;
        this.idx = idx;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(int notify_id) {
        this.notify_id = notify_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUpload_dt() {
        return upload_dt;
    }

    public void setUpload_dt(String upload_dt) {
        this.upload_dt = upload_dt;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocal_file_url() {
        return local_file_url;
    }

    public void setLocal_file_url(String local_file_url) {
        this.local_file_url = local_file_url;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String toJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString;
    }

    public static Image fromJson(String jsonString) {
        Gson gson = new Gson();
        Image image = gson.fromJson(jsonString, Image.class);
        return image;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", notify_id=" + notify_id +
                ", student_id=" + user_id +
                ", upload_dt='" + upload_dt + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_path='" + file_path + '\'' +
                ", file_url='" + file_url + '\'' +
                ", local_file_url='" + local_file_url + '\'' +
                ", caption='" + caption + '\'' +
                ", task_id=" + task_id +
                ", idx=" + idx +
                '}';
    }

    public static abstract class ImageColumns implements BaseColumns {
        public static final String TABLE_NAME = "image";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NOTIFY_ID = "notify_id";
        public static final String COLUMN_NAME_USER_ID = "student_id";

        public static final String COLUMN_NAME_UPLOAD_DT = "upload_dt";
        public static final String COLUMN_NAME_FILE_NAME = "file_name";

        public static final String COLUMN_NAME_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_FILE_URL = "file_url";

        public static final String COLUMN_NAME_CAPTION = "caption";

        public static final String COLUMN_NAME_LOCAL_FILE_URL = "local_file_url";


        public static final int COLUMN_NAME_ID_INDEX_0 = 0;
        public static final int COLUMN_NAME_NOTIFY_ID_INDEX_1 = 1;
        public static final int COLUMN_NAME_USER_ID_INDEX_2 = 2;

        public static final int COLUMN_NAME_UPLOAD_DT_INDEX_3 = 3;
        public static final int COLUMN_NAME_FILE_NAME_INDEX_4 = 4;

        public static final int COLUMN_NAME_FILE_PATH_INDEX_5 = 5;
        public static final int COLUMN_NAME_FILE_URL_INDEX_6 = 6;

        public static final int COLUMN_NAME_CAPTION_INDEX_7 = 7;

        public static final int COLUMN_NAME_LOCAL_FILE_URL_INDEX_8 = 8;


        public static final String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY," //0
                + COLUMN_NAME_NOTIFY_ID + " INTEGER,"//1

                + COLUMN_NAME_USER_ID + " INTEGER,"//2
                + COLUMN_NAME_UPLOAD_DT + " TEXT,"//3

                + COLUMN_NAME_FILE_NAME + " TEXT,"//4
                + COLUMN_NAME_FILE_PATH + " TEXT,"//5

                + COLUMN_NAME_FILE_URL + " TEXT,"//6
                + COLUMN_NAME_CAPTION + " TEXT,"//7
                + COLUMN_NAME_LOCAL_FILE_URL + " TEXT"//8
                + ")";
    }
}
