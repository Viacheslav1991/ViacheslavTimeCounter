package com.android.viacheslavtimecounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.DoingNameLab;

import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import petrov.kristiyan.colorpicker.ColorPicker;

public class AddDoingNameFragment extends DialogFragment {
    private static final String ARG_DOING_NAME_TITLE = "doing_name_title";
    private DoingName mDoingName;
    private Doing mDoing;
    private EditText mTitleField;
    private Button mSelectColorButton;
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
            mDoing = DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                    .getDayStatisticListDoings(new GregorianCalendar())
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
                .inflate(R.layout.dialog_new_employment, null);

        mTitleField = view.findViewById(R.id.enter_name_edit_text);
        mSelectColorButton = view.findViewById(R.id.select_color_button);

        if (mDoingNameExisted) {
            mSelectColorButton.setBackgroundColor(mDoingName.getColor());
            mTitleField.setText(mDoingName.getTitle());
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



        mColorPicker = new ColorPicker(getActivity());
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
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                                .getDayStatisticListDoings(new GregorianCalendar())
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
}
