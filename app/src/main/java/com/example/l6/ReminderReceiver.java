package com.example.l6;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ActivityCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем данные из Intent
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        // Создаем уведомление
        createNotification(context, title, text);
    }

    private void createNotification(Context context, String title, String text) {
        // Проверка разрешения на уведомления (для Android 13+)
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Создаем Intent для открытия приложения при нажатии на уведомление
        Intent notificationIntent = new Intent(context, MainActivity.class); // Укажите активность, которая откроется
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Создаем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notification) // Укажите вашу иконку
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent); // Добавляем PendingIntent

        // Отправляем уведомление через NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }
}
