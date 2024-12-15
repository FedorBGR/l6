package com.example.l6;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import android.content.Intent;
import android.content.Context;


public class AddReminderActivity extends AppCompatActivity {

    private EditText titleEditText, textEditText;
    private TextView dateTextView, timeTextView;
    private Button selectDateButton, selectTimeButton, saveReminderButton;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    private ReminderDatabaseHelper reminderDatabaseHelper; // Database helper

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification) // Иконка для уведомления
            .setContentTitle("Напоминание")
            .setContentText("У вас есть новое напоминание!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                1 // Код запроса
        );

        // Initialize the database helper
        reminderDatabaseHelper = new ReminderDatabaseHelper(this);

        // Initialize components
        titleEditText = findViewById(R.id.titleEditText);
        textEditText = findViewById(R.id.textEditText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        saveReminderButton = findViewById(R.id.saveReminderButton);

        // Initialize current date and time
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // Update TextView with default date and time
        dateTextView.setText(String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
        timeTextView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

        // Set up "Select Date" button
        selectDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddReminderActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;
                        dateTextView.setText(String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
                    },
                    selectedYear, selectedMonth, selectedDay);
            datePickerDialog.show();
        });

        // Set up "Select Time" button
        selectTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddReminderActivity.this,
                    (view, hourOfDay, minute) -> {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        timeTextView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    },
                    selectedHour, selectedMinute, true);
            timePickerDialog.show();
        });

        // Set up "Save Reminder" button
        saveReminderButton.setOnClickListener(v -> {
                    String title = titleEditText.getText().toString().trim();
                    String text = textEditText.getText().toString().trim();

                    // Validate input
                    if (title.isEmpty() || text.isEmpty()) {
                        Toast.makeText(AddReminderActivity.this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long reminderTime = getReminderTimeInMillis(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);

                    // Create a new Reminder object
                    Reminder reminder = new Reminder(title, text, reminderTime);

                    Intent intent = new Intent(this, ReminderReceiver.class);
                    intent.putExtra("title", title); // Передаем название
                    intent.putExtra("text", text);  // Передаем текст


            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0, // ID PendingIntent (может быть уникальным)
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

// Укажите время для срабатывания напоминания
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Toast.makeText(this, "Необходимо разрешение для точных будильников", Toast.LENGTH_LONG).show();
                    return;
                }
            }
                    if (alarmManager != null) {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                reminderTime, // Время напоминания в миллисекундах
                                pendingIntent
                        );
                    }

            // Add the reminder to the database
            boolean isAdded = reminderDatabaseHelper.addReminder(reminder);

            if (isAdded) {
                Toast.makeText(AddReminderActivity.this, "Напоминание успешно добавлено!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(AddReminderActivity.this, "Ошибка при добавлении напоминания.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Method to get reminder time in milliseconds
    private long getReminderTimeInMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);
        return calendar.getTimeInMillis();
    }
}