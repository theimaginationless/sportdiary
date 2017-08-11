package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.R;

import java.util.UUID;

/**
 * Created by Dmitry on 27.07.2017.
 */

public class TitlePickerFragment extends DialogFragment {
    private static final String ARG_TRAINING_UUID = "com.app.arg_training_uuid";
    public static final String EXTRA_NEW_TRAINING_UUID = "com.app.extra_new_training_uuid";

    private EditText mTitleEditText;
    private Training training;

    public static TitlePickerFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAINING_UUID, id);
        TitlePickerFragment fragment = new TitlePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_titlepicker, null);
        training = TrainingStorage.get(getActivity()).getTraining((UUID) getArguments().getSerializable(ARG_TRAINING_UUID));
        mTitleEditText = (EditText) v.findViewById(R.id.training_title_picker_edit_text);
        mTitleEditText.setText(training.getTitle());
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                training.setTitle(c.toString());
            }
            @Override
            public void afterTextChanged(Editable c) {
                //
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.training_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(training.getTitle() == null) {
                            int num = TrainingStorage.get(getActivity()).getTrainings().indexOf(training) + 1;
                            training.setTitle(getString(R.string.training_no_title) + " " + Integer.toString(num));
                        }
                        TrainingStorage.get(getActivity()).updateTraining(training);
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_TRAINING_UUID, training.getId());
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("TPF", "CANCEL");
        sendResult(Activity.RESULT_CANCELED);
    }
}
