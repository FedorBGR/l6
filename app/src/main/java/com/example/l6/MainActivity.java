package com.example.l6;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ReminderDatabaseHelper reminderDatabaseHelper; // База данных для напоминаний
    private ListView reminderListView; // Для отображения напоминаний
    private ReminderAdapter adapter; // Адаптер для ListView
    private Button addReminderButton; // Кнопка для открытия второй активности

    // Метод для создания канала уведомлений
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "reminder_channel";
            String channelName = "Reminder Notifications";
            String channelDescription = "Notifications for reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаем канал уведомлений
        createNotificationChannel();

        // Проверяем разрешение на уведомления (для Android 13 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Инициализация компонентов
        reminderDatabaseHelper = new ReminderDatabaseHelper(this);
        reminderListView = findViewById(R.id.reminderListView);
        addReminderButton = findViewById(R.id.addReminderButton);

        // Получение списка напоминаний
        List<Reminder> reminderList = reminderDatabaseHelper.getAllReminders();

        // Создаем адаптер и передаем ему список напоминаний
        adapter = new ReminderAdapter(this, reminderList);

        // Устанавливаем адаптер для ListView
        reminderListView.setAdapter(adapter);

        // Устанавливаем обработчик для кнопки
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открытие второй активности
                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
                startActivity(intent);
            }
        });


    }

}