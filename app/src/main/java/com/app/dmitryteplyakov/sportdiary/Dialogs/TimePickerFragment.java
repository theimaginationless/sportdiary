package com.app.dmitryteplyakov.sportdiary.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.app.dmitryteplyakov.sportdiary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dmitry21 on 09.08.17.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIMEPICKER_UUID = "com.timepickerfragment.arg_timepicker_uuid";
    public static final String RETURN_TIME = "com.timepickerfragment.return_time";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIMEPICKER_UUID, id);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_timepicker, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        Calendar currentDate = Calendar.getInstance();
                        int year = currentDate.get(Calendar.YEAR);
                        int month = currentDate.get(Calendar.MONTH);
                        int day = currentDate.get(Calendar.DAY_OF_MONTH);
                        int minute;
                        int hour;
                        if(Build.VERSION.SDK_INT >= 23) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }
                        calendar.set(year, month, day, hour, minute);
                        sendResult(Activity.RESULT_OK, calendar.getTime());
                    }
                }).create();
    }

    private void sendResult(int resultCode, Date time) {
        if(resultCode == Activity.RESULT_OK) {
            Intent date = new Intent();
            date.putExtra(RETURN_TIME, time);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, date);
        }
    }
}
