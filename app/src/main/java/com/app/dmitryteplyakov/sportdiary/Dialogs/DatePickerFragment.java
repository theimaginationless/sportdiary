package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.app.dmitryteplyakov.sportdiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dmitry21 on 09.08.17.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATEPICKER_UUID = "com.app.datepickerfragment.arg_datepicker_uuid";
    private static final int REQUEST_TIME = 12;
    private static final String DIALOG_TIME = "com.app.datepickerfragment.dialog_time";
    public static final String RETURN_DATE = "com.app.datepickerfragment.return_date";
    private DatePicker mDatePicker;
    private Date mDate;

    public static DatePickerFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATEPICKER_UUID, id);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datepicker, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                        mDate = calendar.getTime();
                        FragmentManager manager = getFragmentManager();
                        TimePickerFragment dialogFragment = new TimePickerFragment();
                        dialogFragment.setTargetFragment(DatePickerFragment.this, REQUEST_TIME);
                        dialogFragment.show(manager, DIALOG_TIME);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = getDialog().getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = mDatePicker.getWidth();
                lp.height = mDatePicker.getHeight();
                window.setAttributes(lp);
            }
        });
        return alertDialog;
        /*return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                        mDate = calendar.getTime();
                        FragmentManager manager = getFragmentManager();
                        TimePickerFragment dialogFragment = new TimePickerFragment();
                        dialogFragment.setTargetFragment(DatePickerFragment.this, REQUEST_TIME);
                        dialogFragment.show(manager, DIALOG_TIME);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TIME) {
            if(resultCode == Activity.RESULT_OK) {
                Date DateTime = (Date) data.getSerializableExtra(TimePickerFragment.RETURN_TIME);
                Calendar calendar = Calendar.getInstance();
                Calendar time = Calendar.getInstance();
                calendar.setTime(mDate);
                time.setTime(DateTime);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int minute = time.get(Calendar.MINUTE);
                int second = time.get(Calendar.SECOND);
                calendar.set(year, month, day, hour, minute, second);
                Intent intent = new Intent();
                intent.putExtra(RETURN_DATE, calendar.getTime());
                getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
            }
        }
    }
}
