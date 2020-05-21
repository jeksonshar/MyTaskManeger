package com.jeksonshar.mytaskmaneger.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

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

    private static boolean hiding;

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

        if (getHiding()) {
            mTaskListAdapter = new TaskListAdapter(RepositoryProvider.getInstance(getContext()).getUnsolvedTasks(),mItemEventsListener);
        } else {
            mTaskListAdapter = new TaskListAdapter(
                    RepositoryProvider.getInstance(getContext()).getAllTasks(), mItemEventsListener
            );
        }

        mRecyclerView.setAdapter(mTaskListAdapter);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageButton) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer,
                                    TaskDetailsFragment.makeInstance(mRepository.addNewTask()));
                    transaction = transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.hide_solved) {
            setHiding(true);
            onActivityCreated(getArguments());
        } else if (item.getItemId() == R.id.un_hide) {
            setHiding(false);
            onActivityCreated(getArguments());
        } else if (item.getItemId() == R.id.sorted_by_deadline) {
            Toast.makeText(getContext(), "Sorted by deadline...", Toast.LENGTH_LONG).show();
            //TODO
        } else if (item.getItemId() == R.id.sorted_by_creation_date) {
            Toast.makeText(getContext(), "Sorted by created date...", Toast.LENGTH_LONG).show();
            //TODO
        }
        return super.onOptionsItemSelected(item);
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

    public static boolean getHiding() {
        return hiding;
    }

    public void setHiding(boolean hiding) {
        TaskListFragment.hiding = hiding;
    }

    private final Repository.Listener repositoryListener = new Repository.Listener() {
        @Override
        public void onDataChanged() {
            mTaskListAdapter.setNewTasks(mRepository.getAllTasks());
        }
    };

    private final TaskListAdapter.ItemEventsListener mItemEventsListener = new TaskListAdapter.ItemEventsListener() {
        @Override
        public void onSolvedClick(Task task) { // обновление задачи при нажатии чекбокса из списка
            mRepository.update(task);
        }

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
