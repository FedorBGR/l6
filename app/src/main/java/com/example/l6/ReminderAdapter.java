package com.example.l6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReminderAdapter extends BaseAdapter {

    private Context context;
    private List<Reminder> reminderList;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
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
        Reminder reminder = reminderList.get(position);

        // Инициализируем текстовые элементы
        TextView titleTextView = convertView.findViewById(R.id.reminderTitle);
        TextView textTextView = convertView.findViewById(R.id.reminderText);

        // Заполняем данными
        titleTextView.setText(reminder.getTitle());
        textTextView.setText(reminder.getText());

        return convertView;
    }

    public void updateReminderList(List<Reminder> newReminderList) {
        this.reminderList = newReminderList;
        notifyDataSetChanged();
    }
}