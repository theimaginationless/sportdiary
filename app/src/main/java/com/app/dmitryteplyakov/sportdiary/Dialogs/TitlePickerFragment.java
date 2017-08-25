package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.dmitryteplyakov.sportdiary.Core.Timer.TimerStorage;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplate;
import com.app.dmitryteplyakov.sportdiary.Core.TimerTemplate.TimerTemplateStorage;
import com.app.dmitryteplyakov.sportdiary.Core.Training.Training;
import com.app.dmitryteplyakov.sportdiary.Core.Training.TrainingStorage;
import com.app.dmitryteplyakov.sportdiary.R;
import com.app.dmitryteplyakov.sportdiary.Timer.TimerTemplatesListFragment;

import java.util.UUID;

/**
 * Created by Dmitry on 27.07.2017.
 */

public class TitlePickerFragment extends DialogFragment {
    private static final String ARG_TRAINING_UUID = "com.app.arg_training_uuid";
    public static final String EXTRA_NEW_UUID = "com.app.extra_new_training_uuid";
    public static final String EXTRA_TITLE = "com.app.extra_title";

    private EditText mTitleEditText;
    private Training training;
    private String title;
    private TextInputLayout TIHint;

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
        final UUID universalId = (UUID) getArguments().getSerializable(ARG_TRAINING_UUID);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        TIHint = (TextInputLayout) v.findViewById(R.id.TIL);
        mTitleEditText = (EditText) v.findViewById(R.id.training_title_picker_edit_text);
        if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_NEW_TEMPLATE) {
            TIHint.setHint(getString(R.string.timer_template_title));
            if(TimerTemplateStorage.get(getActivity()).getTemplate(universalId) != null) {
                TimerTemplate template = TimerTemplateStorage.get(getActivity()).getTemplate(universalId);
                title = template.getTitle();
                mTitleEditText.setText(title);
            }
        } else if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_EDIT_TEMPLATE) {
            TIHint.setHint(getString(R.string.timer_template_title));
            title = TimerTemplateStorage.get(getActivity()).getTemplate(universalId).getTitle();
            mTitleEditText.setText(title);
        } else {
            TIHint.setHint(getString(R.string.training_title_picker_hint));
            training = TrainingStorage.get(getActivity()).getTraining(universalId);
            mTitleEditText.setText(training.getTitle());
        }
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_NEW_TEMPLATE || getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_EDIT_TEMPLATE) {
                    //TimerTemplate template = TimerTemplateStorage.get(getActivity()).getTemplate(universalId);
                    //template.setTitle(c.toString());
                    //TimerTemplateStorage.get(getActivity()).updateTemplate(template);
                    title = c.toString();
                }
                else
                    training.setTitle(c.toString());
            }
            @Override
            public void afterTextChanged(Editable c) {
                //
            }
        });
        String titleDialog;
        if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_NEW_TEMPLATE || getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_EDIT_TEMPLATE)
            titleDialog = getString(R.string.timer_template_title);
        else
            titleDialog = getString(R.string.training_title);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(titleDialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_NEW_TEMPLATE || getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_EDIT_TEMPLATE) {
                            if(title == null) {
                                int num = TimerTemplateStorage.get(getActivity()).getTemplates().size() + 1;
                                title = (getString(R.string.timer_template_no_title) + " " + Integer.toString(num));
                            }
                        } else {
                            if (training.getTitle() == null) {
                                int num = TrainingStorage.get(getActivity()).getTrainings().size();
                                training.setTitle(getString(R.string.training_no_title) + " " + Integer.toString(num));
                            } else if (training.getTitle().equals("")) {
                                int num = TrainingStorage.get(getActivity()).getTrainings().size() - TrainingStorage.get(getActivity()).getTrainings().indexOf(training);
                                training.setTitle(getString(R.string.training_no_title) + " " + Integer.toString(num));
                            }
                            TrainingStorage.get(getActivity()).updateTraining(training);
                        }

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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        UUID id;
        Intent intent = new Intent();
        if(getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_NEW_TEMPLATE || getTargetRequestCode() == TimerTemplatesListFragment.REQUEST_EDIT_TEMPLATE) {
            id = (UUID) getArguments().getSerializable(ARG_TRAINING_UUID);
            intent.putExtra(EXTRA_TITLE, title);
        }
        else
            id = training.getId();
        intent.putExtra(EXTRA_NEW_UUID, id);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("TPF", "CANCEL");
        sendResult(Activity.RESULT_CANCELED);
    }
}
