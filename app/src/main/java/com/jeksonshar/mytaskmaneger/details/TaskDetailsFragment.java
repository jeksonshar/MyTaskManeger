package com.jeksonshar.mytaskmaneger.details;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.model.TaskPriorityValue;
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
    private ImageView taskPriority;
    private TextView changePriorityView;
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
        changePriorityView = view.findViewById(R.id.change_priority_view);
        taskPriority = view.findViewById(R.id.task_priority);
        save = view.findViewById(R.id.save);

        registerForContextMenu(changePriorityView);
        registerForContextMenu(taskPriority);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleView.setText(mTask.getTitle());
        detailView.setText(mTask.getDetail());

        setPriorityValue();

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

        changePriorityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(v);
            }
        });

        taskPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenu(v);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requireActivity().onBackPressed();  // bad method
                getParentFragmentManager().popBackStack();
            }
        });
    }

    public void setPriorityValue() {
        if (mTask.getPriority().equals(String.valueOf(TaskPriorityValue.GREEN))) {
            taskPriority.setImageResource(R.drawable.ic_brightness_green_24dp);
        } else if (mTask.getPriority().equals(String.valueOf(TaskPriorityValue.RED))) {
            taskPriority.setImageResource(R.drawable.ic_brightness_red_24dp);
        } else if (mTask.getPriority().equals(String.valueOf(TaskPriorityValue.YELLOW))) {
            taskPriority.setImageResource(R.drawable.ic_brightness_yellow_24dp);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_datails_delite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            mRepository.delete(mRepository.getTaskById(taskId));
//            requireActivity().onBackPressed();  // bad method
            getParentFragmentManager().popBackStack();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Moves icons from the PopupMenu MenuItems' icon fields into the menu title as a Spannable with the icon and title text.
     */
    public static void insertMenuItemIcons(Context context, PopupMenu popupMenu) {
        Menu menu = popupMenu.getMenu();
        if (hasIcon(menu)) {
            for (int i = 0; i < menu.size(); i++) {
                insertMenuItemIcon(context, menu.getItem(i), menu);
            }
        }
    }

    /**
     * @return true if the menu has at least one MenuItem with an icon.
     */
    private static boolean hasIcon(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getIcon() != null) return true;
        }
        return false;
    }

    /**
     * Converts the given MenuItem title into a Spannable containing both its icon and title.
     */
    private static void insertMenuItemIcon(Context context, MenuItem menuItem, Menu menu) {
        Drawable icon = menuItem.getIcon();

        // If there no icon, we insert a transparent one to keep the title aligned with the items
        // which do have icons.
        if (icon == null) icon = new ColorDrawable(Color.TRANSPARENT);

        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.menu_priority_menu);
        icon.setBounds(0, 0, iconSize, iconSize);
        ImageSpan imageSpan = new ImageSpan(icon);

        // Add a space placeholder for the icon, before the title.
        SpannableStringBuilder ssb = new SpannableStringBuilder("     " + menuItem.getTitle());

        // Replace the space placeholder with the icon.
        ssb.setSpan(imageSpan, 0, 1, 0);
        menuItem.setTitle(ssb);
        // Set the icon to null just in case, on some weird devices, they've customized Android to display
        // the icon in the menu... we don't want two icons to appear.
        menuItem.setIcon(null);
    }

    public void showPopUpMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.inflate(R.menu.menu_priority_menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.green_priority:
                            mTask.setPriority(String.valueOf(TaskPriorityValue.GREEN));
                            saveTask();
                            setPriorityValue();
                            return true;
                        case R.id.red_priority:
                            mTask.setPriority(String.valueOf(TaskPriorityValue.RED));
                            saveTask();
                            onActivityCreated(getArguments());
                            return true;
                        case R.id.yellow_priority:
                            mTask.setPriority(String.valueOf(TaskPriorityValue.YELLOW));
                            saveTask();
                            onActivityCreated(getArguments());
                            return true;
                    }
                    onCreate(getArguments());
                   return false;
                }
            });
        insertMenuItemIcons(getContext(), popupMenu);
            popupMenu.show();
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
