package com.jeksonshar.mytaskmaneger.details;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.repository.Repository;
import com.jeksonshar.mytaskmaneger.repository.RepositoryProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class TaskDetailsFragment extends Fragment {

    private static final String KEY_ID = "ID";

    // Model
    private UUID taskId;
    private Task mTask;

    private Repository mRepository;

    // View
    private EditText titleView;
    private EditText detailView;
    private TextView dateOfTaskView;
    private TextView timeOfTaskView;
    private Button changeDateButton;
    private Button changeTimeButton;
    private CheckBox solvedView;
    private Button save;

    public TaskDetailsFragment() {
        super(R.layout.fragment_task_details);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        Bundle arguments = getArguments();

        taskId = (UUID) arguments.getSerializable(KEY_ID);
        mRepository = RepositoryProvider.getInstance(getContext());
        mTask = mRepository.getTaskById(taskId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleView = view.findViewById(R.id.task_title_view);
        detailView = view.findViewById(R.id.task_details_view);
        dateOfTaskView = view.findViewById(R.id.date_of_task_view);
        timeOfTaskView = view.findViewById(R.id.time_of_task_view);
        changeDateButton = view.findViewById(R.id.change_date_button);
        changeTimeButton = view.findViewById(R.id.change_time_button);
        solvedView = view.findViewById(R.id.task_solved);
        save = view.findViewById(R.id.save);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleView.setText(mTask.getTitle());
        detailView.setText(mTask.getDetail());
        setInitialDateTime();

        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setTitle(s.toString());
                saveTask();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        detailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTask.setDetail(s.toString());
                saveTask();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dateOfTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate();
            }
        });

        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate();
            }
        });

        timeOfTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTime();
            }
        });

        changeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTime();
            }
        });

        solvedView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTask.setSolved(isChecked);
                saveTask();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_datails_delite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            mRepository.delete(mRepository.getTaskById(taskId));
            requireActivity().onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void changeDate() {
        new DatePickerDialog(getContext(), date,
                mTask.getDateAndTime().get(Calendar.YEAR),
                mTask.getDateAndTime().get(Calendar.MONTH),
                mTask.getDateAndTime().get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void changeTime() {
        new TimePickerDialog(getContext(), time,
                mTask.getDateAndTime().get(Calendar.HOUR_OF_DAY),
                mTask.getDateAndTime().get(Calendar.MINUTE), true)
                .show();
    }

    private void setInitialDateTime() {
/*   first variant of string formatting

        dateOfTaskView.setText(DateUtils.formatDateTime(getContext(),
                mTask.getDateAndTime().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
        timeOfTaskView.setText(DateUtils.formatDateTime(getContext(),
                mTask.getDateAndTime().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
*/

//   second variant of string formatting
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EE, d MMMM yyyy", Locale.getDefault());
        dateOfTaskView.setText(dateFormat.format(mTask.getDateAndTime().getTime()));
        SimpleDateFormat timeFormat = new SimpleDateFormat(
                "HH:mm", Locale.getDefault());
        timeOfTaskView.setText(timeFormat.format(mTask.getDateAndTime().getTime()));
        solvedView.setChecked(mTask.getSolved());
        saveTask();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mTask.getDateAndTime().set(Calendar.YEAR, year);
            mTask.getDateAndTime().set(Calendar.MONTH, month);
            mTask.getDateAndTime().set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTask.getDateAndTime().set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTask.getDateAndTime().set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    private void saveTask() {
        mRepository.update(mTask);
    }

    public static TaskDetailsFragment makeInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ID, id);

        TaskDetailsFragment fragment = new TaskDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
