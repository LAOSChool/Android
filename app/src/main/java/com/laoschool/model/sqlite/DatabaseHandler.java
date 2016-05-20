package com.laoschool.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.laoschool.entities.Image;
import com.laoschool.entities.Message;

/**
 * Created by Hue on 4/11/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "laoSchool.db";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("", "onCreate table:" + Message.MessageColumns.CREATE_MESSAGES_TABLE);
        db.execSQL(Message.MessageColumns.CREATE_MESSAGES_TABLE);
        db.execSQL(Image.ImageColumns.CREATE_IMAGE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Message.MessageColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Image.ImageColumns.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }
}
