package com.android.viacheslavtimecounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChangeDateReceiver extends BroadcastReceiver {
    private static final String TAG = "ChangeDateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());
    }
}
