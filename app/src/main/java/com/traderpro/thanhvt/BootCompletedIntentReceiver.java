package com.traderpro.thanhvt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent myIntent = new Intent(context, DetectSignalService.class);
        context.startService(myIntent);

        NotiEventReceiver.setupAlarm(context);
    }
}
