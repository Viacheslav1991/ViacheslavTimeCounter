package com.android.viacheslavtimecounter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.viacheslavtimecounter.model.StatisticDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.DoingNameLab;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import petrov.kristiyan.colorpicker.ColorPicker;

public class AddDoingNameFragment extends DialogFragment {
    private static final String ARG_DOING_NAME_TITLE = "doing_name_title";
    protected static final String EXTRA_CHANGED_TIME =
            "com.android.viacheslavtimecounter.changedtime";
    public static final String ACTION_TOTAL_TIME_CHANGED =
            "com.android.viacheslavtimecounter.TOTAL_TIME_CHANGED";

    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "DialogTime";
    private DoingName mDoingName;
    private Doing mDoing;
    private EditText mTitleField;
    private Button mSelectColorButton;
    private Button mChangeTimeButton;
    private ColorPicker mColorPicker;
    private Callbacks mCallbacks;
    private AlertDialog mAlertDialog;
    private boolean mDoingNameExisted = false;

    public interface Callbacks {
        void onDoingNameUpdated();
    }

    public static AddDoingNameFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOING_NAME_TITLE, title);

        AddDoingNameFragment fragment = new AddDoingNameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title;
        if (getArguments() != null && (title = (String) getArguments().getSerializable(ARG_DOING_NAME_TITLE)) != null) {
            mDoingName = DoingNameLab.getDoingNameLab(getActivity()).getDoingName(title);
            mDoing = StatisticDoingsLab.getStatisticDoingsLab(getActivity())
                    .getDayDoings(new MyCalendar())
                    .getDoing(mDoingName.getTitle());
            mDoingNameExisted = true;
        } else {
            mDoingName = new DoingName();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_new_doing, null);

        mTitleField = view.findViewById(R.id.enter_name_edit_text);
        mSelectColorButton = view.findViewById(R.id.select_color_button);

        if (mDoingNameExisted) {
            mSelectColorButton.setBackgroundColor(mDoingName.getColor());
            mTitleField.setText(mDoingName.getTitle());
        }


        mChangeTimeButton = view.findViewById(R.id.time_button);

        if (mDoing != null
                /*&& !mDoing.getId().equals(LastStartedDoingPreferences
                        .getStartedDoingID(getActivity()))*/) {//We can't change running activity
            final int totalTime = mDoing.getTotalTimeInt();
            mChangeTimeButton.setText(TimeHelper.getTime(totalTime));
            mChangeTimeButton.setOnClickListener(v -> {
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(totalTime);
                timeDialog.setTargetFragment(AddDoingNameFragment.this, REQUEST_TIME);
                timeDialog.show(getFragmentManager(), DIALOG_TIME);
            });
        } else {
            mChangeTimeButton.setVisibility(View.GONE);
        }


        mSelectColorButton.setOnClickListener(v -> mColorPicker.show());


        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDoingName.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mColorPicker = new ColorPicker(Objects.requireNonNull(getActivity()));
        mColorPicker
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onChooseColor(int position, int color) {
                        mSelectColorButton.setBackgroundColor(color);
                        mDoingName.setColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.ok, null)
                .setView(view)
                .create();

        mAlertDialog.show();
        mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDoingName.getTitle() == null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("You should enter title!")
                            .setPositiveButton(android.R.string.ok, null)
                            .create()
                            .show();

                } else if (mDoingNameExisted) {
                    DoingNameLab.getDoingNameLab(getActivity()).updateDoingName(mDoingName);
                    if (mDoing != null) { //update today's doing(name and color)
                        mDoing.setTitle(mDoingName.getTitle());
                        mDoing.setColor(mDoingName.getColor());
                        StatisticDoingsLab.getStatisticDoingsLab(getActivity())
                                .getDayDoings(new MyCalendar())
                                .updateDoing(mDoing);
                    }
                    mAlertDialog.cancel();
                } else {
                    DoingNameLab.getDoingNameLab(getActivity()).addDoingName(mDoingName);
                    mAlertDialog.cancel();
                }
            }
        });
        return mAlertDialog;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks.onDoingNameUpdated();
        mCallbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_TIME) {
            int timeSec = (int) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME_INT);

            if (mDoing.getId().equals(LastStartedDoingPreferences.getStartedDoingID(getActivity()))) {
                Intent intent = new Intent(ACTION_TOTAL_TIME_CHANGED);
                intent.putExtra(EXTRA_CHANGED_TIME, timeSec);
                Objects.requireNonNull(getActivity()).sendBroadcast(intent);
            }

            mDoing.setTotalTimeInt(timeSec);
            mChangeTimeButton.setText(TimeHelper.getTime(timeSec));
        }
    }
}
