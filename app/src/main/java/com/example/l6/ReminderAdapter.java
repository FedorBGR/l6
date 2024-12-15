package com.example.l6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReminderAdapter extends BaseAdapter {

    private final ReminderDatabaseHelper reminderDatabaseHelper;
    private Context context;
    private List<Reminder> reminderList;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
        this.reminderDatabaseHelper = new ReminderDatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Object getItem(int position) {
        return reminderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Используем LayoutInflater для создания представления
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reminder_item, parent, false);
        }

        // Получаем текущий объект напоминания
        final Reminder reminder = reminderList.get(position);

        // Инициализируем текстовые элементы
        TextView titleTextView = convertView.findViewById(R.id.reminderTitle);
        TextView textTextView = convertView.findViewById(R.id.reminderText);
        Button deleteButton = convertView.findViewById(R.id.deleteReminderButton);

        // Заполняем данными
        titleTextView.setText(reminder.getTitle());
        textTextView.setText(reminder.getText());

        // Обработчик нажатия на кнопку удаления
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReminder(reminder);
            }
        });

        return convertView;
    }

    private void deleteReminder(Reminder reminder) {
        // Удаляем напоминание из базы данных
        reminderDatabaseHelper.deleteReminder(reminder);

        // Удаляем напоминание из списка
        reminderList.remove(reminder);

        // Уведомляем адаптер о том, что данные изменились
        notifyDataSetChanged();
    }
}