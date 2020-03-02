package com.android.viacheslavtimecounter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CounterFragment extends Fragment {
    private static final String ARG_DOING_ID = "doing_id";
    private Doing mDoing;
    private int currentTime;
    private int totalTime;
    private TextView mDoingTextView;
    private TextView mTotalTimeTextView;
    private TextView mCurrentTimeTextView;
    
    public static CounterFragment newInstance(UUID doingID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DOING_ID, doingID);

        CounterFragment fragment = new CounterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        UUID doingID = (UUID) getArguments().getSerializable(ARG_DOING_ID);
        mDoing = DayStatisticListDoingsLab
                .getDayStatisticListDoingsLab(getActivity())
                .getDayStatisticListDoings(new MyCalendar())
                .getDoing(doingID);

        totalTime = mDoing.getTotalTimeInt();
        currentTime = 0;

        mDoingTextView = view.findViewById(R.id.counterTitleTextView);
        mTotalTimeTextView = view.findViewById(R.id.counterTotalTimeTextView);
        mCurrentTimeTextView = view.findViewById(R.id.counterCurrentTimeTextView);

        mDoingTextView.setText(mDoing.getTitle());
        mTotalTimeTextView.setText(getActivity().getString(R.string.total_time_is, TimeHelper.getTime(totalTime + currentTime)));
        mCurrentTimeTextView.setText(getActivity().getString(R.string.current_time_is, TimeHelper.getTime(currentTime++)));

        return view;
    }
}
