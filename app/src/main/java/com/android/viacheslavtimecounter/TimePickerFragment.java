package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME_INT =
            "time";
    protected static final String EXTRA_TIME_INT =
            "com.android.viacheslavtimecounter.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Integer timeInt) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME_INT, timeInt);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int timeIntSec = (Integer) getArguments().getSerializable(ARG_TIME_INT);
        int hour = timeIntSec / 3600,
                min = timeIntSec / 60 % 60;

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(min);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            int selectedHour = mTimePicker.getHour();
                            int selectedMinute = mTimePicker.getMinute();
                            int seconds = (selectedHour * 60 * 60 + selectedMinute * 60);
                            sendResult(Activity.RESULT_OK, seconds);
                        })
                .create();
    }

    private void sendResult(int resultCode, Integer timeInt) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME_INT, timeInt);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
