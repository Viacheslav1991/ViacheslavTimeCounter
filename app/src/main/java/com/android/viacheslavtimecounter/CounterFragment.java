package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "CounterFragment";
    private Doing mDoing;
    private int mCurrentTime;
    private int mTotalTime;
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
    public void onStart() {
/*
        synchronized (this) {
            if (checkMidnight()) {
                Log.i(TAG, "Midnight onStart");
                Calendar today = new MyCalendar();
                int prevTotalTime = (int) ((today.getTimeInMillis() - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
                mDoing.setTotalTimeInt(prevTotalTime);
                DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                        .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                        .updateDoing(mDoing);

                mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                DayStatisticListDoingsLab.getDayStatisticListDoingsLab(mActivity)
                        .getDayStatisticListDoings(today)
                        .addDoing(mDoing);
                int todayTotalTime = (int) ((System.currentTimeMillis() - today.getTimeInMillis()) / 1000);

                LastStartedDoingPreferences.setStartTime(mActivity, today.getTimeInMillis(),
                        mDoing.getId(), 0, TimeHelper.getDateString(today));
                mCurrentTime = todayTotalTime;
                mTotalTime = 0;
            }
        }
*/

        Log.i(TAG, "onStart");
        checkDate();
        super.onStart();
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
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_counter, container, false);
        UUID doingID = (UUID) getArguments().getSerializable(ARG_DOING_ID);

        mDoing = DayStatisticListDoingsLab
                .getDayStatisticListDoingsLab(getActivity())
                .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                .getDoing(doingID);


        view.setBackgroundColor(mDoing.getColor());
        view.getBackground().setAlpha(127);

        mCurrentTime = 0;
        mTotalTime = mDoing.getTotalTimeInt();

        if (mDoing.getId().equals(LastStartedDoingPreferences.getStartedDoingID(mActivity))) {//if it was after recreate
            isRecreated = true;
            mCurrentTime = (int) (((System.currentTimeMillis()) - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
            mTotalTime = LastStartedDoingPreferences.getTotalTimeSec(mActivity);
        }


        mDoingTextView = view.findViewById(R.id.counterTitleTextView);
        mTotalTimeTextView = view.findViewById(R.id.counterTotalTimeTextView);
        mCurrentTimeTextView = view.findViewById(R.id.counterCurrentTimeTextView);

        mDoingTextView.setText(mDoing.getTitle());
        mTotalTimeTextView.setText(TimeHelper.getTime(mTotalTime + mCurrentTime));
        mCurrentTimeTextView.setText(TimeHelper.getTime(mCurrentTime));

        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);

        return view;
    }

    @Override
    public void onStop() {
        updateDoing();
        super.onStop();
    }
    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            mActivity.runOnUiThread(() -> {
                mTotalTimeTextView.setText(TimeHelper.getTime(mTotalTime + mCurrentTime));
                mCurrentTimeTextView.setText(TimeHelper.getTime(mCurrentTime++));

                checkDate();
                updateDoing();


/*
                synchronized (this) {
                    if (checkMidnight()) {
                        */
/*Toast.makeText(mActivity, "Midnight run", Toast.LENGTH_SHORT).show();

                        mDoing.setTotalTimeInt(mCurrentTime + mTotalTime);
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                                .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                                .updateDoing(mDoing);

                        mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(mActivity)
                                .getDayStatisticListDoings(new MyCalendar())
                                .addDoing(mDoing);
                        LastStartedDoingPreferences.setStartTime(mActivity, System.currentTimeMillis(),
                                mDoing.getId(), 0, TimeHelper.getDateString(new MyCalendar()));
                        mCurrentTime = 0;
                        mTotalTime = 0;*//*


                        Calendar today = new MyCalendar();
                        int prevTotalTime = (int) ((today.getTimeInMillis() - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
                        mDoing.setTotalTimeInt(prevTotalTime);
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                                .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                                .updateDoing(mDoing);

                        mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(mActivity)
                                .getDayStatisticListDoings(today)
                                .addDoing(mDoing);
                        int todayTotalTime = (int) ((System.currentTimeMillis() - today.getTimeInMillis()) / 1000);

                        LastStartedDoingPreferences.setStartTime(mActivity, today.getTimeInMillis(),
                                mDoing.getId(), 0, TimeHelper.getDateString(today));
                        mCurrentTime = todayTotalTime;
                        mTotalTime = 0;
                    }
                }
*/

            });
        }
    }

    private void updateDoing() {
        mDoing.setTotalTimeInt(mCurrentTime + mTotalTime);
        DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                .updateDoing(mDoing);
    }

    private boolean checkMidnight() {
        Calendar startDate = TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity));
        Calendar currentDate = new GregorianCalendar();
//        currentDate.setTimeInMillis(System.currentTimeMillis());
        return currentDate.get(Calendar.DAY_OF_MONTH) != startDate.get(Calendar.DAY_OF_MONTH);
    }

    private void checkDate() {
        if (checkMidnight()) {
            synchronized (this) {
                Calendar today = new MyCalendar();
                int prevTotalTime = (int) ((today.getTimeInMillis() - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
                mDoing.setTotalTimeInt(prevTotalTime);
                DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                        .getDayStatisticListDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                        .updateDoing(mDoing);

                mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                DayStatisticListDoingsLab.getDayStatisticListDoingsLab(mActivity)
                        .getDayStatisticListDoings(today)
                        .addDoing(mDoing);
                int todayTotalTime = (int) ((System.currentTimeMillis() - today.getTimeInMillis()) / 1000);

                LastStartedDoingPreferences.setStartTime(mActivity, today.getTimeInMillis(),
                        mDoing.getId(), 0, TimeHelper.getDateString(today));
                mCurrentTime = todayTotalTime;
                mTotalTime = 0;
            }

        }
    }


}
