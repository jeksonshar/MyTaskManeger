package com.jeksonshar.mytaskmaneger.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.jeksonshar.mytaskmaneger.R;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        FragmentManager mFragmentManager =getSupportFragmentManager();

        if (mFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, new TaskListFragment())
                    .commit();
        }
    }
}
