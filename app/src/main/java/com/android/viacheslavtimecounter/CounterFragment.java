package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.StatisticDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
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

    private BroadcastReceiver mOnChangeTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getExtras().get(AddDoingNameFragment.EXTRA_CHANGED_TIME));
            mTotalTime = (int) intent.getExtras().get(AddDoingNameFragment.EXTRA_CHANGED_TIME);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");

        IntentFilter filter = new IntentFilter(AddDoingNameFragment.ACTION_TOTAL_TIME_CHANGED);
        Objects.requireNonNull(getActivity()).registerReceiver(mOnChangeTimeReceiver, filter);

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

        mDoing = StatisticDoingsLab
                .getStatisticDoingsLab(getActivity())
                .getDayDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                .getDoing(doingID);


        view.setBackgroundColor(mDoing.getColor());
        view.getBackground().setAlpha(127);

        mCurrentTime = 0;
        mTotalTime = mDoing.getTotalTimeInt();

        if (mDoing.getId().equals(LastStartedDoingPreferences.getStartedDoingID(mActivity))) {//if it was after recreate
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

            });
        }
    }

    private void updateDoing() {
        mDoing.setTotalTimeInt(mCurrentTime + mTotalTime);
        StatisticDoingsLab.getStatisticDoingsLab(getActivity())
                .getDayDoings(TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity)))
                .updateDoing(mDoing);
    }

    private boolean checkMidnight() {
        Calendar startDate = TimeHelper.getDateCalendar(LastStartedDoingPreferences.getStartDate(mActivity));
        Calendar currentDate = new GregorianCalendar();
        return currentDate.get(Calendar.DAY_OF_MONTH) != startDate.get(Calendar.DAY_OF_MONTH);
    }

    private void checkDate() {
        if (checkMidnight()) {
            synchronized (this) {
                Calendar today = new MyCalendar();
                int prevTotalTime = (int) ((today.getTimeInMillis() - LastStartedDoingPreferences.getStartTimeMillis(mActivity)) / 1000);
                mCurrentTime = prevTotalTime;
                updateDoing();

                mDoing = new Doing(mDoing.getTitle(), mDoing.getColor());
                StatisticDoingsLab.getStatisticDoingsLab(mActivity)
                        .getDayDoings(today)
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
