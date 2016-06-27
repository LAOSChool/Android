package com.laoschool.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.entities.Image;
import com.laoschool.entities.Message;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 4/20/2016.
 */
public class DataAccessNotification {

    private static final String TAG = "DA_Notification";
    private static DatabaseHandler databaseHandler = LaoSchoolSingleton.getInstance().getDataBaseHelper();

    // Getting messages Count
    public static int getNotificationCount() {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_ID + ") " +
                    "FROM " + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + Message.MessageColumns.COLUMN_NAME_TYPE + " = 1";
            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        count = cursor.getInt(0);
                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // Getting messages Count
    public static int getNotificationCount(int isRead) {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_ID + ") " +
                    "FROM " + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + Message.MessageColumns.COLUMN_NAME_TYPE + " = 1"
                    + ((isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + isRead : "");
            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        count = cursor.getInt(0);
                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Message> getListNotificationForUser(String columName, int userID, int limit, int offset, int isRead) {
        List<Message> messages = new ArrayList<>();
        try {
            String selectbyIDQuery =
                    "SELECT * FROM " + Message.MessageColumns.TABLE_NAME + "\n"
                            + " WHERE "
                            + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 1 + "\n"
                            + ((isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + isRead : "")
                            + " AND "
                            + "(" + Message.MessageColumns.COLUMN_NAME_TO_USR_ID + " = " + userID
                            + " OR " + Message.MessageColumns.COLUMN_NAME_CLASS_ID + " = " + LaoSchoolShared.myProfile.getEclass().getId() + ")"
                            + " ORDER BY " + Message.MessageColumns.COLUMN_NAME_ID + " DESC LIMIT " + offset + "," + limit;

            Log.d(TAG, "getListNotificationForUser(" + userID + "):query =" + selectbyIDQuery);

            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        Message message = LaoSchoolShared.parseMessageFormSql(cursor);
                        message.setNotifyImages(DataAccessImage.getListImageFormNotificationId(message.getTask_id()));
                        messages.add(message);
                    } while (cursor.moveToNext());
            }
            Log.d(TAG, "getListNotificationForUser(" + userID + "):Result size =" + messages.size());
        } catch (Exception e) {
            Log.d(TAG, "getListNotificationForUser(" + userID + "):Exception message =" + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }

    public static void addOrUpdateNotification(Message message) {
        int update = getNotificatioCountbyId(message.getId());
        if (update > 0) {
            //Log.d(TAG, "addOrUpdateMessage():message exists");
        } else {
            Log.d(TAG, "addOrUpdateNotification():add new notification");
            addNotificaion(message);
        }
    }


    // Adding new notification
    public static void addNotificaion(Message message) {
        Log.d("CRUDMEssage", "message:" + message.toJson());
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Message.MessageColumns.COLUMN_NAME_ID, message.getId());

        values.put(Message.MessageColumns.COLUMN_NAME_SCHOOL_ID, message.getSchool_id());
        values.put(Message.MessageColumns.COLUMN_NAME_CLASS_ID, message.getClass_id());

        values.put(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID, message.getFrom_usr_id());
        values.put(Message.MessageColumns.COLUMN_NAME_FROM_USER_NAME, message.getFrom_user_name());

        values.put(Message.MessageColumns.COLUMN_NAME_TO_USR_ID, message.getTo_usr_id());
        values.put(Message.MessageColumns.COLUMN_NAME_TO_USER_NAME, message.getTo_user_name());

        values.put(Message.MessageColumns.COLUMN_NAME_CONTENT, message.getContent());
        values.put(Message.MessageColumns.COLUMN_NAME_MSG_TYPE_ID, message.getMsg_type_id());

        values.put(Message.MessageColumns.COLUMN_NAME_CHANNEL, message.getChannel());
        values.put(Message.MessageColumns.COLUMN_NAME_IS_SENT, message.getIs_sent());

        values.put(Message.MessageColumns.COLUMN_NAME_SENT_DT, message.getSent_dt());
        values.put(Message.MessageColumns.COLUMN_NAME_IS_READ, message.getIs_read());

        values.put(Message.MessageColumns.COLUMN_NAME_READ_DT, message.getRead_dt());
        values.put(Message.MessageColumns.COLUMN_NAME_IMP_FLG, message.getImp_flg());

        values.put(Message.MessageColumns.COLUMN_NAME_TITLE, message.getTitle());
        values.put(Message.MessageColumns.COLUMN_NAME_OTHER, message.getOther());
        values.put(Message.MessageColumns.COLUMN_NAME_CC_LIST, message.getCc_list());

        values.put(Message.MessageColumns.COLUMN_NAME_SCHOOL_NAME, message.getSchoolName());
        values.put(Message.MessageColumns.COLUMN_NAME_MESSAGE_TYPE, message.getMessageType());
        values.put(Message.MessageColumns.COLUMN_NAME_TASK_ID, message.getTask_id());
        values.put(Message.MessageColumns.COLUMN_NAME_TYPE, 1);

        // Inserting Row
        db.insert(Message.MessageColumns.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "addNotificaion:success");

        for (Image image : message.getNotifyImages()) {
            DataAccessImage.addImage(image);
        }
    }

    // Getting messages Count
    private static int getNotificatioCountbyId(int messageID) {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_CLIENT_ID + ") FROM "
                    + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + Message.MessageColumns.COLUMN_NAME_ID + " = " + messageID
                    + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 1;

            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        count = cursor.getInt(0);
                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Message> searchNotificationInbox(int toUserId, int isRead, String query) {
        List<Message> messages = new ArrayList<>();
        try {
            //select * from messages WHERE messages.content LIKE '%a%'
            String like_query = "Select * from messages WHERE "
                    + Message.MessageColumns.COLUMN_NAME_TO_USR_ID + " = " + toUserId +
                    ((isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + isRead : "") +
                    " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 1 +
                    " AND " + "messages.content LIKE '%" + query + "%'";
            Log.d(TAG, "searchNotificationInbox() -query:" + like_query);
            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(like_query, null);

            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        Message message = LaoSchoolShared.parseMessageFormSql(cursor);
                        message.setNotifyImages(DataAccessImage.getListImageFormNotificationId(message.getTask_id()));
                        messages.add(message);
                    } while (cursor.moveToNext());
            }
            Log.d(TAG, "searchNotificationInbox(" + toUserId + "):Result size =" + messages.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
