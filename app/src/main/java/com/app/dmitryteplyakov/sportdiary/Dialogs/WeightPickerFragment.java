package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.dmitryteplyakov.sportdiary.Core.Weight.Weight;
import com.app.dmitryteplyakov.sportdiary.Core.Weight.WeightStorage;
import com.app.dmitryteplyakov.sportdiary.R;
import com.app.dmitryteplyakov.sportdiary.Weight.WeightListFragment;

import java.util.UUID;


/**
 * Created by dmitry21 on 22.08.17.
 */

public class WeightPickerFragment extends DialogFragment {
    public static final String EXTRA_NEW_WEIGHT = "com.app.extra_new_weight";
    public static final String ARG_WEIGHT_UUID = "com.app.arg_weight_uuid";
    public static final String EXTRA_OLD_WEIGHT = "com.app.extra_old_weight";

    private EditText mWeightEditText;
    private float mWeight;

    public static WeightPickerFragment getInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEIGHT_UUID, id);
        WeightPickerFragment fragment = new WeightPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_weightpicker, null);
        mWeightEditText = (EditText) v.findViewById(R.id.weight_picker_edit_text);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        if(getTargetRequestCode() == WeightListFragment.REQUEST_WEIGHT_EDIT) {
            Weight weight = WeightStorage.get(getActivity()).getWeight((UUID) getArguments().getSerializable(ARG_WEIGHT_UUID));
            mWeightEditText.setText(Float.toString(weight.getValue()));
        }
        mWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //
            }
            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                if(count != 0)
                    mWeight = Float.parseFloat(c.toString());
                else
                    mWeight = 0;
            }
            @Override
            public void afterTextChanged(Editable c) {
                //
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.action_weight_tab_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mWeight != 0)
                            sendResult(Activity.RESULT_OK);
                        else
                            sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        Log.d("WPF", Boolean.toString(getTargetFragment() == null) + " " + Boolean.toString(resultCode == Activity.RESULT_CANCELED));
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        if(getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_WEIGHT, mWeight);
        if(getTargetRequestCode() == WeightListFragment.REQUEST_WEIGHT_EDIT)
            intent.putExtra(EXTRA_OLD_WEIGHT, getArguments().getSerializable(ARG_WEIGHT_UUID));
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("WPF", "CANCEL");
        sendResult(Activity.RESULT_CANCELED);
    }
}
