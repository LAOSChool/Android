package com.laoschool.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.laoschool.entities.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hue on 4/11/2016.
 */
public class CRUDMessage {
    private DatabaseHandler databaseHandler;

    public CRUDMessage(Context context) {
        databaseHandler = new DatabaseHandler(context);
    }


    // Adding new message
    public void addMessage(Message message) {
        SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
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

        // Inserting Row
        db.insert(Message.MessageColumns.TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Log.d("CRUDMEssage", "Ok");
    }

    // Getting single contact
    public Message getMessage(int id) {
        Message message = new Message();
        try {
            String selectbyIDQuery = "SELECT * FROM " + Message.MessageColumns.TABLE_NAME + " WHERE " + Message.MessageColumns.COLUMN_NAME_CLIENT_ID + " = " + id;
            SQLiteDatabase db = this.databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        message = parseMessageFormSql(cursor);
                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    // Getting All Smessages
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            String selectbyIDQuery = "SELECT * FROM " + Message.MessageColumns.TABLE_NAME + " ";
            SQLiteDatabase db = this.databaseHandler.getReadableDatabase();

            //query for cursor
            Cursor cursor = db.rawQuery(selectbyIDQuery, null);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0)
                    do {
                        Message message = parseMessageFormSql(cursor);
                        messages.add(message);
                    } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Getting messages Count
    public int getMessagesCount() {
        int count = 0;
        try {
            String selectbyIDQuery = "SELECT COUNT(id) FROM " + Message.MessageColumns.TABLE_NAME;
            SQLiteDatabase db = this.databaseHandler.getReadableDatabase();

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
    public int updateMessage(Message message) {
        SQLiteDatabase db = this.databaseHandler.getWritableDatabase();

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
        return db.update(Message.MessageColumns.TABLE_NAME, values, Message.MessageColumns.COLUMN_NAME_CLIENT_ID + " = ?",
                new String[]{String.valueOf(message.getClienId())});
    }

    // Deleting single contact
    public void deleteMessage(Message message) {
        SQLiteDatabase db = this.databaseHandler.getWritableDatabase();
        db.delete(Message.MessageColumns.TABLE_NAME, Message.MessageColumns.COLUMN_NAME_CLIENT_ID + " = ?",
                new String[]{String.valueOf(message.getClienId())});
        db.close();
    }

    private Message parseMessageFormSql(Cursor cursor) {
        Message message = new Message();

        message.setId(cursor.getInt(Message.MessageColumns.COLUMN_NAME_ID_INDEX_0));
        message.setSchool_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_SCHOOL_ID_INDEX_1));
        message.setClass_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CLASS_ID_INDEX_2));
        message.setFrom_usr_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_FROM_USR_ID_INDEX_3));
        message.setFrom_user_name(cursor.getString(Message.MessageColumns.COLUMN_NAME_FROM_USER_NAME_INDEX_4));
        message.setTo_usr_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_TO_USR_ID_INDEX_5));
        message.setTo_user_name(cursor.getString(Message.MessageColumns.COLUMN_NAME_TO_USER_NAME_INDEX_6));
        message.setContent(cursor.getString(Message.MessageColumns.COLUMN_NAME_CONTENT_INDEX_7));
        message.setMsg_type_id(cursor.getInt(Message.MessageColumns.COLUMN_NAME_MSG_TYPE_ID_INDEX_8));
        message.setChannel(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CHANNEL_INDEX_9));
        message.setIs_sent(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IS_SENT_INDEX_10));
        message.setIs_read(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IS_READ_INDEX_11));
        message.setRead_dt(cursor.getString(Message.MessageColumns.COLUMN_NAME_READ_DT_INDEX_13));
        message.setSent_dt(cursor.getString(Message.MessageColumns.COLUMN_NAME_SENT_DT_INDEX_12));
        message.setImp_flg(cursor.getInt(Message.MessageColumns.COLUMN_NAME_IMP_FLG_INDEX_14));
        message.setOther(cursor.getString(Message.MessageColumns.COLUMN_NAME_OTHER_INDEX_15));
        message.setTitle(cursor.getString(Message.MessageColumns.COLUMN_NAME_TITLE_INDEX_16));
        message.setCc_list(cursor.getString(Message.MessageColumns.COLUMN_NAME_CC_LIST_INDEX_17));
        message.setSchoolName(cursor.getString(Message.MessageColumns.COLUMN_NAME_SCHOOL_NAME_INDEX_18));
        message.setMessageType(cursor.getString(Message.MessageColumns.COLUMN_NAME_MESSAGE_TYPE_INDEX_19));
        message.setClienId(cursor.getInt(Message.MessageColumns.COLUMN_NAME_CLIENT_ID_INDEX_20));

        return message;
    }

}
