package com.jeksonshar.mytaskmaneger.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.details.TaskDetailsFragment;
import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.repository.Repository;
import com.jeksonshar.mytaskmaneger.repository.RepositoryProvider;

public class TaskListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mTaskListAdapter;
    private ImageButton addTask;

    private Repository mRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mRepository = RepositoryProvider.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        addTask = view.findViewById(R.id.imageButton);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mTaskListAdapter = new TaskListAdapter(
                RepositoryProvider.getInstance(getContext()).getAllTasks(), mItemEventsListener
        );

        mRecyclerView.setAdapter(mTaskListAdapter);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageButton) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, TaskDetailsFragment.makeInstance(mRepository.addNewTask()));
                    transaction = transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mRepository.addListener(repositoryListener);
    }

    @Override
    public void onPause() {
        mRepository.removeListener(repositoryListener);
        super.onPause();
    }

    private final Repository.Listener repositoryListener = new Repository.Listener() {
        @Override
        public void onDataChanged() {
            mTaskListAdapter.setNewTasks(mRepository.getAllTasks());
        }
    };

    private final TaskListAdapter.ItemEventsListener mItemEventsListener = new TaskListAdapter.ItemEventsListener() {
        @Override
        public void onItemClick(Task task) {

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, TaskDetailsFragment.makeInstance(task.getId()));
            transaction = transaction.addToBackStack(null);
            transaction.commit();
        }

        @Override
        public void onLongItemClick(Task task) {
            ConfirmationDialogOfDeleteFragment.makeInstance(task)
                    .show(getParentFragmentManager(), null);
        }
    };
}
