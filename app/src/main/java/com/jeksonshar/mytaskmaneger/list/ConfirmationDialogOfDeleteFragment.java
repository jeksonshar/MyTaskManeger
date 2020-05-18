package com.jeksonshar.mytaskmaneger.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jeksonshar.mytaskmaneger.R;
import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.repository.Repository;
import com.jeksonshar.mytaskmaneger.repository.RepositoryProvider;

import java.util.UUID;

public class ConfirmationDialogOfDeleteFragment extends DialogFragment {

    private static final String KEY_ID = "ID";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.delete))
                .setMessage(getResources().getString(R.string.are_you_sure_to_delete))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentTask();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }

    private void deleteCurrentTask() {
        Repository repository = RepositoryProvider.getInstance(getContext());
        UUID taskId = (UUID) getArguments().getSerializable(KEY_ID);
        Task taskToDelete = repository.getTaskById(taskId);
        repository.delete(taskToDelete);
    }

    public static DialogFragment makeInstance(Task task) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ID, task.getId());

        ConfirmationDialogOfDeleteFragment fragment = new ConfirmationDialogOfDeleteFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
