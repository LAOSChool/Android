package com.laoschool.model.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.laoschool.LaoSchoolSingleton;
import com.laoschool.entities.Message;
import com.laoschool.shared.LaoSchoolShared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 4/11/2016.
 */
public class DataAccessMessage {
    private static final String TAG = "DataAccessMessage";
    private static DatabaseHandler databaseHandler = LaoSchoolSingleton.getInstance().getDataBaseHelper();

    // Adding new message
    public static void addMessage(Message message) {
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
        values.put(Message.MessageColumns.COLUMN_NAME_FRM_USER_PHOTO, message.getFrm_user_photo());
        values.put(Message.MessageColumns.COLUMN_NAME_TYPE, 0);

        // Inserting Row
        db.insert(Message.MessageColumns.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "addMessage:success");
    }


    // Getting messages Count
    public static int getMessagesCount() {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_CLIENT_ID + ") " +
                    "FROM " + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + Message.MessageColumns.COLUMN_NAME_TYPE + " = 0";
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

    // Updating single message
    public static int updateMessage(Message message) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Message.MessageColumns.COLUMN_NAME_ID, message.getId());
        values.put(Message.MessageColumns.COLUMN_NAME_TITLE, message.getTitle());

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

        values.put(Message.MessageColumns.COLUMN_NAME_OTHER, message.getOther());
        values.put(Message.MessageColumns.COLUMN_NAME_CC_LIST, message.getCc_list());

        values.put(Message.MessageColumns.COLUMN_NAME_SCHOOL_NAME, message.getSchoolName());
        values.put(Message.MessageColumns.COLUMN_NAME_MESSAGE_TYPE, message.getMessageType());

        // updating row
        int update = db.update(Message.MessageColumns.TABLE_NAME, values, Message.MessageColumns.COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(message.getId())});
        if (update > 0) {
            Log.d(TAG, "updateMessage():success");
        } else {
            Log.d(TAG, "updateMessage():failure");
        }
        return update;
    }

    // Deleting single contact
    public static void deleteMessage(Message message) {
        SQLiteDatabase db = databaseHandler.getWritableDatabase();
        db.delete(Message.MessageColumns.TABLE_NAME, Message.MessageColumns.COLUMN_NAME_CLIENT_ID + " = ?",
                new String[]{String.valueOf(message.getClienId())});
        db.close();
    }


    public static void addOrUpdateMessage(Message message) {
        int update = getMessagesCountbyId(message.getId());
        if (update > 0) {
            //Log.d(TAG, "addOrUpdateMessage():message exists");
        } else {
            Log.d(TAG, "addOrUpdateMessage():add new message");
            addMessage(message);
        }
    }


    public static List<Message> getListMessagesForUser(String columName, int userID, int limit, int offset, int isRead) {
        List<Message> messages = new ArrayList<>();
        try {
            //String sisRead = (isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + isRead : "";
            String selectbyIDQuery =
                    "SELECT * FROM " + Message.MessageColumns.TABLE_NAME + "\n"
                            + " WHERE " + columName + " = " + userID + "\n"
                            + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 0 + "\n"
                            + ((isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + isRead : "")
                            + " ORDER BY " + Message.MessageColumns.COLUMN_NAME_ID + " DESC LIMIT " + limit + " OFFSET " + offset;

            Log.d(TAG, "getListMessagesForUser(" + userID + "):query =" + selectbyIDQuery);

            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        Message message = LaoSchoolShared.parseMessageFormSql(cursor);
                        messages.add(message);
                    } while (cursor.moveToNext());
            }
            Log.d(TAG, "getListMessagesForUser(" + userID + "):Result size =" + messages.size());
        } catch (Exception e) {
            Log.d(TAG, "getListMessagesForUser(" + userID + "):Exception message =" + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }


    // Getting messages Count
    private static int getMessagesCountbyId(int messageID) {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_CLIENT_ID + ") FROM "
                    + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + Message.MessageColumns.COLUMN_NAME_ID + " = " + messageID
                    + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 0;
            ;

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

    public static int getMessagesCountFormUser(String colum, int userID) {
        int count = 0;
        String selectbyIDQuery = "SELECT COUNT(" + Message.MessageColumns.COLUMN_NAME_ID + ") FROM " + Message.MessageColumns.TABLE_NAME
                + " WHERE " + colum + " = " + userID + ""
                + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 0;
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        //query for cursor
        Cursor cursor = db.rawQuery(selectbyIDQuery, null);
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0)
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
        }

        Log.d(TAG, "getMessagesCountFormUser() query:" + selectbyIDQuery + "-Results:" + count);
        return count;
    }

    public static int getMessagesCountFormUser(String colum, int userID, int isRead) {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(" + colum + ") FROM " + Message.MessageColumns.TABLE_NAME
                    + " WHERE " + colum + " = " + userID + "\n"
                    + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 0
                    + ((isRead == 0) ? " AND " + Message.MessageColumns.COLUMN_NAME_IS_READ + " = " + 0 : "");
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

    public static int getMaxMessagesID(int userID) {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT MAX(" + Message.MessageColumns.COLUMN_NAME_ID + ") FROM " + Message.MessageColumns.TABLE_NAME
                    + " WHERE ( " + Message.MessageColumns.COLUMN_NAME_TO_USR_ID + " = " + userID +
                    " OR " + Message.MessageColumns.COLUMN_NAME_FROM_USR_ID + " = " + userID + " )\n"
                    + " AND " + Message.MessageColumns.COLUMN_NAME_TYPE + " = " + 0;
            SQLiteDatabase db = databaseHandler.getReadableDatabase();
            Log.d(TAG, selectbyIDQuery);
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

    public static void deleteTable() {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();
        db.delete(Message.MessageColumns.TABLE_NAME, null, null);
        db.close();
    }


}
