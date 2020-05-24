package com.jeksonshar.mytaskmaneger.list;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.model.TaskPriorityValue;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final TextView dateAndTime;
    private final CheckBox solvedView;
    private final ImageView priorityView;

    private Task currentTask;
    private final TaskListAdapter.ItemEventsListener listener;

    public TaskViewHolder(@NonNull View itemView, final TaskListAdapter.ItemEventsListener listener) {
        super(itemView);

        this.listener = listener;

        titleView = itemView.findViewById(R.id.item_title);
        dateAndTime = itemView.findViewById(R.id.item_date_and_time);
        solvedView = itemView.findViewById(R.id.item_solved);

        priorityView = itemView.findViewById(R.id.item_task_priority);

        solvedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask.setSolved(solvedView.isChecked());  // изменяем значение чекбокса
                listener.onSolvedClick(currentTask);            // сохраняем его в задаче
            }
        });

        itemView.setOnClickListener(itemClickListener);
        itemView.setOnLongClickListener(itemLongClickListener);
    }


    private final View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                listener.onItemClick(currentTask);
        }
    };

    private final View.OnLongClickListener itemLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            listener.onLongItemClick(currentTask);
            return false;
        }
    };


    public void bindTo(final Task task) {
        this.currentTask = task;

        titleView.setText(task.getTitle());
        solvedView.setChecked(task.getSolved());

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEEE, d MMMM yyyy; HH:mm", Locale.getDefault());
        dateAndTime.setText(dateFormat.format(task.getDateAndTime().getTime()));

        if (currentTask.getPriority().equals(String.valueOf(TaskPriorityValue.GREEN))) {
            priorityView.setImageResource(R.drawable.ic_brightness_green_16dp);
        } else if (currentTask.getPriority().equals(String.valueOf(TaskPriorityValue.RED))) {
            priorityView.setImageResource(R.drawable.ic_brightness_red_16dp);
        } else if (currentTask.getPriority().equals(String.valueOf(TaskPriorityValue.YELLOW))) {
            priorityView.setImageResource(R.drawable.ic_brightness_yellow_slow_16dp);
        }
    }
}
