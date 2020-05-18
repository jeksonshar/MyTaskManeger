package com.jeksonshar.mytaskmaneger.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.model.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> mTaskList;
    private final ItemEventsListener mListener;

    public TaskListAdapter(List<Task> taskList, ItemEventsListener listener) {
        mTaskList = taskList;
        mListener = listener;

        setHasStableIds(true);
    }

    void setNewTasks(List<Task> newTasks) {
        this.mTaskList = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindTo(mTaskList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    interface ItemEventsListener {
        void onItemClick(Task task);
        void onLongItemClick(Task task);
    }
}
