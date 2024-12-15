package com.example.l6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderSQLiteOpenHelper extends SQLiteOpenHelper {

    public ReminderSQLiteOpenHelper(Context context) {
        super(context, "reminders_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE reminders (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, reminder_time INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reminders");
        onCreate(db);
    }
}