package com.example.l6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    // Имя базы данных и версия
    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 3;

    // Константа для имени таблицы
    private static final String TABLE_REMINDERS = "reminders";

    // SQL-запрос для создания таблицы
    private static final String CREATE_TABLE_SQL = "CREATE TABLE reminders (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "text TEXT, " +
            "reminder_time INTEGER);";

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Используем константу TABLE_REMINDERS для создания таблицы
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Очистка базы данных при обновлении версии
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("reminders", "id = ?", new String[]{String.valueOf(reminder.getId())});
        db.close();
    }

    // Метод для добавления напоминания
    public boolean addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", reminder.getTitle());
        values.put("text", reminder.getText());
        values.put("reminder_time", reminder.getReminderTime());

        long result = db.insert("reminders", null, values);
        return result != -1; // Вернет true, если добавление успешно
    }

    // Пример метода для получения всех напоминаний
    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Используем константу TABLE_REMINDERS в запросах
        Cursor cursor = db.query(TABLE_REMINDERS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Получаем индексы столбцов
                int idIndex = cursor.getColumnIndex("id");
                int titleIndex = cursor.getColumnIndex("title");
                int textIndex = cursor.getColumnIndex("text");
                int timeIndex = cursor.getColumnIndex("reminder_time");

                // Проверяем, чтобы индексы были валидными
                if (idIndex != -1 && titleIndex != -1 && textIndex != -1 && timeIndex != -1) {
                    int id = cursor.getInt(idIndex); // Получаем id
                    String title = cursor.getString(titleIndex); // Получаем title
                    String text = cursor.getString(textIndex); // Получаем text
                    long reminderTime = cursor.getLong(timeIndex); // Получаем reminder_time

                    // Создаем объект Reminder с 4 параметрами (id, title, text, reminderTime)
                    Reminder reminder = new Reminder(title, text, reminderTime);
                    reminder.setId(id);  // Устанавливаем id, полученный из базы данных

                    reminders.add(reminder);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return reminders;
    }
}