package com.laoschool.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.entities.Image;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 4/20/2016.
 */
public class DataAccessImage {

    private static final String TAG = "DA_Image";
    private static DatabaseHandler databaseHandler = LaoSchoolSingleton.getInstance().getDataBaseHelper();


    // Adding new notification
    public static void addImage(Image image) {
        Log.d(TAG, "image:" + image.toString());
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Image.ImageColumns.COLUMN_NAME_ID, image.getId());
        values.put(Image.ImageColumns.COLUMN_NAME_CAPTION, image.getCaption());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_NAME, image.getFile_name());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_PATH, image.getFile_path());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_URL, image.getFile_url());
        values.put(Image.ImageColumns.COLUMN_NAME_NOTIFY_ID, image.getNotify_id());
        values.put(Image.ImageColumns.COLUMN_NAME_USER_ID, image.getUser_id());
        values.put(Image.ImageColumns.COLUMN_NAME_UPLOAD_DT, image.getUpload_dt());
        values.put(Image.ImageColumns.COLUMN_NAME_LOCAL_FILE_URL, image.getLocal_file_url());

        // Inserting Row
        db.insert(Image.ImageColumns.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "addImage:success");
    }

    // Adding new notification
    public static int updateImage(Image image) {
        Log.d(TAG, "image:" + image.toString());
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Image.ImageColumns.COLUMN_NAME_CAPTION, image.getCaption());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_NAME, image.getFile_name());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_PATH, image.getFile_path());
        values.put(Image.ImageColumns.COLUMN_NAME_FILE_URL, image.getFile_url());
        values.put(Image.ImageColumns.COLUMN_NAME_NOTIFY_ID, image.getNotify_id());
        values.put(Image.ImageColumns.COLUMN_NAME_USER_ID, image.getUser_id());
        values.put(Image.ImageColumns.COLUMN_NAME_UPLOAD_DT, image.getUpload_dt());
        values.put(Image.ImageColumns.COLUMN_NAME_LOCAL_FILE_URL, image.getLocal_file_url());

        // Inserting Row
        int update = db.update(Image.ImageColumns.TABLE_NAME, values, Image.ImageColumns.COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(image.getId())});
        if (update > 0) {
            Log.d(TAG, "updateImage():success");
        } else {
            Log.d(TAG, "updateImage():failure");
        }
        db.close(); // Closing database connection
        return update;
    }

    public static List<Image> getListImageFormNotificationId(int notificationId) {
        List<Image> images = new ArrayList<>();
        try {
            String selectbyIDQuery = "SELECT * FROM " + Image.ImageColumns.TABLE_NAME
                    + " WHERE " + Image.ImageColumns.COLUMN_NAME_NOTIFY_ID + " = " + notificationId;
            //Log.d(TAG, "getListImageFormNotificationId(" + notificationId + "):query =" + selectbyIDQuery);

            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        Image image = LaoSchoolShared.parseImageFormSql(cursor);
                        //Log.d(TAG, image.toString());
                        images.add(image);
                    } while (cursor.moveToNext());
            }
            //Log.d(TAG, "getListImageFormNotificationId(" + notificationId + "):Result size =" + images.size());
        } catch (Exception e) {
            Log.d(TAG, "getListImageFormNotificationId(" + notificationId + "):Exception message =" + e.getMessage());
            e.printStackTrace();
        }
        return images;
    }

    public static void deleteTable() {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        db.delete(Image.ImageColumns.TABLE_NAME, null, null);
        db.close();
    }

}
