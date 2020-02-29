package com.android.viacheslavtimecounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.viacheslavtimecounter.model.DoingName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import petrov.kristiyan.colorpicker.ColorPicker;

public class AddDoingNameFragment extends DialogFragment {
    private DoingName mDoingName;
    private EditText mTitleField;
    private Button selectColorButton;
    private ColorPicker mColorPicker;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDoingName = new DoingName();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_new_employment, null);

        mTitleField = view.findViewById(R.id.enter_name_edit_text);
//        mTitleField.setText(mDoingName.getTitle());
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

        selectColorButton = view.findViewById(R.id.select_color_button);
        /*if (change) {
            selectColorButton.setBackgroundColor(mListEmploymentsItem.getColor());
        }*/
        selectColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorPicker.show();
            }
        });

        mColorPicker = new ColorPicker(getActivity());
        final int[] colors = getResources().getIntArray(R.array.default_colors);
        mColorPicker
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onChooseColor(int position, int color) {
                        selectColorButton.setBackgroundColor(color);
                        mDoingName.setColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        return new AlertDialog.Builder(getActivity())
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDoingName.getTitle() == null) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("You should enter title!")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .create()
                                    .show();

                        }
                        /*else if (!change) {
                            EmploymentLab.getInstance(getContext()).addItemListEmployment(mListEmploymentsItem);
                        }*/
                    }
                })
                .setView(view)
                .create();


    }


}
