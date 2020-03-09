package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
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

    private MyTimerTask mTimerTask;
    private Timer mTimer;
    private Activity mActivity;

    private boolean isRecreated = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

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
        currentTime = 0;
        totalTime = mDoing.getTotalTimeInt();

        if (mDoing.getId().equals(LastStartedDoingPreferences.getStartedDoingID(mActivity))) {//if it was after recreate
            isRecreated = true;
            currentTime = (int) (((System.currentTimeMillis()) - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
            totalTime = LastStartedDoingPreferences.getTotalTimeSec(mActivity);
        }



        mDoingTextView = view.findViewById(R.id.counterTitleTextView);
        mTotalTimeTextView = view.findViewById(R.id.counterTotalTimeTextView);
        mCurrentTimeTextView = view.findViewById(R.id.counterCurrentTimeTextView);

        mDoingTextView.setText(mDoing.getTitle());
        mTotalTimeTextView.setText(TimeHelper.getTime(totalTime + currentTime));
        mCurrentTimeTextView.setText(TimeHelper.getTime(currentTime));

        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mDoing.setTotalTimeInt(currentTime + totalTime);
        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                .getDayStatisticListDoings(new MyCalendar())
                .updateDoing(mDoing);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mTotalTimeTextView.setText( TimeHelper.getTime(totalTime + currentTime));
                    mCurrentTimeTextView.setText(TimeHelper.getTime(currentTime++));
                    if (checkMidnight()) {
                        Toast.makeText(mActivity, "Midnight", Toast.LENGTH_SHORT).show();

                        mDoing.setTotalTimeInt(currentTime + totalTime);
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                                .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                                .updateDoing(mDoing);

                        mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(mActivity)
                                .getDayStatisticListDoings(new MyCalendar())
                                .addDoing(mDoing);
                        LastStartedDoingPreferences.setStartTime(mActivity, System.currentTimeMillis(),
                                mDoing.getId(), 0, TimeHelper.getDateString(new MyCalendar()));
                        currentTime = 0;
                        totalTime = 0;
                    }
                }
            });
        }
    }


    private boolean checkMidnight() {
        Calendar startDate = TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity));
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTimeInMillis(System.currentTimeMillis());
        return currentDate.get(Calendar.DAY_OF_MONTH) != startDate.get(Calendar.DAY_OF_MONTH);
    }


}
