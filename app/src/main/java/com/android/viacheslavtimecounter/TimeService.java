package com.android.viacheslavtimecounter;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class TimeService extends Service {
    private static final String TAG = "TimeService";
    private static final String EXTRA_DOING_TOTAL_TIME =
            "com.android.viacheslavtimecounter.totaltime";
    public static final String EXTRA_DOING_CURRENT_TIME =
            "com.android.viacheslavtimecounter.currenttime";
    private static final int REFRESH_RATE = 1000;
    private int totalTime;
    private MyTimerTask mTimerTask;
    private Timer mTimer;
    private Intent mBroadcastIntent;

    public static final String STOPWATCH_BR = "com.android.viacheslavtimecounter.timerbroadcast";

    public static Intent newIntent(Context context, int totalTime) {
        Intent intent = new Intent(context, TimeService.class);
        intent.putExtra(EXTRA_DOING_TOTAL_TIME, totalTime);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        mBroadcastIntent = new Intent(STOPWATCH_BR);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        totalTime = intent.getIntExtra(EXTRA_DOING_TOTAL_TIME, 0);
        mTimer.scheduleAtFixedRate(mTimerTask, 0, REFRESH_RATE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Destroyed");
        mTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Log.i(TAG, "started, time = " + totalTime++);
            String time = String.valueOf(totalTime);
            mBroadcastIntent.putExtra(EXTRA_DOING_CURRENT_TIME, time);
            sendBroadcast(mBroadcastIntent);
        }
    }
}
