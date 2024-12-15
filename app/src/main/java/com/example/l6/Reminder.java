package com.example.l6;

public class Reminder {

    private int id;  // Если вам нужно хранить ID напоминания
    private String title;
    private String text;
    private long reminderTime;

    // Конструктор с 3 параметрами (без ID)
    public Reminder(String title, String text, long reminderTime) {
        this.title = title;
        this.text = text;
        this.reminderTime = reminderTime;
    }

    // Геттеры и сеттеры для полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }
}
